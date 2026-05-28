package compose.project.sicedroid.data.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.asFlow
import androidx.work.*
import compose.project.sicedroid.data.local.AppDatabase
import compose.project.sicedroid.data.local.SicenetDao
import compose.project.sicedroid.data.model.soap.AccesoLoginData
import compose.project.sicedroid.data.model.soap.LoginRequestEnvelope
import compose.project.sicedroid.data.remote.NetworkProvider
import compose.project.sicedroid.data.remote.SicenetApiService
import compose.project.sicedroid.data.worker.FetchWorker
import compose.project.sicedroid.data.worker.StoreWorker
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SicenetRepository(
    private val context: Context,
    private val apiService: SicenetApiService = NetworkProvider.apiService,
    private val dao: SicenetDao = AppDatabase.getDatabase(context).sicenetDao()
) {

    fun isOnline(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
        return activeNetwork.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    suspend fun loginAndAuthenticate(
        matricula: String,
        contrasenia: String,
        tipo: String
    ): Result<String> {
        return try {
            val request = LoginRequestEnvelope(
                accesoLogin = AccesoLoginData(matricula, contrasenia, tipo)
            )

            val response = apiService.login(request)
            val resultString = response.body?.response?.result

            if (!resultString.isNullOrEmpty()) {
                // If login success and online, trigger profile sync
                if (isOnline()) {
                    syncData("profile")
                }
                Result.success(resultString)
            } else {
                Result.failure(Exception("Error de autenticación o credenciales incorrectas."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun syncData(type: String): Flow<WorkInfo?> {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val fetchRequest = OneTimeWorkRequestBuilder<FetchWorker>()
            .setConstraints(constraints)
            .setInputData(workDataOf("type" to type))
            .build()

        val storeRequest = OneTimeWorkRequestBuilder<StoreWorker>()
            .build()

        WorkManager.getInstance(context)
            .beginUniqueWork("sync_$type", ExistingWorkPolicy.REPLACE, fetchRequest)
            .then(storeRequest)
            .enqueue()

        return WorkManager.getInstance(context).getWorkInfoByIdLiveData(fetchRequest.id).asFlow()
    }

    suspend fun getProfile(): Pair<String?, Long?> {
        val local = dao.getProfile()
        return if (isOnline()) {
            // Trigger sync if online, but return local if exists
            syncData("profile")
            local?.data to local?.lastUpdated
        } else {
            local?.data to local?.lastUpdated
        }
    }

    suspend fun getCarga(): Pair<String?, Long?> {
        val local = dao.getCarga()
        if (isOnline()) syncData("carga")
        return local?.data to local?.lastUpdated
    }

    suspend fun getKardex(): Pair<String?, Long?> {
        val local = dao.getKardex()
        if (isOnline()) syncData("kardex")
        return local?.data to local?.lastUpdated
    }

    suspend fun getCalifUnidad(): Pair<String?, Long?> {
        val local = dao.getCalifUnidad()
        if (isOnline()) syncData("calif_unidad")
        return local?.data to local?.lastUpdated
    }

    suspend fun getCalifFinal(): Pair<String?, Long?> {
        val local = dao.getCalifFinal()
        if (isOnline()) syncData("calif_final")
        return local?.data to local?.lastUpdated
    }
}