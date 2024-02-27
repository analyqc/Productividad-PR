package PullRequest.DAO.implement

import PullRequestCSV.DAO.PullRequestDAO
import PullRequestCSV.Model.PullRequests
import org.apache.poi.ss.usermodel.WorkbookFactory
import org.apache.poi.xssf.usermodel.XSSFSheet
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.time.LocalDate
import java.time.format.DateTimeFormatter

open class PullRequestCSVDAO: PullRequestDAO {

    override fun getPullRequests(): List<PullRequests> {
        ConsolidarCSV()
        return importarXLSConsolidado()
    }

    fun leerPullRequestDeCSV(): List<PullRequests> {
        val rutasDePullRequestsCSV = listOf(
            "D:\\Documentos\\PullRequest_010124.0526.csv",
            "D:\\Documentos\\PullRequest_050224.0802.csv",
            "D:\\Documentos\\PullRequest_080124.0621.csv",
            "D:\\Documentos\\PullRequest_120224.0907.csv",
            "D:\\Documentos\\PullRequest_150124.0620.csv",
            "D:\\Documentos\\PullRequest_190224.0634.csv",
            "D:\\Documentos\\PullRequest_220124.0754.csv",
            "D:\\Documentos\\PullRequest_290124.0821.csv"
        )

        val pullrequests = ArrayList<PullRequests>()

        for (rutaDePullRequest in rutasDePullRequestsCSV) {
            val archivoCSV = File(rutaDePullRequest)

            val fechaArchivo =convertirOrigenAFecha(archivoCSV)

            archivoCSV.bufferedReader().useLines { lines ->
                lines.drop(1)
                    .forEach { line ->
                        val tokens = line.split(",")
                        val pullRequests = PullRequests(
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
                            PRReviewers = tokens[12].toIntOrNull() ?: 0,
                            FechaDeArchivo = fechaArchivo
                        )
                        pullrequests.add(pullRequests)
                    }
            }
        }
        return pullrequests
    }
    fun ConsolidarCSV() {

        val pullrequests = leerPullRequestDeCSV()
        val recientePullRequest = mutableMapOf<Int, PullRequests>()

        compareTo(pullrequests,recientePullRequest)

        val latestPullRequests = recientePullRequest.values.sortedBy {it.Id}
        val workbook = XSSFWorkbook()
        val sheet = workbook.createSheet("DATA")

        generarCabecera(sheet)
        generarCuerpo(sheet,latestPullRequests)

        FileOutputStream("D:\\PullRequest\\ReadPullRequest.xlsx").use { outputStream ->
            workbook.write(outputStream)
        }
    }

    fun importarXLSConsolidado(): List<PullRequests> {
        val filePath="D:\\PullRequest\\ReadPullRequest.xlsx"

        val pullrequest = mutableListOf<PullRequests>()

        val excelFile = FileInputStream(filePath)
        val workbook = WorkbookFactory.create(excelFile)
        val sheet = workbook.getSheetAt(0)

        for (rowNum in 1 until sheet.physicalNumberOfRows) {
            val row = sheet.getRow(rowNum)
            val pullRequests = PullRequests(
                Username = row.getCell(0).stringCellValue,
                Email = row.getCell(1).stringCellValue,
                Repository = row.getCell(2).stringCellValue,
                Branch = row.getCell(3).stringCellValue,
                UserStory = row.getCell(4).stringCellValue,
                PRNumber = row.getCell(5).numericCellValue.toInt(),
                PRTitle = row.getCell(6).stringCellValue,
                PRState = row.getCell(7).stringCellValue,
                PRCreated = row.getCell(8).stringCellValue,
                PRMerged = row.getCell(9).stringCellValue,
                PRClosed = row.getCell(10).stringCellValue,
                Id = row.getCell(11).numericCellValue.toInt(),
                PRReviewers = row.getCell(12).numericCellValue.toInt(),
                FechaDeArchivo = row.getCell(13).stringCellValue,
            )
            pullrequest.add(pullRequests)
        }

        workbook.close()
        excelFile.close()

        return pullrequest
    }

   fun compareTo(pullrequests: List<PullRequests>,  recientePullRequest: MutableMap<Int, PullRequests>){
       for (pullrequest in pullrequests) {
           val ordenarPullRequestId = recientePullRequest[pullrequest.Id]
           if (ordenarPullRequestId == null || LocalDate.parse(pullrequest.FechaDeArchivo, DateTimeFormatter.ofPattern("dd/MM/yyyy"))
               > LocalDate.parse(ordenarPullRequestId.FechaDeArchivo, DateTimeFormatter.ofPattern("dd/MM/yyyy"))) {
               recientePullRequest[pullrequest.Id] = pullrequest
           }
       }
   }

    private fun convertirOrigenAFecha(archivoCSV: File): String {
        val nombreArchivo = archivoCSV.nameWithoutExtension
        val numeroArchivo = nombreArchivo.substringAfterLast("_").substringBeforeLast(".").toIntOrNull()
        val dateString = numeroArchivo.toString().padStart(6, '0')
        val formatter = DateTimeFormatter.ofPattern("ddMMyy")
        val date = LocalDate.parse(dateString, formatter)
        return date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
    }

    fun generarCabecera( sheet: XSSFSheet){
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
        headerRow.createCell(13).setCellValue("Origen")
    }

    fun generarCuerpo(sheet: XSSFSheet, latestPullRequests: List<PullRequests>){
        var rowNum = 1
        for (pullrequest in latestPullRequests) {
            val row = sheet.createRow(rowNum++)
            row.createCell(0).setCellValue(pullrequest.Username)
            row.createCell(1).setCellValue(pullrequest.Email)
            row.createCell(2).setCellValue(pullrequest.Repository)
            row.createCell(3).setCellValue(pullrequest.Branch)
            row.createCell(4).setCellValue(pullrequest.UserStory)
            row.createCell(5).setCellValue(pullrequest.PRNumber.toDouble())
            row.createCell(6).setCellValue(pullrequest.PRTitle)
            row.createCell(7).setCellValue(pullrequest.PRState)
            row.createCell(8).setCellValue(pullrequest.PRCreated)
            row.createCell(9).setCellValue(pullrequest.PRMerged)
            row.createCell(10).setCellValue(pullrequest.PRClosed)
            row.createCell(11).setCellValue(pullrequest.Id.toDouble())
            row.createCell(12).setCellValue(pullrequest.PRReviewers.toDouble())
            row.createCell(13).setCellValue(pullrequest.FechaDeArchivo)
        }
    }
}
