package PullRequestCSV

import PullRequest.DAO.implement.ReadCSVDAO
import PullRequestCSV.Controller.ReadPRController

fun main() {
    val repository = ReadCSVDAO()
    val properFile = ReadPRController(repository)
    println(properFile.repositoryExcel())
}