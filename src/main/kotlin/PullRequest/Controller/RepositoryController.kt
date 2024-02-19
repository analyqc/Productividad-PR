package PullRequest.Controller

import Productivity.AutomatizerFilters.Model.IssueExcel
import PullRequest.DAO.implement.Repository
import PullRequest.Model.RepositoryExcel
import PullRequest.Model.RepositoryGIT

class RepositoryController(private val repositoryDAO: Repository) {

    private val excelGeneratorPR = ExcelGeneratorPR()


    fun repositoryExcel(rutaArchivo: String) {
        val repository = repositoryDAO.getRepository()
        val filtradoPR= exportarFiltradosDelGitHub(repository)
        excelGeneratorPR.generarExcel(filtradoPR, rutaArchivo)
    }


    private fun exportarFiltradosDelGitHub(repositoryFiltrado: List<RepositoryGIT>): List<RepositoryExcel> {
        val repFiltrado = repositoryFiltrado.filter {
            it.branch.equals("develop", ignoreCase = true)
        }

        val emailCountMap = mutableMapOf<String, MutableMap<String, Int>>()

        for (issue in repFiltrado) {
            emailCountMap
                .computeIfAbsent(issue.email) { mutableMapOf() }
                .merge(issue.UserStory, 1, Int::plus)
        }

        val respositoryExcel = mutableListOf<RepositoryExcel>()

        val filtradoGitHub = StringBuilder()

        for ((email, userStoryCountMap) in emailCountMap) {
            for ((userStory, count) in userStoryCountMap) {
                val formattedOutput = String.format("%-40s %-20s %d", email, userStory, count)
                filtradoGitHub.append("$formattedOutput\n")
                respositoryExcel.add(RepositoryExcel(email, userStory, count))
            }
        }

        return respositoryExcel
    }
}
