package AutomatizerFilters.Controller

import AutomatizerFilters.Model.Issue
import ExcelGenerator
import Productivity.AutomatizerFilters.Model.IssueExcel
import Productivity.AutomatizerFilters.DAO.JiraDAO

class ProductividadController(val jiraDAO: JiraDAO){

    private val excelGenerator = ExcelGenerator()
    fun productividadExcel(rutaArchivo: String) {
        val issues = jiraDAO.getIssues()
        val filtradoIssuesPeriodo = exportarIssuesDelPeriodo(issues)
        excelGenerator.generarExcel(filtradoIssuesPeriodo, rutaArchivo)
    }

    private fun exportarIssuesDelPeriodo(issuesFiltrado: List<Issue>): List<IssueExcel> {
        val contadorDeTareas = mutableMapOf<String, Int>()
        val contadorDeHistorias = mutableMapOf<String, Int>()

        val repFiltradoTarea = issuesFiltrado.filter {
            it.fechaTerminado.matches("\\d{2}/06/2023".toRegex()) &&
                    it.tipoincidencia.equals("Tarea", ignoreCase = true)
        }
        for (issue in repFiltradoTarea) {
            contadorDeTareas[issue.responsable] = contadorDeTareas.getOrDefault(issue.responsable, 0) + 1
        }

        val repFiltradoHistoria = issuesFiltrado.filter {
            it.fechaTerminado.matches("\\d{2}/06/2023".toRegex()) &&
                    it.tipoincidencia.equals("Historia", ignoreCase = true)
        }
        for (issue in repFiltradoHistoria) {
            contadorDeHistorias[issue.responsable] = contadorDeHistorias.getOrDefault(issue.responsable, 0) + 1
        }

        val  uniqueEncargadoTareas= contadorDeTareas.keys.toSet()
        val  uniqueEncargadoHistorias= contadorDeHistorias.keys.toSet()

        val issuesExcel = mutableListOf<IssueExcel>()

        for (encargado in uniqueEncargadoTareas+uniqueEncargadoHistorias) {
            val totalTareas = contadorDeTareas.getOrDefault(encargado, 0)
            val totalHistorias = contadorDeHistorias.getOrDefault(encargado, 0)

            issuesExcel.add(IssueExcel(encargado, totalTareas, totalHistorias))
        }

        return issuesExcel
    }
}
