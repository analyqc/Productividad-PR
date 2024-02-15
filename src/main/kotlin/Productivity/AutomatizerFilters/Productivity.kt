package AutomatizerFilters

import AutomatizerFilters.Controller.ProductividadController
import AutomatizerFilters.DAO.JiraExcelDAO

fun main(){
    val jiraDAO = JiraExcelDAO()
    val properFile = ProductividadController(jiraDAO)
    println(properFile.productividadExcel())
}
