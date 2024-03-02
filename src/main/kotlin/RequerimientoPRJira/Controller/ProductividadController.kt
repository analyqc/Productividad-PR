package RequerimientoPRJira.Controller

import IssueXLSX.DAO.implement.IssueXLSXDAO
import PullRequestCSV.DAO.PullRequestDAO
import RequerimientoPRJira.DAO.ProductividadService

class ProductividadController(
    private val pullRequestDAO: PullRequestDAO,
    private val issueDAO : IssueXLSXDAO,
    private val productividadService: ProductividadService,
) {
    fun exportMetricas(){
        //TODO 1 exportar consolidado PullRequest
        val consolidadoPullRequest=pullRequestDAO.getPullRequests()
        //TODO 2 exportar consolidado JIRA
        val consolidadoJira=issueDAO.getIssue()

        //TODO 3 Filtrar PR (PullRequest) E Issues (JIRA)
        //TODO 4 Combinar listado de PR e Issues
        val unificados =productividadService.combinarListadoPRIssue(consolidadoPullRequest,consolidadoJira)

        //TODO 5 Calcular métrica PR/HU
        //val metricaPRHU = MetricaPRHU().calcularMetricaPRHU(unificados, consolidadoJira)
        //unificados devolver archivo excel y consolidado Jira agarrar sus datos

        //TODO 6 Calcular métrica Revisiones/PR
        //TODO 7 Exportar métricas

        return unificados
    }


}
