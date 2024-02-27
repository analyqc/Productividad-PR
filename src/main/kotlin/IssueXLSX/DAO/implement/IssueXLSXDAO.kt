package IssueXLSX.DAO.implement

import IssueXLSX.DAO.IssueDAO
import IssueXLSX.Model.Issue
import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.WorkbookFactory
import org.apache.poi.xssf.usermodel.XSSFSheet
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.FileInputStream
import java.io.FileOutputStream

open class IssueXLSXDAO : IssueDAO {

    override fun getIssue(): List<Issue> {
        ConsolidarCSV()
        return importarXLSConsolidado()
    }

    fun leerIssueDeExcel(): List<Issue> {
        val issue = mutableListOf<Issue>()
        val filePath = "D:\\BBDDJira.xlsx"
        var contador= 0

        try {
            val archivoExcel = WorkbookFactory.create(FileInputStream(filePath))
            val hoja = archivoExcel.getSheetAt(0)

            for (filaExcel in 1 until hoja.physicalNumberOfRows) {
                val fila = hoja.getRow(filaExcel)

                val resumen = getValueString(fila, 0)
                val clave = getValueString(fila, 1)
                val id = getValueId(fila,2)
                val padre = getValueString(fila, 3)
                val tipoIncidencia = getValueString(fila, 4)
                val estado = getValueString(fila, 5)
                val proyectoKey = getValueString(fila, 6)
                val proyecto = getValueString(fila, 7)
                val responsable = getValueString(fila, 8)
                val responsableAccountId = getValueString(fila, 9)
                val estimacionOriginal = getValueDate(fila,10)
                val tiempoEmpleado = getValueDate(fila,11)
                val fechaEnProgreso = getValueDateTime(fila,12)
                val fechaEnTerminado = getValueDateTime(fila,13)
                val responsableEmailAddress = getValueString(fila, 14)
                val registrarHorasDeTrabajoStarted = getValueNumeric(fila,15)
                val registrarHorasTrabajotimeSpentSeconds =getValueNumeric(fila,16)
                val registrarHorasTrabajoauthorEmail = getValueString(fila, 17)
                val tipoDeTrabajo = getValueString(fila, 18)
                val mes = getValueString(fila, 19)
                val llave = getValueString(fila, 20)
                val tiempoEmpleadoEnHoras = getValueDouble(fila,21)

                val issueExcel = Issue(
                    resumen, clave, id, padre, tipoIncidencia, estado, proyectoKey, proyecto,
                    responsable, responsableAccountId, estimacionOriginal, tiempoEmpleado, fechaEnProgreso,
                    fechaEnTerminado, responsableEmailAddress, registrarHorasDeTrabajoStarted,
                    registrarHorasTrabajotimeSpentSeconds, registrarHorasTrabajoauthorEmail,
                    tipoDeTrabajo, mes, llave, tiempoEmpleadoEnHoras
                )
                issue.add(issueExcel)
                contador++
            }

            println("jira excel: $contador")
            archivoExcel.close()

        } catch (e: Exception) {
            e.printStackTrace()
        }

        return issue
    }

    fun ConsolidarCSV() {
        val jira = leerIssueDeExcel()

        val workbook = XSSFWorkbook()
        val sheet = workbook.createSheet("DATA")

        generarCabecera(sheet)
        generarCuerpo(sheet, jira)

        try {
            FileOutputStream("D:\\PullRequest\\Jira.xlsx").use { outputStream ->
                workbook.write(outputStream)
            }
        } catch (e: Exception) {
          println("Error: $e")
        }
    }

    fun importarXLSConsolidado(): List<Issue> {
        val issueXLSX = mutableListOf<Issue>()

        try {
            val rutaDelArchivo = FileInputStream("D:\\PullRequest\\Jira.xlsx")
            val workbook = WorkbookFactory.create(rutaDelArchivo)
            val sheet = workbook.getSheetAt(0)

            for (rowNum in 1 until sheet.physicalNumberOfRows) {
                val row = sheet.getRow(rowNum)
                val nombreHistoria = row.getCell(1)?.stringCellValue ?: ""
                val estado = row.getCell(2)?.stringCellValue ?: ""
                val fechaTerminado = row.getCell(3)?.stringCellValue ?: ""
                val email = row.getCell(4)?.stringCellValue ?: ""
                val issue = Issue(
                    Resumen = "",
                    Clave = "",
                    Id = 0,
                    NombreHistoria = nombreHistoria,
                    TipoIncidencia = "",
                    Estado = estado,
                    ProyectoKey = "",
                    Proyecto = "",
                    Responsable = "",
                    ResponsableAccountId = "",
                    EstimacionOriginal = 0,
                    TiempoEmpleado = 0,
                    FechaEnProgreso = "",
                    FechaTerminado = fechaTerminado,
                    Email = email,
                    RegistrarHorasDeTrabajoStarted = 0,
                    RegistrarHorasTrabajotimeSpentSeconds = 0,
                    RegistrarHorasTrabajoauthorEmail = "",
                    TipoDeTrabajo = "",
                    Mes = "",
                    Llave = "",
                    TiempoEmpleadoEnHoras = 0.0
                )
                issueXLSX.add(issue)
            }

            workbook.close()
            rutaDelArchivo.close()

        } catch (e: Exception) {
            println("Error: $e")
        }

        return issueXLSX
    }

    fun generarCabecera(sheet: XSSFSheet) {
        val headerRow = sheet.createRow(0)
        headerRow.createCell(0).setCellValue("Nombre de Historia")
        headerRow.createCell(1).setCellValue("Estado")
        headerRow.createCell(2).setCellValue("Fecha Terminado")
        headerRow.createCell(3).setCellValue("Email")
    }

    fun generarCuerpo(sheet: XSSFSheet, Issues: List<Issue>) {
        var rowNum = 1

        for (issue in Issues ) {
            val row = sheet.createRow(rowNum++)
            row.createCell(0).setCellValue(issue.NombreHistoria)
            row.createCell(1).setCellValue(issue.Estado)
            row.createCell(2).setCellValue(issue.FechaTerminado)
            row.createCell(3).setCellValue(issue.Email)
        }
    }

    fun getValueString(fila: Row, posicion:Int): String {
        return fila.getCell(posicion)?.stringCellValue ?: ""
    }

    fun getValueDate(fila: Row, posicion:Int):Int{
        return fila.getCell(posicion)?.let {
            when (it.cellType) {
                CellType.NUMERIC -> it.numericCellValue.toInt()
                else -> 0
            }
        } ?: 0
    }
    fun getValueDateTime(fila: Row, posicion:Int):String{
        return fila.getCell(posicion)?.localDateTimeCellValue?.toString() ?: ""
    }

    fun getValueNumeric(fila: Row, posicion:Int):Int{
        return fila.getCell(posicion).numericCellValue.toInt()
    }

    fun getValueDouble(fila: Row, posicion:Int):Double{
        return fila.getCell(posicion)?.numericCellValue?: 0.0
    }

    fun getValueId(fila: Row, posicion:Int):Int{
        return  try {
            when (fila.getCell(posicion)?.cellType) {
                CellType.NUMERIC -> fila.getCell(2).numericCellValue.toInt()
                CellType.STRING -> fila.getCell(2).stringCellValue.toInt()
                else -> 0
            }
        } catch (e: Exception) {
            0
        }
    }
}