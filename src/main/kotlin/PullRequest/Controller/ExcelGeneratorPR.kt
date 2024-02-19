package PullRequest.Controller

import PullRequest.Model.RepositoryExcel
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.FileOutputStream

class ExcelGeneratorPR {
    fun generarExcel(issues: List<RepositoryExcel>, rutaArchivo: String) {
        val workbook = XSSFWorkbook()
        val sheet = workbook.createSheet("Cantidad PR")

        val headerRow = sheet.createRow(0)
        headerRow.createCell(0).setCellValue("Email")
        headerRow.createCell(1).setCellValue("User Story")
        headerRow.createCell(2).setCellValue("Total PR")

        var rowNum = 1
        for (issue in issues) {
            val row = sheet.createRow(rowNum++)
            row.createCell(0).setCellValue(issue.email)
            row.createCell(1).setCellValue(issue.userHistory)
            row.createCell(2).setCellValue(issue.totalPR.toDouble())
        }

        val outputStream = FileOutputStream(rutaArchivo)
        workbook.write(outputStream)
        outputStream.close()
        workbook.close()

        println("Archivo Excel generado exitosamente en la ruta: $rutaArchivo")
    }
}