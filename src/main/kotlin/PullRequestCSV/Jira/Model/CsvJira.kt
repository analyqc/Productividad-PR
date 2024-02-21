package Jira.Model

data class CsvJira(
    val Resumen: String,
    val Clave: String,
    val Id: Int,
    val PadreUndefine: String,
    val TipoIncidencia: String,
    val Estado: String,
    val ProyectoKey: String,
    val Proyecto: String,
    val Responsable: String,
    val ResponsableAccountId: String,
    val EstimacionOriginal: Int,
    val TiempoEmpleado: Int,
    val FechaEnProgreso: String,
    val FechaEnTerminado: String,
    val ResponsableEmailAddress: String
)
