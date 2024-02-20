package PullRequestCSV

import PullRequest.DAO.implement.PullRequestCSVDAO
import PullRequestCSV.Controller.ProductividadController

fun main() {
    val pullrequestDao = PullRequestCSVDAO()
    val controller = ProductividadController(pullrequestDao)
    controller.exportMetricas()
}