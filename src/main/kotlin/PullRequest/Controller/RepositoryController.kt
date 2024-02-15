package PullRequest.Controller

import PullRequest.DAO.implement.Repository
import PullRequest.Model.RepositoryGIT

class RepositoryController(private val repositoryDAO: Repository) {

    fun repositoryExcel():String {
        val repository = repositoryDAO.getRepository()
        val repositoryGitHub = getRepositoryGitHub(repository)

        return exportarFiltradosDelGitHub(repositoryGitHub)
    }

    private fun getRepositoryGitHub(repository: List<RepositoryGIT>): List<RepositoryGIT> {
        val repFiltrado = repository.filter {
            it.UserStory.isNotBlank()
        }
        return repFiltrado
    }

    private fun exportarFiltradosDelGitHub (repositoryFiltrado: List<RepositoryGIT>): String {
        var FiltradoDelGithub=""
        for (issue in repositoryFiltrado) {
            FiltradoDelGithub+="Email: ${issue.email} - PRNumber:${issue.PRNumber} - Usuario: ${issue.UserStory}\n"
        }
        return FiltradoDelGithub
    }
}
