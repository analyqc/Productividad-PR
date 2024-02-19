import Productivity.AutomatizerFilters.Model.IssueExcel
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.FileOutputStream

class ExcelGenerator {
    fun generarExcel(issues: List<IssueExcel>, rutaArchivo: String) {
        val workbook = XSSFWorkbook()
        val sheet = workbook.createSheet("Issues Periodo")

        val headerRow = sheet.createRow(0)
        headerRow.createCell(0).setCellValue("Encargado")
        headerRow.createCell(1).setCellValue("Total de PR")
        headerRow.createCell(2).setCellValue("Total de Historias")

        var rowNum = 1
        for (issue in issues) {
            val row = sheet.createRow(rowNum++)
            row.createCell(0).setCellValue(issue.encargado)
            row.createCell(1).setCellValue(issue.totalTareas.toDouble())
            row.createCell(2).setCellValue(issue.totalHistorias.toDouble())
        }

        val outputStream = FileOutputStream(rutaArchivo)
        workbook.write(outputStream)
        outputStream.close()
        workbook.close()

        println("Archivo Excel generado exitosamente en la ruta: $rutaArchivo")
    }
}
