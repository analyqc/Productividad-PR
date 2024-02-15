package Productivity.AutomatizerCantidadHistory.Controller

import Productivity.AutomatizerCantidadHistory.Model.IssueCantidad
import Productivity.AutomatizerCantidadHistory.DAO.CantidadHistoriasDAO

class CantidadHistorias {

    private var cantidadDAO = CantidadHistoriasDAO()

    fun cantidadRepeticiones() {
        val issues = cantidadDAO.getIssues()
        val filtros = { it: IssueCantidad ->
            it.tipoincidencia.equals("Historia", ignoreCase = true) &&
                    it.estado.equals("TERMINADO", ignoreCase = true) &&
                    it.fechaTerminado.matches("\\d{2}/06/2023".toRegex())
        }
        val issuesPeriodo = getIssuesDeRepeticiones(issues, filtros)
        val repetitionsMap = cantidadDAO.countRepetitions(issues, filtros)

        exportarIssuesDeRepeticiones(issuesPeriodo, repetitionsMap)
    }

    private fun getIssuesDeRepeticiones(issues: List<IssueCantidad>, filtros: (IssueCantidad) -> Boolean): List<IssueCantidad> {
        return issues.filter(filtros)
    }

    private fun exportarIssuesDeRepeticiones(issuesFiltrado: List<IssueCantidad>, repetitionsMap: Map<String, Int>) {
        for (issue in issuesFiltrado) {
            val key = "${issue.mail} - ${issue.tipoincidencia}"
            val count = repetitionsMap[key] ?: 0
            println("""${issue.mail} - ${issue.clave} - ${issue.tipoincidencia} - Repeticiones: $count """)
        }
    }

}
