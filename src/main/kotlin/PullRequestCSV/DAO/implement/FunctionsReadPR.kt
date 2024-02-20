package PullRequest.DAO.implement

import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.xssf.usermodel.XSSFRow
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
class FunctionsReadPR {

    fun getUserName(
        row: XSSFRow,
        position: Int
    ): String = row.getCell(position).stringCellValue
    fun getEmail(
        row: XSSFRow,
        position: Int
    ): String = row.getCell(position).stringCellValue

    fun getRepository(
        row: XSSFRow,
        position: Int
    ): String = row.getCell(position).stringCellValue

    fun getBranch(
        row: XSSFRow,
        position: Int
    ): String = row.getCell(position).stringCellValue

    fun getUserStory(
        row: XSSFRow,
        position: Int
    ): String{
        val cell = row.getCell(position)
        return if (cell == null || cell.stringCellValue.isNullOrBlank()) {
            "VACIO"
        } else {
            cell.stringCellValue
        }
    }

    fun getPRNumber(
        row: XSSFRow,
        position: Int
    ): Int = row.getCell(position).numericCellValue.toInt()

    fun getPRTitle(
        row: XSSFRow,
        position: Int
    ): String = row.getCell(position).stringCellValue
    fun getPRState(
        row: XSSFRow,
        position: Int
    ): String = row.getCell(position).stringCellValue
    fun getPRCreated(
        row: XSSFRow,
        position: Int
    ): String = row.getCell(position).stringCellValue


    fun getPRId(row: XSSFRow?, columnIndex: Int): Int {
        if (row == null) {
            return 0
        }

        val cell = row.getCell(columnIndex)
        return try {
            if (cell.cellType == CellType.NUMERIC) {
                cell.numericCellValue.toInt()
            } else {
                0
            }
        } catch (e: IllegalStateException) {
            0
        }
    }


    fun getPRReviewers(row: XSSFRow?, columnIndex: Int): Int {
        if (row == null) {
            return 0
        }
        val cell = row.getCell(columnIndex)
        return try {
            if (cell.cellType == CellType.NUMERIC) {
                cell.numericCellValue.toInt()
            } else {
                0
            }
        } catch (e: IllegalStateException) {
            0
        }
    }

}