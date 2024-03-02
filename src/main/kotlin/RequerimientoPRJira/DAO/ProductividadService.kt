package RequerimientoPRJira.DAO

import IssueXLSX.Model.Issue
import PullRequestCSV.Model.PullRequests
import RequerimientoPRJira.Model.MetricaUnificada
import org.apache.poi.xssf.usermodel.XSSFSheet
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.FileOutputStream

class ProductividadService {
    fun combinarListadoPRIssue(pullRequests: List<PullRequests>, issues: List<Issue>) {
        val metricasUnificadas = agruparPorCorreoYHU(pullRequests)
        generarArchivoUnificado(metricasUnificadas,issues)
    }

    private fun agruparPorCorreoYHU(pullRequests: List<PullRequests>): List<MetricaUnificada> {
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

    private fun generarArchivoUnificado(
        metricas: List<MetricaUnificada>,
        issues: List<Issue>
    ) {
        val workbook = XSSFWorkbook()
        val sheetMetrica1 = workbook.createSheet("Metrica1HUPR")
        val sheetMetrica2 = workbook.createSheet("Metrica2HUCtdRevisiones")

        val estadoDeMetrica= calcularEstadoDelRequerimiento(metricas,issues)

        generarInformeRequerimientoYEstado(sheetMetrica1,estadoDeMetrica)
        generarInformePromedioRepeticionesPorCorreoPR(sheetMetrica2,metricas)

        FileOutputStream("D:\\PullRequestIssue\\Archivo_unificado.xlsx").use { outputStream ->
            (workbook).write(outputStream)
        }
    }

    private fun generarInformeRequerimientoYEstado(
        sheet: XSSFSheet,
        metricas: List<MetricaUnificada>,
       )
    {
        val headerRow = sheet.createRow(0)
        headerRow.createCell(0).setCellValue("Correo PR")
        headerRow.createCell(1).setCellValue("Nombre de Historia")
        headerRow.createCell(2).setCellValue("Veces Repetido")
        headerRow.createCell(3).setCellValue("Estado")

        var rowNum = 1

        for (metrica in metricas) {
            val row = sheet.createRow(rowNum++)
            row.createCell(0).setCellValue(metrica.correoPr)
            row.createCell(1).setCellValue(metrica.nombreHistoriaMetrica)
            row.createCell(2).setCellValue(metrica.repeticiones.toDouble())
            row.createCell(3).setCellValue((metrica.confirmacion))
        }
    }

    private fun generarInformePromedioRepeticionesPorCorreoPR (
        sheet: XSSFSheet,
        metricas: List<MetricaUnificada>
    ) {
        val headerRow = sheet.createRow(0)
        headerRow.createCell(0).setCellValue("Correo PR")
        headerRow.createCell(1).setCellValue("Cantidad promedio PR")

        val promediosPorCorreo = calcularPromedioPorCorreo(metricas)

        var rowNum = 1
        for ((correo, promedio) in promediosPorCorreo) {
            val row = sheet.createRow(rowNum++)
            row.createCell(0).setCellValue(correo)
            row.createCell(1).setCellValue(promedio)
        }
    }

    private fun calcularEstadoDelRequerimiento(
        metricas: List<MetricaUnificada>,
        issues: List<Issue>
    ): List<MetricaUnificada> {
        return metricas.map { metrica ->
            val existeEnIssues = issues.any { it.NombreHistoria == metrica.nombreHistoriaMetrica }
            val espaciosEnBlancoMetrica = metrica.nombreHistoriaMetrica.isNotBlank()
            val confirmacion = if (existeEnIssues && espaciosEnBlancoMetrica) "SI" else "NO"
            metrica.copy(confirmacion = confirmacion)
        }
    }

    private fun calcularPromedioPorCorreo(metricas: List<MetricaUnificada>): Map<String, Double> {
        val correos = metricas.map { it.correoPr }.distinct()
        val promediosPorCorreo = mutableMapOf<String, Double>()

        for (correo in correos) {
            val repeticiones = metricas.filter { it.correoPr == correo }.sumOf { it.repeticiones }
            val cantidadRepeticiones = metricas.count { it.correoPr == correo }
            val promedio = if (cantidadRepeticiones != 0) repeticiones.toDouble() / cantidadRepeticiones else 0.0
            promediosPorCorreo[correo] = promedio
        }

        return promediosPorCorreo
    }
}