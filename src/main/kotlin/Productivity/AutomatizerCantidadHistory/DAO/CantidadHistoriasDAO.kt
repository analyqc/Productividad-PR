package Productivity.AutomatizerCantidadHistory.DAO

import Productivity.AutomatizerCantidadHistory.Model.IssueCantidad
import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.xssf.usermodel.XSSFRow
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class CantidadHistoriasDAO {

    private var prop = Properties()
    private var projectPath = System.getProperty("user.dir")

    val filepath = getProperties("ruta_excel")
    val workbook = XSSFWorkbook(File(filepath))
    val tableName = getProperties("tableName")
    val table = workbook.getTable(tableName) ?: throw IllegalArgumentException("table")
    val iniTable = table.startRowIndex + 1
    val endTable = table.endRowIndex

    val positions: Map<String, Int> = mapOf(
        "idClave" to (table.findColumnIndex("Clave")),
        "idTipoIncidencia" to (table.findColumnIndex("Tipo de Incidencia")),
        "idEstado" to (table.findColumnIndex("Estado")),
        "idEmail" to (table.findColumnIndex("Responsable.emailAddress")),
        "idFecha" to (table.findColumnIndex("Fecha en TERMINADO"))
    )


    fun countRepetitions(issues: List<IssueCantidad>, filtros: (IssueCantidad) -> Boolean): Map<String, Int> {
        val correoTipoMap: MutableMap<String, MutableMap<String, Int>> = mutableMapOf()

        for (issue in issues.filter(filtros)) {
            val key = "${issue.mail} - ${issue.tipoincidencia}"
            correoTipoMap.computeIfAbsent(key) { mutableMapOf() }
            correoTipoMap[key]?.compute("count") { _, count -> (count ?: 0) + 1 }
        }

        val repetitionsMap: MutableMap<String, Int> = mutableMapOf()
        correoTipoMap.forEach { (key, value) ->
            val count = value["count"] ?: 0
            repetitionsMap[key] = count
        }

        return repetitionsMap
    }

    fun getIssues(): List<IssueCantidad> {
        val issues = mutableListOf<IssueCantidad>()

        for (i in iniTable..endTable) {
            val row = table.xssfSheet.getRow(i)

            val clave = getClave(row, positions["idClave"]!!)
            val tipoincidencia = getTipoIncidencia(row, positions["idTipoIncidencia"]!!)
            val estado = getEstado(row, positions["idEstado"]!!)
            val mail = getMail(row,positions["idEmail"]!!).toString()
            val fechaCell = row.getCell(positions["idFecha"]!!)
            var fechaTerminado = ""

            if (fechaCell != null) {
                fechaTerminado = when (fechaCell.cellType) {
                    CellType.NUMERIC -> {
                        val localDateTime = fechaCell.localDateTimeCellValue
                        formatLocalDateTime(localDateTime)
                    }
                    else -> ""
                }
            }

            if (clave.isNullOrEmpty()) {
                throw IllegalArgumentException("clave")
            }
            if (tipoincidencia.isNullOrEmpty()) {
                throw IllegalArgumentException("tipoincidencia")
            }
            if (estado.isNullOrEmpty()) {
                throw IllegalArgumentException("estado")
            }
            if (mail.isNullOrEmpty()) {
                throw IllegalArgumentException("mail")
            }

            val issue = IssueCantidad(clave, tipoincidencia, estado, fechaTerminado,mail)
            issues.add(issue)
        }

        return issues
    }


    fun getProperties(property: String): String{
        val input: InputStream = FileInputStream("$projectPath\\src\\main\\kotlin\\Productivity\\util\\config.properties")
        prop.load(input)
        return prop.getProperty(property)
    }

    fun getClave(
        row: XSSFRow,
        position: Int
    ): String? = row.getCell(position).stringCellValue

    fun getTipoIncidencia(
        row: XSSFRow,
        position: Int
    ): String? = row.getCell(position).stringCellValue

    fun getEstado(
        row: XSSFRow,
        position: Int
    ): String? = row.getCell(position).stringCellValue


    fun getMail(
        row: XSSFRow,
        position: Int
    ): String? = row.getCell(position)?.stringCellValue


    fun formatLocalDateTime(localDateTime: LocalDateTime): String {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        return localDateTime.format(formatter)
    }
}