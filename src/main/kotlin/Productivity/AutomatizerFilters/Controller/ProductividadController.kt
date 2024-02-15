package AutomatizerFilters.Controller

import AutomatizerFilters.Model.Issue
import Productivity.AutomatizerFilters.DAO.JiraDAO

class ProductividadController(val jiraDAO: JiraDAO){
    fun productividadExcel():String {

        val issues = jiraDAO.getIssues()
        val issuesPeriodo = getIssuesDelPeriodo(issues)

        return exportarIssuesDelPeriodo(issuesPeriodo)
    }

    private fun getIssuesDelPeriodo(issues: List<Issue>): List<Issue> {
        val issuesFiltrado = issues.filter {
            it.tipoincidencia.equals("Historia", ignoreCase = true) &&
            it.estado.equals("TERMINADO", ignoreCase = true) &&
            it.fechaTerminado.matches("\\d{2}/06/2023".toRegex())
        }
        return issuesFiltrado
    }

   private fun exportarIssuesDelPeriodo (issuesFiltrado: List<Issue>): String {
        var issuesDelPeriodo=""
        for (issue in issuesFiltrado) {
            issuesDelPeriodo+="${issue.clave} - ${issue.tipoincidencia} - ${issue.estado} - ${issue.fechaTerminado}"
        }
        return issuesDelPeriodo
    }
}
