package compose.project.sicedroid.data.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import compose.project.sicedroid.data.local.AppDatabase
import compose.project.sicedroid.data.local.CargaEntity
import compose.project.sicedroid.data.local.ProfileEntity
import compose.project.sicedroid.data.local.KardexEntity
import compose.project.sicedroid.data.local.CalifUnidadEntity
import compose.project.sicedroid.data.local.CalifFinalEntity
import compose.project.sicedroid.data.model.soap.*
import compose.project.sicedroid.data.remote.NetworkProvider

class FetchWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        val type = inputData.getString("type") ?: return Result.failure()
        val apiService = NetworkProvider.apiService

        return try {
            val result = when (type) {
                "profile" -> apiService.getPerfil(ProfileRequestEnvelope()).body?.response?.result
                "carga" -> apiService.getCarga(CargaRequestEnvelope()).body?.response?.result
                "kardex" -> apiService.getKardex(KardexRequestEnvelope(GetKardexData())).body?.response?.result
                "calif_unidad" -> apiService.getCalifUnidades(CalifUnidadRequestEnvelope()).body?.response?.result
                "calif_final" -> apiService.getCalifFinal(CalifFinalRequestEnvelope(GetCalifFinalData())).body?.response?.result
                else -> null
            }

            if (result != null) {
                Result.success(workDataOf("type" to type, "data" to result))
            } else {
                Result.failure()
            }
        } catch (e: Exception) {
            Result.failure()
        }
    }
}

class StoreWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        val type = inputData.getString("type") ?: return Result.failure()
        val data = inputData.getString("data") ?: return Result.failure()
        val dao = AppDatabase.getDatabase(applicationContext).sicenetDao()

        return try {
            when (type) {
                "profile" -> dao.insertProfile(ProfileEntity(data = data))
                "carga" -> dao.insertCarga(CargaEntity(data = data))
                "kardex" -> dao.insertKardex(KardexEntity(data = data))
                "calif_unidad" -> dao.insertCalifUnidad(CalifUnidadEntity(data = data))
                "calif_final" -> dao.insertCalifFinal(CalifFinalEntity(data = data))
            }
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}
