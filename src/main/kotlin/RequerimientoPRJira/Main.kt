package RequerimientoPRJira

import RequerimientoPRJira.Controller.ProductividadController
import IssueXLSX.DAO.implement.IssueXLSXDAO
import PullRequest.DAO.implement.PullRequestCSVDAO
import RequerimientoPRJira.DAO.ProductividadDAO

fun main() {

    val pullrequestDAO = PullRequestCSVDAO()
    val issueDAO = IssueXLSXDAO()
    val productividadDAO= ProductividadDAO()

    val controller = ProductividadController(pullrequestDAO,issueDAO,productividadDAO)
    controller.exportMetricas()
}