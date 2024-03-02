package RequerimientoPRJira

import RequerimientoPRJira.Controller.ProductividadController
import IssueXLSX.DAO.implement.IssueXLSXDAO
import PullRequest.DAO.implement.PullRequestCSVDAO
import RequerimientoPRJira.DAO.ProductividadService

fun main() {

    val pullrequestDAO = PullRequestCSVDAO()
    val issueDAO = IssueXLSXDAO()
    val productividadDAO= ProductividadService()

    val controller = ProductividadController(pullrequestDAO,issueDAO,productividadDAO)
    controller.exportMetricas()
}