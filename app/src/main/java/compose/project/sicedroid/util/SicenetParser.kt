package compose.project.sicedroid.util

import android.util.Log
import androidx.core.text.HtmlCompat
import compose.project.sicedroid.data.model.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.*

object SicenetParser {
    private const val TAG = "SicenetParser"
    private val json = Json { 
        ignoreUnknownKeys = true 
        coerceInputValues = true
        isLenient = true
    }

    private fun prepareData(data: String): String {
        val trimmed = data.trim()
        return if (trimmed.startsWith("{") || trimmed.startsWith("[")) {
            trimmed
        } else {
            HtmlCompat.fromHtml(data, HtmlCompat.FROM_HTML_MODE_LEGACY).toString().trim()
        }
    }

    private fun JsonObject.flexibleGet(vararg keys: String): String? {
        for (key in keys) {
            this[key]?.jsonPrimitive?.contentOrNull?.let { return it }
        }
        val lowerKeys = keys.map { it.lowercase() }
        for ((actualKey, value) in this) {
            if (actualKey.lowercase() in lowerKeys) {
                return value.jsonPrimitive.contentOrNull
            }
        }
        for ((actualKey, value) in this) {
            val lowerActual = actualKey.lowercase()
            if (lowerKeys.any { lowerActual.contains(it) }) {
                return value.jsonPrimitive.contentOrNull
            }
        }
        return null
    }

    private fun logKeys(label: String, obj: JsonObject) {
        Log.d(TAG, "Available keys in $label: ${obj.keys.joinToString(", ")}")
    }

    fun parsePerfil(data: String): AlumnoPerfil {
        return try {
            val clean = prepareData(data)
            val obj = json.parseToJsonElement(clean).jsonObject
            logKeys("Profile", obj)
            AlumnoPerfil(
                matricula = obj.flexibleGet("matricula") ?: "",
                nombre = obj.flexibleGet("nombre") ?: "",
                carrera = obj.flexibleGet("carrera") ?: "",
                semActual = obj.flexibleGet("semActual", "semestre")?.toIntOrNull() ?: 0,
                especialidad = obj.flexibleGet("especialidad") ?: "",
                estatus = obj.flexibleGet("estatus") ?: "",
                cdtosAcumulados = obj.flexibleGet("cdtosAcumulados")?.toIntOrNull() ?: 0,
                cdtosActuales = obj.flexibleGet("cdtosActuales")?.toIntOrNull() ?: 0,
                inscrito = obj.flexibleGet("inscrito")?.toBoolean() ?: false,
                fechaReins = obj.flexibleGet("fechaReins") ?: ""
            )
        } catch (e: Exception) {
            Log.e(TAG, "Error parsing profile JSON", e)
            AlumnoPerfil()
        }
    }

    fun parseCarga(data: String): List<MateriaCarga> {
        return try {
            val clean = prepareData(data)
            val jsonArray = json.parseToJsonElement(clean).jsonArray
            if (jsonArray.isNotEmpty()) logKeys("Carga Item", jsonArray[0].jsonObject)
            jsonArray.map { element ->
                val obj = element.jsonObject
                MateriaCarga(
                    clvMat = obj.flexibleGet("clvMat", "clave", "id") ?: "",
                    materia = obj.flexibleGet("materia", "nombre", "asignatura", "desMat") ?: "Materia sin nombre",
                    grupo = obj.flexibleGet("grupo") ?: "",
                    maestro = obj.flexibleGet("maestro", "profesor", "docente") ?: "Sin profesor asignado",
                    lunes = obj.flexibleGet("lunes") ?: "",
                    martes = obj.flexibleGet("martes") ?: "",
                    miercoles = obj.flexibleGet("miercoles") ?: "",
                    jueves = obj.flexibleGet("jueves") ?: "",
                    viernes = obj.flexibleGet("viernes") ?: "",
                    sabado = obj.flexibleGet("sabado") ?: ""
                )
            }
        } catch (e: Exception) {
            Log.e(TAG, "All carga parsing attempts failed", e)
            emptyList()
        }
    }

    fun parseKardex(data: String): List<KardexMateria> {
        return try {
            val clean = prepareData(data)
            val jsonArray = json.parseToJsonElement(clean).jsonArray
            if (jsonArray.isNotEmpty()) logKeys("Kardex Item", jsonArray[0].jsonObject)
            jsonArray.map { element ->
                val obj = element.jsonObject
                KardexMateria(
                    clvMat = obj.flexibleGet("clvMat", "clave") ?: "",
                    materia = obj.flexibleGet("materia", "nombre", "asignatura") ?: "",
                    calif = obj.flexibleGet("calif", "calificacion")?.toIntOrNull() ?: 0,
                    semestre = obj.flexibleGet("semestre")?.toIntOrNull() ?: 0,
                    periodo = obj.flexibleGet("periodo") ?: ""
                )
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error parsing kardex JSON", e)
            emptyList()
        }
    }

    fun parseCalifUnidad(data: String): List<CalifUnidad> {
        return try {
            val clean = prepareData(data)
            val jsonArray = json.parseToJsonElement(clean).jsonArray
            if (jsonArray.isNotEmpty()) logKeys("CalifUnidad Item", jsonArray[0].jsonObject)
            jsonArray.map { element ->
                val obj = element.jsonObject
                val unidades = mutableListOf<String>()
                for (i in 1..13) {
                    val key = "C$i"
                    obj.flexibleGet(key)?.let { 
                        if (it.isNotBlank()) unidades.add(it)
                    }
                }
                CalifUnidad(
                    clvMat = obj.flexibleGet("clvMat", "clave") ?: "",
                    materia = obj.flexibleGet("materia", "nombre", "asignatura", "desMat") ?: "Materia sin nombre",
                    unidades = unidades,
                    promedio = obj.flexibleGet("promedio") ?: ""
                )
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error parsing calif unidad JSON", e)
            emptyList()
        }
    }

    fun parseCalifFinal(data: String): List<CalifFinal> {
        return try {
            val clean = prepareData(data)
            val jsonArray = json.parseToJsonElement(clean).jsonArray
            if (jsonArray.isNotEmpty()) logKeys("CalifFinal Item", jsonArray[0].jsonObject)
            jsonArray.map { element ->
                val obj = element.jsonObject
                CalifFinal(
                    clvMat = obj.flexibleGet("clvMat", "clave") ?: "",
                    materia = obj.flexibleGet("materia", "nombre") ?: "",
                    calif = obj.flexibleGet("calif", "calificacion")?.toIntOrNull() ?: 0,
                    evaluacion = obj.flexibleGet("evaluacion") ?: ""
                )
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error parsing calif final JSON", e)
            emptyList()
        }
    }
}
