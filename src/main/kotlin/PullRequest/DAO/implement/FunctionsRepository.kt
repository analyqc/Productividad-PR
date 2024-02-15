package PullRequest.DAO.implement

import org.apache.poi.xssf.usermodel.XSSFRow
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
class FunctionsRepository {

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
        var user=""
        if(row.getCell(position)==null){
            user=""
        }else{
            user=row.getCell(position).stringCellValue
        }
        return user
    }

    fun getPRNumber(
        row: XSSFRow,
        position: Int
    ): Int = row.getCell(position).numericCellValue.toInt()

    fun formatLocalDateTime(localDateTime: LocalDateTime?): String {
        return localDateTime?.let {
            val formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy")
            it.format(formatter)
        } ?: ""
    }

    fun getPRId(
        row: XSSFRow,
        position: Int
    ):Int = row.getCell(position).numericCellValue.toInt()

}