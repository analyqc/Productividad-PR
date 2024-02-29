package RequerimientoPRJira.Model

data class MetricaUnificada(
    val correoPr:String,
    val nombreHistoriaMetrica:String,
    var repeticiones:Int,
    var confirmacion:String
)
