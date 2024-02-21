package PullRequestCSV.Controller

import PullRequestCSV.DAO.PullRequestDAO
import PullRequestCSV.Model.CsvPullRequest

class ProductividadController(private val pullRequestDAO: PullRequestDAO) {
    fun exportMetricas(): String {
        //TODO 1 exportar consolidado PullRequest
        val pullRequests = pullRequestDAO.getPullRequests()

        //TODO 2 exportar consolidado JIRA
        //TODO 3 Filtrar PR (PullRequest) E Issues (JIRA)
        //TODO 4 Combinar listado de PR e Issues
        //TODO 5 Calcular métrica PR/HU
        //TODO 6 Calcular métrica Revisiones/PR
        //TODO 7 Exportar métricas
        return exportarFiltradosDelGitHub(pullRequests)
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
}
