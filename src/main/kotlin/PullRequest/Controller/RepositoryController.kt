package PullRequest.Controller

import PullRequest.DAO.implement.Repository
import PullRequest.Model.RepositoryGIT

class RepositoryController(private val repositoryDAO: Repository) {

    fun repositoryExcel():String {
        val repository = repositoryDAO.getRepository()
        return exportarFiltradosDelGitHub(repository)
    }

    private fun exportarFiltradosDelGitHub(repositoryFiltrado: List<RepositoryGIT>): String {

        val repFiltrado = repositoryFiltrado.filter {
            it.branch.equals("develop", ignoreCase = true)&&
                    it.PRMerge.isNotBlank()
        }
        val emailCountMap = mutableMapOf<String, Int>()

        for (issue in repFiltrado) {
            emailCountMap[issue.email] = emailCountMap.getOrDefault(issue.email, 0) + 1
        }


        val repFiltradoDesing = repositoryFiltrado.filter {
            it.branch.equals("develop", ignoreCase = true)&&
                    it.PRMerge.isNotBlank() &&
                    !it.repository.matches(Regex(".*-de?si?gn?-.*", RegexOption.IGNORE_CASE))
        }
        val emailCountMapDesing = mutableMapOf<String, Int>()
        for (issue in repFiltradoDesing) {
            emailCountMapDesing[issue.email] = emailCountMapDesing.getOrDefault(issue.email, 0) + 1
        }

        val uniqueEmails = emailCountMap.keys.toSet()
        val uniqueEmailsDesing = emailCountMapDesing.keys.toSet()

        val filtradoGitHub = StringBuilder()

        for (email in uniqueEmails+uniqueEmailsDesing ) {
            val totalTareas = emailCountMap.getOrDefault(email, 0)
            val totalHistorias = emailCountMapDesing.getOrDefault(email, 0)
            filtradoGitHub.append("- Cantidad de HU: $totalTareas \t -Total PR: $totalHistorias \t -Email: $email \n")
        }

        return filtradoGitHub.toString()
    }

}
