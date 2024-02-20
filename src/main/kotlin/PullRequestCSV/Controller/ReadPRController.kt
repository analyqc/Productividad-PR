package PullRequestCSV.Controller

import PullRequestCSV.DAO.CsvDAO
import PullRequestCSV.Model.CsvPullRequest

class ReadPRController(private val csvDAO: CsvDAO) {

    fun repositoryExcel(): String {
        val repository = csvDAO.getCsvPullRequest()
        return exportarFiltradosDelGitHub(repository)
    }

    private fun exportarFiltradosDelGitHub(repositoryFiltrado: List<CsvPullRequest>): String {
        val filtradoGitHub = StringBuilder()
        var lineNumber = 1
        for (u in repositoryFiltrado) {
            val formattedOutput = String.format(
                "%d. %-15s %-15s %-15s %-15s %-15s %-15d",
                lineNumber, u.Username, u.Email, u.Repository, u.Branch, u.UserStory, u.PRNumber
            )
            filtradoGitHub.append("$formattedOutput\n")
            lineNumber++
        }

        return filtradoGitHub.toString()
    }

//    fun repositoryExcel(rutaArchivo: String) {
//        val repository = csvDAO.getCsvPullRequest()
//        val filtradoPR= exportarFiltradosDelGitHub(repository)
//        excelGeneratorPR.generarExcel(filtradoPR, rutaArchivo)
//    }
//
//    private fun exportarFiltradosDelGitHub(repositoryFiltrado: List<CsvPullRequest>): String{
//        val repFiltrado = repositoryFiltrado.filter {
//            it.branch.equals("develop", ignoreCase = true)
//        }
//
//        val emailCountMap = mutableMapOf<String, MutableMap<String, Int>>()
//
//        for (issue in repFiltrado) {
//            emailCountMap
//                .computeIfAbsent(issue.email) { mutableMapOf() }
//                .merge(issue.UserStory, 1, Int::plus)
//        }
//
//        val respositoryExcel = mutableListOf<RepositoryExcel>()
//
//        val filtradoGitHub = StringBuilder()
//
//        for ((email, userStoryCountMap) in emailCountMap) {
//            for ((userStory, count) in userStoryCountMap) {
//                val formattedOutput = String.format("%-40s %-20s %d", email, userStory, count)
//                filtradoGitHub.append("$formattedOutput\n")
//                respositoryExcel.add(RepositoryExcel(email, userStory, count))
//            }
//        }
//
//        return filtradoGitHub
//    }
}
