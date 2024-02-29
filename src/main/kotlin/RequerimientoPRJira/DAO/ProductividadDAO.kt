package RequerimientoPRJira.DAO

import IssueXLSX.Model.Issue
import PullRequestCSV.Model.PullRequests
import RequerimientoPRJira.Model.MetricaUnificada
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.FileOutputStream

class ProductividadDAO {
    fun combinarListadoPRIssue(pullRequests: List<PullRequests>, issues: List<Issue>) {
        val repeticionesPorFila = contarRepeticionesPorFila(pullRequests)
        val metricasUnificadas = filtrarYEliminarDuplicados(pullRequests)
        generarArchivoUnificado(metricasUnificadas, repeticionesPorFila,issues)
    }

    private fun contarRepeticionesPorFila(pullRequests: List<PullRequests>): Map<Pair<String, String>, Int> {
        val repeticionesPorFila = mutableMapOf<Pair<String, String>, Int>()

        for (pullRequest in pullRequests) {
            val userStory = pullRequest.UserStory
            val correoPR = pullRequest.Email
            val combinacion = Pair(correoPR, userStory)

            if (!repeticionesPorFila.contains(combinacion)) {
                repeticionesPorFila[combinacion] = 1
            } else {
                val repeticion = repeticionesPorFila[combinacion] ?: 0
                repeticionesPorFila[combinacion] = repeticion + 1
            }
        }

        return repeticionesPorFila
    }

    private fun filtrarYEliminarDuplicados(pullRequests: List<PullRequests>): List<MetricaUnificada> {
        val repeticionesPorFila = mutableMapOf<Pair<String, String>, Int>()

        for (pullRequest in pullRequests) {
            val combinacion = Pair(pullRequest.Email, pullRequest.UserStory)
            repeticionesPorFila[combinacion] = repeticionesPorFila.getOrDefault(combinacion, 0) + 1
        }

        val filteredData = mutableListOf<MetricaUnificada>()

        val groupedByUserStory = pullRequests.groupBy { Pair(it.Email, it.UserStory) }
        groupedByUserStory.forEach { (_, requests) ->
            val uniqueRequests = requests.distinctBy { it.Email }
            uniqueRequests.forEach {
                filteredData.add(
                    MetricaUnificada(
                        correoPr = it.Email,
                        nombreHistoriaMetrica = it.UserStory,
                        repeticiones = repeticionesPorFila[Pair(it.Email, it.UserStory)] ?: 0,
                        confirmacion = ""
                    )
                )
            }
        }

        return filteredData
    }



    //consumire a  metricas y a lista de pull requtest
    private fun generarArchivoUnificado(
        metricas: List<MetricaUnificada>,
        repeticionesPorFila: Map<Pair<String, String>, Int>,
        issues: List<Issue>
    ) {
        val workbook = XSSFWorkbook()
        val sheet = workbook.createSheet("Unificado")

        val headerRow = sheet.createRow(0)
        headerRow.createCell(0).setCellValue("Correo PR")
        headerRow.createCell(1).setCellValue("Nombre de Historia")
        headerRow.createCell(2).setCellValue("Veces Repetido")
        headerRow.createCell(3).setCellValue("Estado")

        var rowNum = 1

        for (metrica in metricas) {
            val row = sheet.createRow(rowNum++)
            val combinacion = Pair(metrica.correoPr, metrica.nombreHistoriaMetrica)
            val repeticiones = repeticionesPorFila[combinacion] ?: 0

            row.createCell(0).setCellValue(metrica.correoPr)
            row.createCell(1).setCellValue(metrica.nombreHistoriaMetrica)
            row.createCell(2).setCellValue(repeticiones.toDouble())

            val existeEnIssues = issues.any { it.NombreHistoria == metrica.nombreHistoriaMetrica }
            val espaciosEnBlancoMetrica = metrica.nombreHistoriaMetrica.isNotBlank()

            val confirmacion = when {
                existeEnIssues && espaciosEnBlancoMetrica -> "Si existe"
                else -> "No"
            }
            row.createCell(3).setCellValue(confirmacion)
        }

        FileOutputStream("D:\\PullRequestIssue\\Archivo_unificado.xlsx").use { outputStream ->
            workbook.write(outputStream)
        }
    }

}
