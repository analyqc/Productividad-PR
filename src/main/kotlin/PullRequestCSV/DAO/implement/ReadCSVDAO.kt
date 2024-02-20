package PullRequest.DAO.implement

import PullRequestCSV.DAO.CsvDAO
import PullRequestCSV.Model.CsvPullRequest
import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.xssf.usermodel.XSSFSheet
import org.apache.poi.xssf.usermodel.XSSFTable
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

open class ReadCSVDAO: CsvDAO {
    val functionRepository= FunctionsReadPR()
    val workbook = XSSFWorkbook(File("D://ReadPullRequest.xlsx"))

    val sheet: XSSFSheet = workbook.getSheetAt(0)
    val tables: List<XSSFTable> = sheet.tables

    val table = workbook.getTable("DATAANALISIS")
    private val iniTable = table.startRowIndex+1
    private val endTable = table.endRowIndex

    val positions: Map<String, Int> = mapOf(
        "Username" to (table.findColumnIndex("UserName")),
        "email" to (table.findColumnIndex("Email")),
        "repository" to (table.findColumnIndex("Repository")),
        "branch" to (table.findColumnIndex("Branch")),
        "userStory" to (table.findColumnIndex("User Story")),
        "PRNumber" to (table.findColumnIndex("PR Number")),
        "PRTitle" to (table.findColumnIndex("PR Title")),
        "PRState" to (table.findColumnIndex("PR State")),
        "PRCreated" to (table.findColumnIndex("PR Created")),
        "PRMergeDate" to (table.findColumnIndex("PR Merged")),
        "PRClosedDate" to (table.findColumnIndex("PR Closed")),
        "PRId" to (table.findColumnIndex("Id")),
        "PRReviewers" to (table.findColumnIndex("PR Reviewers"))
    )

    override fun getCsvPullRequest(): List<CsvPullRequest> {
        val issues = mutableListOf<CsvPullRequest>()

        for (i in iniTable..endTable) {
            val row = table.xssfSheet.getRow(i)
            val username=functionRepository.getUserName(row, positions["Username"]!!)
            val email = functionRepository.getEmail(row, positions["email"]!!)
            val repository = functionRepository.getRepository(row, positions["repository"]!!)
            val branch = functionRepository.getBranch(row, positions["branch"]!!)
            val userStory = functionRepository.getUserStory(row, positions["userStory"]!!)
            val PRNumber = functionRepository.getPRNumber(row, positions["PRNumber"]!!)
            val PRTitle = functionRepository.getPRTitle(row, positions["PRTitle"]!!)
            val PRState = functionRepository.getPRState(row, positions["PRState"]!!)
            val PRCreated = functionRepository.getPRCreated(row, positions["PRCreated"]!!)
            val PRReviewers = functionRepository.getPRReviewers(row, positions["PRReviewers"]!!)
            val PRId = functionRepository.getPRId(row, positions["PRId"]!!)

            var fechaMerge = ""
            val PRMergeDate = row.getCell(positions["PRMergeDate"]!!)

            if (PRMergeDate != null) {
                fechaMerge = when (PRMergeDate.cellType) {
                    CellType.NUMERIC -> {
                        val localDateTime = PRMergeDate.localDateTimeCellValue
                        localDateTime.format(DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss"))
                    }
                    CellType.STRING -> {
                        val fechaMergeString = PRMergeDate.stringCellValue.trim()
                        if (fechaMergeString.isNotEmpty() && fechaMergeString.equals("closed", ignoreCase = true)) {
                            try {
                                val localDateTime =
                                    LocalDateTime.parse(fechaMergeString, DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss"))
                                localDateTime.format(DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss"))
                            } catch (e: DateTimeParseException) {
                                ""
                            }
                        } else {
                            ""
                        }
                    }
                    else -> ""
                }
            }
            var fechaClosedDate = ""
            val PRClosedDate = row.getCell(positions["PRClosedDate"]!!)

            if (PRClosedDate != null) {
                fechaClosedDate = when (PRClosedDate.cellType) {
                    CellType.NUMERIC -> {
                        val localDateTime = PRClosedDate.localDateTimeCellValue
                        localDateTime.format(DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss"))
                    }
                    CellType.STRING -> {
                        val fechaClosedDateString = PRClosedDate.stringCellValue.trim().toLowerCase()
                        if (!fechaClosedDateString.equals("closed", ignoreCase = true)) {
                            try {
                                val localDateTime = LocalDateTime.parse(
                                    fechaClosedDateString,
                                    DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss")
                                )
                                localDateTime.format(DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss"))
                            } catch (e: DateTimeParseException) {
                               "Error"
                            }
                        } else {
                            ""
                        }
                    }
                    else -> ""
                }
            }

            val CsvPullRequestT = CsvPullRequest(username,email,repository,branch,userStory,PRNumber,PRTitle,PRState,PRCreated,fechaMerge,fechaClosedDate,PRId,PRReviewers)
            issues.add(CsvPullRequestT)
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