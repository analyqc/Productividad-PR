package Controller

import Jira.DAO.implement.IssueXLSXDAO

fun main() {
//    val pullrequestDAO = PullRequestCSVDAO()
////    val jiraDAO = JiraCSVDAO()
//    val controller = ProductividadController(pullrequestDAO)

    val jiraDAO = IssueXLSXDAO()
    val controller = ProductividadController(jiraDAO)
    controller.exportMetricas()
}