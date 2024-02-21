//import AutomatizerFilters.Model.Issue
//import AutomatizerFilters.Controller.ProductividadController
//import io.cucumber.java.en.Given
//import io.cucumber.java.en.Then
//import io.cucumber.java.en.When
//import org.junit.Assert
//
//class ProductividadExcel {
//
//    private lateinit var productividadController: ProductividadController
//
//    private lateinit var issuesExported: String
//    private lateinit var issue: Issue
//
//    @Given("obtengo un excel con los datos de los empleado \"([^\\\"]*)\\\"\\ , \"([^\\\"]*)\\\"\\ , \"([^\\\"]*)\\\"\\ y \"([^\\\"]*)\\\"\$")
//    fun datosExcel(Codigo:String,Incidencia:String, Estado:String, Tiempo:String){
//        issue = Issue(Codigo, Incidencia, Estado, Tiempo)
//        val jiraMockDAO = JiraMockDAO(listOf(issue))
//        productividadController = ProductividadController(jiraMockDAO)
//    }
//
//    @When("filtro el excel que obtenga")
//    fun filtroExcel() {
//        issuesExported = productividadController.productividadExcel()
//    }
//
//    @Then("obtendre historias culminadas en Junio 2023 \"([^\\\"]*)\\\"\$")
//    fun datosFiltrados(respuesta:String){
//        Assert.assertEquals(respuesta, issuesExported)
//    }
//
//}