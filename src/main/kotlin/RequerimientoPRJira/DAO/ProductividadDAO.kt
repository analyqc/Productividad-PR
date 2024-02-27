package RequerimientoPRJira.DAO

import IssueXLSX.Model.Issue
import PullRequestCSV.Model.PullRequests
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.FileOutputStream

class ProductividadDAO {

    fun combinarListadoPRIssue(pullRequests: List<PullRequests>, issues: List<Issue>){
        val unificados = unificarPorUserStory(pullRequests, issues)
        generarArchivoUnificado(unificados, "D:\\PullRequestIssue\\Archivo_unificado.xlsx")
    }

    private fun unificarPorUserStory(pullRequests: List<PullRequests>, issues: List<Issue>): List<Pair<PullRequests?, Issue?>> {
        val unificados = mutableListOf<Pair<PullRequests?, Issue?>>()

        // Agregar todas las PullRequests al resultado
        unificados.addAll(pullRequests.map { Pair(it, null) })

        // Agregar todas las Issues al resultado
        unificados.addAll(issues.map { Pair(null, it) })

        return unificados
    }

    private fun generarArchivoUnificado(unificados: List<Pair<PullRequests?, Issue?>>, rutaArchivo: String) {
        val workbook = XSSFWorkbook()
        val sheet = workbook.createSheet("Unificado")

        val headerRow = sheet.createRow(0)

        headerRow.createCell(0).setCellValue("Estado Issue")
        headerRow.createCell(1).setCellValue("Nombre de Historia Issue")
        headerRow.createCell(2).setCellValue("Fecha Terminado Issue ")
        headerRow.createCell(3).setCellValue("User Story PullRequest")
        headerRow.createCell(4).setCellValue("PRstate PullRequest")
        headerRow.createCell(5).setCellValue("PR Merged PullRequest")

        var rowNum = 1
        for ((pullRequest, issue) in unificados) {
            val row = sheet.createRow(rowNum++)
            if (issue != null) {

                row.createCell(0).setCellValue(issue.Estado)
                row.createCell(1).setCellValue(issue.NombreHistoria)
                row.createCell(2).setCellValue(issue.FechaTerminado)
            }
            if (pullRequest != null) {
                row.createCell(3).setCellValue(pullRequest.UserStory)
                row.createCell(4).setCellValue(pullRequest.PRState)
                row.createCell(5).setCellValue(pullRequest.PRMerged)
            }
        }

        FileOutputStream(rutaArchivo).use { outputStream ->
            workbook.write(outputStream)
        }
    }
}