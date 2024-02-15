package org.example

import PullRequest.Controller.RepositoryController
import PullRequest.DAO.implement.Repository

fun main() {
        val repository = Repository()
        val properFile = RepositoryController(repository)
        println(properFile.repositoryExcel())
}