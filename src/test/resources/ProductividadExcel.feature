Feature: Empleados que terminaron historias en el periodo determinado

  Scenario Outline: Filtro de la Incidencia en estado terminado en el mes de Junio 2023
    Given obtengo un excel con los datos de los empleado "<Codigo>" , "<Incidencia>" , "<Estado>" y "<Tiempo>"
    When filtro el excel que obtenga
    Then obtendre historias culminadas en Junio 2023 "<Respuesta>"

    Examples:
      | Codigo         | Incidencia | Estado    | Tiempo     | Respuesta                                          |
      | DIGB2B06-10372 | Historia   | TERMINADO | 12/06/2023 | DIGB2B06-10372 - Historia - TERMINADO - 12/06/2023 |
      | RPC00X3-1619   | Historia   | TERMINADO | 12/06/2023 | RPC00X3-1619 - Historia - TERMINADO - 12/06/2023   |
      | T0105-342      | Historia   | TERMINADO | 14/06/2023 | T0105-342 - Historia - TERMINADO - 14/06/2023      |
