package compose.project.sicedroid.data.model

import kotlinx.serialization.Serializable

@Serializable
data class AlumnoPerfil(
    val matricula: String = "",
    val nombre: String = "",
    val carrera: String = "",
    val semActual: Int = 0,
    val especialidad: String = "",
    val estatus: String = "",
    val cdtosAcumulados: Int = 0,
    val cdtosActuales: Int = 0,
    val inscrito: Boolean = false,
    val fechaReins: String = ""
)

@Serializable
data class MateriaCarga(
    val clvMat: String = "",
    val materia: String = "",
    val grupo: String = "",
    val maestro: String = "",
    val lunes: String = "",
    val martes: String = "",
    val miercoles: String = "",
    val jueves: String = "",
    val viernes: String = "",
    val sabado: String = ""
)

@Serializable
data class KardexMateria(
    val clvMat: String = "",
    val materia: String = "",
    val calif: Int = 0,
    val semestre: Int = 0,
    val periodo: String = ""
)

@Serializable
data class CalifUnidad(
    val clvMat: String = "",
    val materia: String = "",
    val unidades: List<String> = emptyList(),
    val promedio: String = "",
    // Campos temporales para parseo manual
    val c1: String? = null,
    val c2: String? = null,
    val c3: String? = null,
    val c4: String? = null,
    val c5: String? = null,
    val c6: String? = null,
    val c7: String? = null,
    val c8: String? = null,
    val c9: String? = null,
    val c10: String? = null,
    val c11: String? = null,
    val c12: String? = null,
    val c13: String? = null
)

@Serializable
data class CalifFinal(
    val clvMat: String = "",
    val materia: String = "",
    val calif: Int = 0,
    val evaluacion: String = ""
)
