package IssueXLSX.DAO.implement

import IssueXLSX.DAO.IssueDAO
import IssueXLSX.Model.Issue
import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.WorkbookFactory
import org.apache.poi.xssf.usermodel.XSSFSheet
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.FileInputStream
import java.io.FileOutputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

open class IssueXLSXDAO : IssueDAO {

    override fun getIssue(): List<Issue> {
        ConsolidarCSV()
        return importarXLSConsolidado()
    }

    fun leerIssueDeExcel(): List<Issue> {
        val issue = mutableListOf<Issue>()
        val filePath = "D:\\BBDDJira.xlsx"

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
            }
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
        generarCuerpo(sheet,jira)

        try {
            FileOutputStream("D:\\PullRequestIssue\\Issue.xlsx").use { outputStream ->
                workbook.write(outputStream)
            }
        } catch (e: Exception) {
            println("Error: $e")
        }
    }


    fun importarXLSConsolidado(): List<Issue> {
        val jiraXLS = mutableListOf<Issue>()

        try {
            val rutaDelArchivo = FileInputStream("D:\\PullRequestIssue\\Issue.xlsx")
            val workbook = WorkbookFactory.create(rutaDelArchivo)
            val sheet = workbook.getSheetAt(0)

            leerColumnas(sheet,jiraXLS)

            workbook.close()
            rutaDelArchivo.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return jiraXLS
    }

    fun generarCabecera(sheet: XSSFSheet) {
        val headerRow = sheet.createRow(0)
        headerRow.createCell(0).setCellValue("Nombre de Historia")
        headerRow.createCell(1).setCellValue("Estado")
        headerRow.createCell(2).setCellValue("Fecha Terminado")
    }

    fun generarCuerpo(sheet: XSSFSheet, Issues: List<Issue>) {
        val nombresHistorias = HashSet<String>()

        for (issue in Issues) {
            //TODO 1. Eliminar correo de Excel de Jira.xlsx (Que ya no lo exporte)
            //TODO 2. Eliminar duplicados por nombres de historia antes de exportar el excel de Jira.xlsx
            //TODO 4. antes de exportar el excel filtrar las historias de usuario que hayan terminado en Enero Jira.xlsx
            if (!nombresHistorias.contains(issue.NombreHistoria) && fechaEnero(issue.FechaTerminado)) {
                val row = sheet.createRow(sheet.physicalNumberOfRows)
                row.createCell(0).setCellValue(issue.NombreHistoria)
                row.createCell(1).setCellValue(issue.Estado)
                row.createCell(2).setCellValue(formatearFecha(issue.FechaTerminado))
                nombresHistorias.add(issue.NombreHistoria)
            }
        }
    }

    fun leerColumnas(sheet: Sheet, jiraXLS: MutableList<Issue> ){
        for (rowNum in 1 until sheet.physicalNumberOfRows) {
            val row = sheet.getRow(rowNum)
            val nombreHistoria = row.getCell(0)?.stringCellValue ?: ""
            val estado = row.getCell(1)?.stringCellValue ?: ""
            val fechaTerminado = row.getCell(2)?.stringCellValue ?: ""
            val email = row.getCell(3)?.stringCellValue ?: ""
            val csvJira = Issue(
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
            jiraXLS.add(csvJira)

        }
    }

    fun formatearFecha(fecha: String): String {
        if (fecha.isBlank()) {
            return ""
        }
        val formatoEntrada = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")
        val formatoSalida = DateTimeFormatter.ofPattern("dd-MM-yyyy")

        val fechaLocal = LocalDateTime.parse(fecha, formatoEntrada)
        return fechaLocal.format(formatoSalida)
    }

    fun fechaEnero(fecha: String): Boolean {
        if (fecha.isBlank()) {
            return false
        }
        val formatoEntrada = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")
        val fechaLocal = LocalDateTime.parse(fecha, formatoEntrada)
        return fechaLocal.monthValue == 1
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