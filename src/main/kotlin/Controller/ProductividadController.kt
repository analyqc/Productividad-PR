package Controller

import IssueXLSX.DAO.implement.IssueXLSXDAO
import IssueXLSX.Model.Issue

class ProductividadController( private val jiraDAO : IssueXLSXDAO) {
    fun exportMetricas(): List<Issue> {
        //TODO 1 exportar consolidado PullRequest
        //private val pullRequestDAO: PullRequestDAO,
        //val consolidadoPullRequest=pullRequestDAO.getPullRequests()

        //TODO 2 exportar consolidado JIRA

        val consolidadoJira=jiraDAO.getIssue()

        //TODO 3 Filtrar PR (PullRequest) E Issues (JIRA)
        //TODO 4 Combinar listado de PR e Issues
        //TODO 5 Calcular métrica PR/HU
        //TODO 6 Calcular métrica Revisiones/PR
        //TODO 7 Exportar métricas

        return consolidadoJira
    }
}
