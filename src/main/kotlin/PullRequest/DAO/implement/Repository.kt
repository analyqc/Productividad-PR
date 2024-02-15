package PullRequest.DAO.implement

import PullRequest.DAO.RepositoryDAO
import PullRequest.Model.RepositoryGIT
import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.xssf.usermodel.XSSFSheet
import org.apache.poi.xssf.usermodel.XSSFTable
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

open class Repository: RepositoryDAO {

    val functionRepository=FunctionsRepository()
    val workbook = XSSFWorkbook(File("D://PullRequest_271123.0139.xlsx"))

    val sheet: XSSFSheet = workbook.getSheetAt(0)
    val tables: List<XSSFTable> = sheet.tables

    val table = workbook.getTable("GHReportes")
    val iniTable = table.startRowIndex + 1
    val endTable = table.endRowIndex

    val positions: Map<String, Int> = mapOf(
        "email" to (table.findColumnIndex("Email")),
        "repository" to (table.findColumnIndex("Repository")),
        "branch" to (table.findColumnIndex("Branch")),
        "userStory" to (table.findColumnIndex("User Story")),
        "PRNumber" to (table.findColumnIndex("PR Number")),
        "PRMergeDate" to (table.findColumnIndex("PR Merged")),
        "PRClosedDate" to (table.findColumnIndex("PR Closed")),
        "PRId" to (table.findColumnIndex("Id"))
    )

    override fun getRepository(): List<RepositoryGIT> {
        val issues = mutableListOf<RepositoryGIT>()

        for (i in iniTable..endTable) {
            val row = table.xssfSheet.getRow(i)

            val email = functionRepository.getEmail(row, positions["email"]!!)
            val repository = functionRepository.getRepository(row, positions["repository"]!!)
            val branch = functionRepository.getBranch(row, positions["branch"]!!)
            val userStory = functionRepository.getUserStory(row, positions["userStory"]!!)
            val PRNumber = functionRepository.getPRNumber(row, positions["PRNumber"]!!)
            val PRId = functionRepository.getPRId(row, positions["PRId"]!!)

            var fechaMerge = ""
            val PRMergeDate = row.getCell(positions["PRMergeDate"]!!)

            if (PRMergeDate != null) {
                fechaMerge = when (PRMergeDate.cellType) {
                    CellType.NUMERIC -> {
                        val localDateTime = PRMergeDate.localDateTimeCellValue
                        functionRepository.formatLocalDateTime(localDateTime)
                    }
                    CellType.STRING -> {
                        val fechaMergeString = PRMergeDate.stringCellValue
                        if (fechaMergeString.isNotEmpty()) {
                            val localDateTime = LocalDateTime.parse(fechaMergeString, DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss"))
                            functionRepository.formatLocalDateTime(localDateTime)
                        } else {
                            ""
                        }
                    }
                    else -> ""
                }
            }

            var fechaClosedDate = ""
            val PRClosedDate = row.getCell(positions["PRMergeDate"]!!)

            if (PRClosedDate != null) {
                fechaClosedDate = when (PRClosedDate.cellType) {
                    CellType.NUMERIC -> {
                        val localDateTime = PRClosedDate.localDateTimeCellValue
                        functionRepository.formatLocalDateTime(localDateTime)
                    }
                    else -> ""
                }
            }


            val repositoryGIT = RepositoryGIT(email,repository,branch,userStory,PRNumber,fechaMerge,fechaClosedDate,PRId)
            issues.add(repositoryGIT)
        }
        return issues
    }

    fun prueba() {
        val headerRow = sheet.getRow(0)
        val totalColumns = headerRow.physicalNumberOfCells
        val columnNames = mutableListOf<String>()
        for (colIndex in 0 until totalColumns) {
            val cell = headerRow.getCell(colIndex)
            val columnName = cell.stringCellValue
            columnNames.add(columnName)
        }

        println("Nombres de las columnas:")
        columnNames.forEach { println(it) }

        for (t in tables) {
            println("Nombre: "+ t.displayName)
            println("Tabla: "+ t.name)
        }
    }

}