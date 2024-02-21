package PullRequestCSV

import PullRequest.DAO.implement.PullRequestCSVDAO
import PullRequestCSV.Controller.ProductividadController

fun main() {
    val pullrequestDAO = PullRequestCSVDAO()
    val controller = ProductividadController(pullrequestDAO)
    controller.exportMetricas()
}