package PullRequestCSV

import PullRequestCSV.DAO.Read.ReadCSV
import PullRequestCSV.Model.CsvPullRequest
import java.io.File

fun main() {
    val readCsvFile = ReadCSV()
    val inputFiles = listOf("D:\\Documentos\\PullRequest_010124.0526.csv",
        "D:\\Documentos\\PullRequest_050224.0802.csv",
        "D:\\Documentos\\PullRequest_080124.0621.csv",
        "D:\\Documentos\\PullRequest_120224.0907.csv",
        "D:\\Documentos\\PullRequest_150124.0620.csv",
        "D:\\Documentos\\PullRequest_190224.0634.csv",
        "D:\\Documentos\\PullRequest_220124.0754.csv",
        "D:\\Documentos\\PullRequest_290124.0821.csv",
        )
    val outputFile = "D:\\ReadPullRequest.xlsx"

    val dataList = mutableListOf<CsvPullRequest>()
    inputFiles.forEach { filePath ->
        dataList.addAll(readCsvFile.readCsvFile(filePath))
    }

    readCsvFile.writeExcel(dataList, File(outputFile))
}