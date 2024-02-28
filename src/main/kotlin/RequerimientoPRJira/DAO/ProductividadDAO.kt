package RequerimientoPRJira.DAO

import IssueXLSX.Model.Issue
import PullRequestCSV.Model.PullRequests
import RequerimientoPRJira.Model.ProductividadPRIssue
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.FileOutputStream

class ProductividadDAO {

    fun combinarListadoPRIssue(pullRequests: List<PullRequests>, issues: List<Issue>) {
        val datosCombinados = combinarPorCodigoDeHistoria(pullRequests, issues)
        val repeticionesPorFila = contarRepeticionesPorFila(datosCombinados)
        val datosFiltrados = filtrarYEliminarDuplicados(pullRequests, datosCombinados)
        generarArchivoUnificado(datosFiltrados, repeticionesPorFila)
    }

    private fun combinarPorCodigoDeHistoria(pullRequests: List<PullRequests>, issues: List<Issue>): List<ProductividadPRIssue> {
        val datosCombinados = mutableListOf<ProductividadPRIssue>()

        for (pullRequest in pullRequests) {
            datosCombinados.add(ProductividadPRIssue(pullRequest, null))
        }
        for (issue in issues) {
            datosCombinados.add(ProductividadPRIssue(null, issue))
        }

        return datosCombinados
    }

    private fun contarRepeticionesPorFila(datosCombinados: List<ProductividadPRIssue>): Map<Pair<String, String>, Int> {
        val repeticionesPorFila = mutableMapOf<Pair<String, String>, Int>()

        for (dato in datosCombinados) {
            val userStory = dato.pullRequest?.UserStory ?: dato.issue?.NombreHistoria ?: ""
            val correoPR = dato.pullRequest?.Email ?: ""
            val combinacion = Pair(correoPR, userStory)
            repeticionesPorFila[combinacion] = repeticionesPorFila.getOrDefault(combinacion, 0) + 1
        }

        return repeticionesPorFila
    }

    private fun filtrarYEliminarDuplicados(pullRequests: List<PullRequests>, datosCombinados: List<ProductividadPRIssue>): List<ProductividadPRIssue> {
        val filteredPullRequests = mutableListOf<PullRequests>()
        val filteredData = mutableListOf<ProductividadPRIssue>()

        val sortedPullRequests = pullRequests.sortedBy { it.Email }
        val groupedByUserStory = sortedPullRequests.groupBy { it.UserStory }

        groupedByUserStory.forEach { (_, requests) ->
            val uniqueRequests = requests.distinctBy { it.Email }

            filteredPullRequests.addAll(uniqueRequests)
        }

        datosCombinados.forEach { item ->
            if (item.pullRequest != null && filteredPullRequests.contains(item.pullRequest)) {
                filteredData.add(item)
            }
        }

        return filteredData
    }

    private fun generarArchivoUnificado(unificados: List<ProductividadPRIssue>, repeticionesPorFila: Map<Pair<String, String>, Int>) {
        val workbook = XSSFWorkbook()
        val sheet = workbook.createSheet("Unificado")

        val headerRow = sheet.createRow(0)
        headerRow.createCell(0).setCellValue("Correo PR")
        headerRow.createCell(1).setCellValue("Nombre de Historia")
        headerRow.createCell(2).setCellValue("Veces Repetido")

        var rowNum = 1

        for (item in unificados) {
            val row = sheet.createRow(rowNum++)

            if (item.pullRequest != null) {
                val correoPR = item.pullRequest.Email
                val nombreHistoria = item.pullRequest.UserStory ?: ""
                val combinacion = Pair(correoPR, nombreHistoria)
                val repeticiones = repeticionesPorFila[combinacion] ?: 0

                row.createCell(0).setCellValue(correoPR)
                row.createCell(1).setCellValue(nombreHistoria)
                row.createCell(2).setCellValue(repeticiones.toDouble())
            }
        }

        FileOutputStream("D:\\PullRequestIssue\\Archivo_unificado.xlsx").use { outputStream ->
            workbook.write(outputStream)
        }
    }
}
