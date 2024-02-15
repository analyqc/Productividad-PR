package Productivity.AutomatizerCantidadHistory.Model

data class IssueCantidad(
    val clave: String,
    val tipoincidencia: String,
    val estado: String,
    val fechaTerminado: String,
    val mail: String
)