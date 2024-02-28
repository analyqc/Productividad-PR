package RequerimientoPRJira.DAO

import IssueXLSX.Model.Issue
import PullRequestCSV.Model.PullRequests
import RequerimientoPRJira.Model.ProductividadPRIssue
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.FileOutputStream

class ProductividadDAO {

    fun combinarListadoPRIssue(pullRequests: List<PullRequests>, issues: List<Issue>) {
        val datosCombinados = combinarPorCodigoDeHistoria(pullRequests, issues)
        val datosFiltrados = filtrarYEliminarDuplicados(pullRequests, datosCombinados)
        generarArchivoUnificado(datosFiltrados)
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


    private fun generarArchivoUnificado(unificados: List<ProductividadPRIssue>) {
        val workbook = XSSFWorkbook()
        val sheet = workbook.createSheet("Unificado")

        val headerRow = sheet.createRow(0)
        headerRow.createCell(0).setCellValue("Correo PR")
        headerRow.createCell(1).setCellValue("Nombre de Historia")

        var rowNum = 1

        for (item in unificados) {
            val row = sheet.createRow(rowNum++)

            if (item.pullRequest != null) {
                val correoPR = item.pullRequest.Email
                val nombreHistoria = item.pullRequest.UserStory ?: ""
                row.createCell(0).setCellValue(correoPR)
                row.createCell(1).setCellValue(nombreHistoria)
            }
        }

        FileOutputStream("D:\\PullRequestIssue\\Archivo_unificado.xlsx").use { outputStream ->
            workbook.write(outputStream)
        }
    }
}
