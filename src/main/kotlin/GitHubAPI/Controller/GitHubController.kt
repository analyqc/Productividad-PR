package GitHubAPI.Controller

import GitHubAPI.DAO.implement.GitHubDAO

class GitHubController(
    private val gitHubDAO: GitHubDAO
) {
    fun exportarAPIDeUsuario(){

        val apiDeUsuario = gitHubDAO.getUser()

        return apiDeUsuario
    }
}