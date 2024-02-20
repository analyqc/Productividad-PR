package PullRequestCSV.DAO.Read

import PullRequestCSV.Model.CsvPullRequest
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileOutputStream

class ReadCSV {
    fun readCsvFile(filePath: String): List<CsvPullRequest>{
        val dataList = mutableListOf<CsvPullRequest>()
        val csvFile = File(filePath)

        if (!csvFile.exists()) {
            return emptyList()
        }
        csvFile.bufferedReader().useLines { lines ->
            lines.drop(1)
                .forEach { line ->
                    val tokens = line.split(",")
                    val csvPullRequest = CsvPullRequest(
                        Username = tokens[0],
                        Email = tokens[1],
                        Repository = tokens[2],
                        Branch = tokens[3],
                        UserStory = tokens[4],
                        PRNumber = tokens[5].toIntOrNull() ?: 0,
                        PRTitle = tokens[6],
                        PRState = tokens[7],
                        PRCreated = tokens[8],
                        PRMerged = tokens[9],
                        PRClosed = tokens[10],
                        Id = tokens[11].toIntOrNull() ?: 0,
                        PRReviewers = tokens[12].toIntOrNull() ?: 0
                    )
                    dataList.add(csvPullRequest)
                }
        }

        return dataList
    }

    fun writeExcel(dataList: List<CsvPullRequest>, outputFile: File) {
        val workbook = XSSFWorkbook()
        val sheet = workbook.createSheet("Data")

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

        var rowNum = 1
        for (csvData in dataList) {
            val row = sheet.createRow(rowNum++)
            row.createCell(0).setCellValue(csvData.Username)
            row.createCell(1).setCellValue(csvData.Email)
            row.createCell(2).setCellValue(csvData.Repository)
            row.createCell(3).setCellValue(csvData.Branch)
            row.createCell(4).setCellValue(csvData.UserStory ?: "")
            row.createCell(5).setCellValue((csvData.PRNumber).toDouble())
            row.createCell(6).setCellValue(csvData.PRTitle)
            row.createCell(7).setCellValue(csvData.PRState)
            row.createCell(8).setCellValue(csvData.PRCreated)
            row.createCell(9).setCellValue(csvData.PRMerged)
            row.createCell(10).setCellValue(csvData.PRClosed)
            row.createCell(11).setCellValue((csvData.Id).toDouble())
            row.createCell(12).setCellValue((csvData.PRReviewers).toDouble())
        }

        FileOutputStream(outputFile).use { outputStream ->
            workbook.write(outputStream)
        }
    }

}