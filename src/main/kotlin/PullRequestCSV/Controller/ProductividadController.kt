package PullRequestCSV.Controller

import PullRequestCSV.DAO.PullRequestDAO
import PullRequestCSV.Model.CsvPullRequest

class ProductividadController(private val pullRequestDAO: PullRequestDAO) {
    fun exportMetricas(): List<CsvPullRequest> {
        //TODO 1 exportar consolidado PullRequest
        val consolidadoPullRequest=pullRequestDAO.getPullRequests()


        //TODO 2 exportar consolidado JIRA
//        , private val jiraDAO : JiraDAO
//        val consolidadoJira=jiraDAO.getJira()

        //TODO 3 Filtrar PR (PullRequest) E Issues (JIRA)
        //TODO 4 Combinar listado de PR e Issues
        //TODO 5 Calcular métrica PR/HU
        //TODO 6 Calcular métrica Revisiones/PR
        //TODO 7 Exportar métricas

        return consolidadoPullRequest
    }
}
