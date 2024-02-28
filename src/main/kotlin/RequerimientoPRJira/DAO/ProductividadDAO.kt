package RequerimientoPRJira.DAO

import IssueXLSX.Model.Issue
import PullRequestCSV.Model.PullRequests
import RequerimientoPRJira.Model.ProductividadPRIssue
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.FileOutputStream
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ProductividadDAO {

    fun combinarListadoPRIssue(pullRequests: List<PullRequests>, issues: List<Issue>) {
        val datosCombinados = combinarPorCodigoDeHistoria(pullRequests, issues)
        generarArchivoUnificado(datosCombinados)
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

    private fun generarArchivoUnificado(unificados: List<ProductividadPRIssue>) {
        val workbook = XSSFWorkbook()
        val sheet = workbook.createSheet("Unificado")

        val headerRow = sheet.createRow(0)
        headerRow.createCell(0).setCellValue("Correo PR")
        headerRow.createCell(1).setCellValue("Nombre de Historia")
        //headerRow.createCell(2).setCellValue("Fecha Terminado/PR Merged")

        var rowNum = 1

        for ((pullRequest, issue) in unificados) {
            val row = sheet.createRow(rowNum++)

            if (issue != null || pullRequest != null) {
                //TODO 3. unificar 2 excels, atraves del codigo de Historia(A y E)
                val correoPR = pullRequest?.Email
                val nombreHistoria = pullRequest?.UserStory ?: issue?.NombreHistoria ?: ""
                //val fechaTerminado = issue?.FechaTerminado ?: pullRequest?.PRMerged ?: ""

                row.createCell(0).setCellValue(correoPR)
                row.createCell(1).setCellValue(nombreHistoria)
                //row.createCell(2).setCellValue(fechaTerminado)

            }
        }

        FileOutputStream("D:\\PullRequestIssue\\Archivo_unificado.xlsx").use { outputStream ->
            workbook.write(outputStream)
        }
    }

//    fun combinarListadoPRIssue(pullRequests: List<PullRequests>, issues: List<Issue>) {
//        val pullRequestsFiltrados = filtrarPullRequests(pullRequests)
//        val issuesFiltrados = filtrarIssuesJira(issues)
//        val datosCombinados = combinarPorCodigoDeHistoria(pullRequestsFiltrados, issuesFiltrados)
//        generarArchivoUnificado(datosCombinados)
//    }
    //    fun filtrarPullRequests(pullRequests: List<PullRequests>): List<PullRequests> {
//        return pullRequests.filter { it.PRMerged.isNotEmpty() }
//    }
//
//     fun filtrarIssuesJira(issues: List<Issue>): List<Issue> {
//        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
//        return issues.filter { issue ->
//            val fechaTerminado = issue.FechaTerminado
//            if (fechaTerminado.isNotEmpty()) {
//                val fecha = LocalDate.parse(fechaTerminado, formatter)
//                fecha.monthValue == 1
//            } else {
//                false
//            }
//        }
//    }
}