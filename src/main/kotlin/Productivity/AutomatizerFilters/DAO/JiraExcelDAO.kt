package AutomatizerFilters.DAO

import AutomatizerFilters.Model.Issue
import Productivity.AutomatizerFilters.DAO.JiraDAO
import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.xssf.usermodel.XSSFRow
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

open class JiraExcelDAO: JiraDAO {

    private var prop = Properties()
    private var projectPath = System.getProperty("user.dir")

    val filepath = getProperties("ruta_excel")
    val workbook = XSSFWorkbook(File(filepath))
    val tableName = getProperties("tableName")
    val table = workbook.getTable(tableName) ?: throw IllegalArgumentException("table: $tableName")
    val iniTable = table.startRowIndex + 1
    val endTable = table.endRowIndex

    val positions: Map<String, Int> = mapOf(
        "idClave" to (table.findColumnIndex("Clave")),
        "idTipoIncidencia" to (table.findColumnIndex("Tipo de Incidencia")),
        "idEstado" to (table.findColumnIndex("Estado")),
        "idFecha" to (table.findColumnIndex("Fecha en TERMINADO"))
    )

    //TODO CONSUMO DE INTERFAZ
    override fun getIssues(): List<Issue> {
        val issues = mutableListOf<Issue>()

        for (i in iniTable..endTable) {
            val row = table.xssfSheet.getRow(i)

            val clave = getClave(row, positions["idClave"]!!)
            val tipoincidencia = getTipoIncidencia(row, positions["idTipoIncidencia"]!!)
            val estado = getEstado(row, positions["idEstado"]!!)

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

            val issue = Issue(clave, tipoincidencia, estado, fechaTerminado)
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

    fun formatLocalDateTime(localDateTime: LocalDateTime): String {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        return localDateTime.format(formatter)
    }
}