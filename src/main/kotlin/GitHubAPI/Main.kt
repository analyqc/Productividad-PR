package GitHubAPI

import GitHubAPI.Controller.GitHubController
import GitHubAPI.DAO.implement.GitHubDAO

fun main() {

    val gitHubDAO= GitHubDAO()

    val controller = GitHubController(gitHubDAO)
    controller.exportarAPIDeUsuario()
}
