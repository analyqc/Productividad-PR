package AutomatizerFilters.Controller

import AutomatizerFilters.Model.Issue
import Productivity.AutomatizerFilters.DAO.JiraDAO

class ProductividadController(val jiraDAO: JiraDAO){
    fun productividadExcel():String {
        val issues = jiraDAO.getIssues()
        return exportarIssuesDelPeriodo(issues)
    }

   private fun exportarIssuesDelPeriodo (issuesFiltrado: List<Issue>): String {

       val repFiltradoTarea = issuesFiltrado.filter {
           it.fechaTerminado.matches("\\d{2}/06/2023".toRegex()) &&
           it.tipoincidencia.equals("Tarea", ignoreCase = true)
       }

       val contadorDeTareas = mutableMapOf<String, Int>()
       for (issue in repFiltradoTarea) {
           contadorDeTareas[issue.responsable] = contadorDeTareas.getOrDefault(issue.responsable, 0) + 1
       }

       val repFiltradoHistoria = issuesFiltrado.filter {
           it.fechaTerminado.matches("\\d{2}/06/2023".toRegex()) &&
                   it.tipoincidencia.equals("Historia", ignoreCase = true)
       }

       val contadorDeHistorias = mutableMapOf<String, Int>()
       for (issue in repFiltradoHistoria) {
           contadorDeHistorias[issue.responsable] = contadorDeHistorias.getOrDefault(issue.responsable, 0) + 1
       }

       val  uniqueEncargadoTareas= contadorDeTareas.keys.toSet()
       val  uniqueEncargadoHistorias= contadorDeHistorias.keys.toSet()

       val filtradoIssuesPeriodo = StringBuilder()

       for (encargado in uniqueEncargadoTareas+uniqueEncargadoHistorias) {

           val totalTareas = contadorDeTareas.getOrDefault(encargado, 0)
           val totalHistorias = contadorDeHistorias.getOrDefault(encargado, 0)

           filtradoIssuesPeriodo.append("-Total de tareas: $totalTareas \t -Total de historias: $totalHistorias \t -Encargado: $encargado\n")
       }

       return filtradoIssuesPeriodo.toString()
   }
}
