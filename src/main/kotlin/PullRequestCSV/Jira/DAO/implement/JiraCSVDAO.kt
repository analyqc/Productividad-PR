package Jira.DAO.implement
import Jira.DAO.JiraDAO
import Jira.Model.CsvJira
import org.apache.poi.ss.usermodel.WorkbookFactory
import org.apache.poi.xssf.usermodel.XSSFSheet
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.time.LocalDate
import java.time.format.DateTimeFormatter

open class JiraCSVDAO: JiraDAO {

    override fun getJira(): List<CsvJira> {
        ConsolidarCSV()
        return importarXLSConsolidado()
    }

    fun leerJiraDeCSV(): List<CsvJira> {

        val entradaDearchivosJira = listOf(
            "D:\\Documentos\\PullRequest_010124.0526.csv",
            "D:\\Documentos\\PullRequest_050224.0802.csv",
            "D:\\Documentos\\PullRequest_080124.0621.csv",
            "D:\\Documentos\\PullRequest_120224.0907.csv",
            "D:\\Documentos\\PullRequest_150124.0620.csv",
            "D:\\Documentos\\PullRequest_190224.0634.csv",
            "D:\\Documentos\\PullRequest_220124.0754.csv",
            "D:\\Documentos\\PullRequest_290124.0821.csv"
        )

        val jira = ArrayList<CsvJira>()

        for (entradaDeArchivo in entradaDearchivosJira) {
            val archivoJiraCSV = File(entradaDeArchivo)

            archivoJiraCSV.bufferedReader().useLines { lines ->
                lines.drop(1)
                    .forEach { line ->
                        val tokens = line.split(",")
                        val csvPullRequest = CsvJira(
                            Resumen = tokens[0],
                            Clave = tokens[1],
                            Id = tokens[2].toIntOrNull() ?: 0,
                            PadreUndefine = tokens[3],
                            TipoIncidencia = tokens[4],
                            Estado = tokens[5],
                            ProyectoKey = tokens[6],
                            Proyecto = tokens[7],
                            Responsable = tokens[8],
                            ResponsableAccountId = tokens[9],
                            EstimacionOriginal = tokens[10].toIntOrNull() ?: 0,
                            TiempoEmpleado = tokens[11].toIntOrNull() ?: 0,
                            FechaEnProgreso = tokens[12],
                            FechaEnTerminado = tokens[13],
                            ResponsableEmailAddress = tokens[14]
                        )
                        jira.add(csvPullRequest)
                    }
            }
        }
        return jira
    }
    fun ConsolidarCSV() {
        val jirasDocumentos = leerJiraDeCSV()
        val jiraIdentificador = mutableMapOf<Int, CsvJira>()

        for (jira in jirasDocumentos) {
            val ordenarPullRequestId = jiraIdentificador[jira.Id]

            if (ordenarPullRequestId == null || LocalDate.parse(jira.FechaEnTerminado, DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                > LocalDate.parse(ordenarPullRequestId.FechaEnTerminado, DateTimeFormatter.ofPattern("dd/MM/yyyy"))) {
                jiraIdentificador[jira.Id] = jira
            }
        }

        val workbook = XSSFWorkbook()
        val sheet = workbook.createSheet("DATA")

        generarCabecera(sheet)
        generarCuerpo(sheet,jiraIdentificador)
    }

    fun importarXLSConsolidado(): List<CsvJira> {
        val jiraXLS = mutableListOf<CsvJira>()

        val rutaDelArchivo = FileInputStream("D:\\PullRequest\\Jira.xlsx")
        val workbook = WorkbookFactory.create(rutaDelArchivo)
        val sheet = workbook.getSheetAt(0)

        for (rowNum in 1 until sheet.physicalNumberOfRows) {
            val row = sheet.getRow(rowNum)
            val csvJira = CsvJira(
                Resumen = row.getCell(0).stringCellValue,
                Clave = row.getCell(1).stringCellValue,
                Id = row.getCell(2).numericCellValue.toInt(),
                PadreUndefine = row.getCell(3).stringCellValue,
                TipoIncidencia = row.getCell(4).stringCellValue,
                Estado = row.getCell(5).stringCellValue,
                ProyectoKey = row.getCell(6).stringCellValue,
                Proyecto = row.getCell(7).stringCellValue,
                Responsable = row.getCell(8)?.stringCellValue  ?: "",
                ResponsableAccountId = row.getCell(9)?.stringCellValue  ?: "",
                EstimacionOriginal = row.getCell(10).numericCellValue.toInt(),
                TiempoEmpleado = row.getCell(11).numericCellValue.toInt(),
                FechaEnProgreso = row.getCell(12).stringCellValue,
                FechaEnTerminado = row.getCell(13).stringCellValue,
                ResponsableEmailAddress = row.getCell(14)?.stringCellValue  ?: "",
            )
            jiraXLS.add(csvJira)
        }
        workbook.close()
        rutaDelArchivo.close()

        return jiraXLS
    }


    private fun convertirOrigenAFecha(numeroOrigen: Int): String {
        val dateString = numeroOrigen.toString().padStart(6, '0')
        val formatter = DateTimeFormatter.ofPattern("ddMMyy")
        val date = LocalDate.parse(dateString, formatter)
        return date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
    }

    fun generarCabecera( sheet: XSSFSheet){
        val headerRow = sheet.createRow(0)
        headerRow.createCell(0).setCellValue("UserName")
        headerRow.createCell(1).setCellValue("Email")
        headerRow.createCell(2).setCellValue("Repository")
        headerRow.createCell(3).setCellValue("Branch")
        headerRow.createCell(4).setCellValue("User Story")
        headerRow.createCell(5).setCellValue("PR Number")
        headerRow.createCell(6).setCellValue("PR Title")
        headerRow.createCell(7).setCellValue("PR State")
        headerRow.createCell(8).setCellValue("PR Created")
        headerRow.createCell(9).setCellValue("PR Merged")
        headerRow.createCell(10).setCellValue("PR Closed")
        headerRow.createCell(11).setCellValue("Id")
        headerRow.createCell(12).setCellValue("PR Reviewers")
        headerRow.createCell(13).setCellValue("Origen")
    }

    fun generarCuerpo(sheet: XSSFSheet, jiraIdentificador: MutableMap<Int, CsvJira>){
        val jirasOrdenados = jiraIdentificador.values.sortedBy {it.Id}
        var rowNum = 1

        for (jira in jirasOrdenados) {
            val row = sheet.createRow(rowNum++)
            row.createCell(0).setCellValue(jira.Resumen)
            row.createCell(1).setCellValue(jira.Clave)
            row.createCell(2).setCellValue(jira.Id.toDouble())
            row.createCell(3).setCellValue(jira.PadreUndefine)
            row.createCell(4).setCellValue(jira.TipoIncidencia)
            row.createCell(5).setCellValue(jira.Estado)
            row.createCell(6).setCellValue(jira.ProyectoKey)
            row.createCell(7).setCellValue(jira.Proyecto)
            row.createCell(8).setCellValue(jira.Responsable)
            row.createCell(9).setCellValue(jira.ResponsableAccountId)
            row.createCell(10).setCellValue(jira.EstimacionOriginal.toDouble())
            row.createCell(11).setCellValue(jira.TiempoEmpleado.toDouble())
            row.createCell(12).setCellValue(jira.FechaEnProgreso)
            row.createCell(13).setCellValue(jira.FechaEnTerminado)
            row.createCell(14).setCellValue(jira.ResponsableEmailAddress)
        }
        FileOutputStream("D:\\PullRequest\\Jira.xlsx").use { outputStream ->
            XSSFWorkbook().write(outputStream)
        }
    }
}
