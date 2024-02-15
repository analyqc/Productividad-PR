package AutomatizerFilters.Model

data class Issue(
    val clave: String,
    val tipoincidencia: String,
    val estado: String,
    val fechaTerminado: String
)