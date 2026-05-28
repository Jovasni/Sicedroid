package compose.project.sicedroid.ui

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.WorkInfo
import compose.project.sicedroid.data.repository.SicenetRepository
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SicenetViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = SicenetRepository(application)

    var loginUiState by mutableStateOf<LoginState>(LoginState.Idle)
        private set

    var profileUiState by mutableStateOf<FeatureState>(FeatureState.Idle)
        private set

    var cargaUiState by mutableStateOf<FeatureState>(FeatureState.Idle)
        private set

    var kardexUiState by mutableStateOf<FeatureState>(FeatureState.Idle)
        private set

    var califUnidadUiState by mutableStateOf<FeatureState>(FeatureState.Idle)
        private set

    var califFinalUiState by mutableStateOf<FeatureState>(FeatureState.Idle)
        private set

    fun login(matricula: String, contrasenia: String, tipo: String) {
        viewModelScope.launch {
            loginUiState = LoginState.Loading

            repository.loginAndAuthenticate(matricula, contrasenia, tipo)
                .onSuccess {
                    loginUiState = LoginState.Success
                    fetchProfile()
                }
                .onFailure { error ->
                    loginUiState = LoginState.Error(error.localizedMessage ?: "Error de conexión")
                }
        }
    }

    fun fetchProfile() {
        viewModelScope.launch {
            val (data, lastUpdated) = repository.getProfile()
            if (data != null) {
                profileUiState = FeatureState.Success(data, lastUpdated)
            } else if (!repository.isOnline()) {
                profileUiState = FeatureState.Error("Sin conexión y sin datos locales")
            } else {
                profileUiState = FeatureState.Loading
            }

            if (repository.isOnline()) {
                monitorSync("profile") { profileUiState = it }
            }
        }
    }

    fun fetchCarga() {
        viewModelScope.launch {
            val (data, lastUpdated) = repository.getCarga()
            if (data != null) cargaUiState = FeatureState.Success(data, lastUpdated)
            else if (!repository.isOnline()) cargaUiState = FeatureState.Error("Sin conexión")
            else cargaUiState = FeatureState.Loading

            if (repository.isOnline()) {
                monitorSync("carga") { cargaUiState = it }
            }
        }
    }

    fun fetchKardex() {
        viewModelScope.launch {
            val (data, lastUpdated) = repository.getKardex()
            if (data != null) kardexUiState = FeatureState.Success(data, lastUpdated)
            else if (!repository.isOnline()) kardexUiState = FeatureState.Error("Sin conexión")
            else kardexUiState = FeatureState.Loading

            if (repository.isOnline()) {
                monitorSync("kardex") { kardexUiState = it }
            }
        }
    }

    fun fetchCalifUnidad() {
        viewModelScope.launch {
            val (data, lastUpdated) = repository.getCalifUnidad()
            if (data != null) califUnidadUiState = FeatureState.Success(data, lastUpdated)
            else if (!repository.isOnline()) califUnidadUiState = FeatureState.Error("Sin conexión")
            else califUnidadUiState = FeatureState.Loading

            if (repository.isOnline()) {
                monitorSync("calif_unidad") { califUnidadUiState = it }
            }
        }
    }

    fun fetchCalifFinal() {
        viewModelScope.launch {
            val (data, lastUpdated) = repository.getCalifFinal()
            if (data != null) califFinalUiState = FeatureState.Success(data, lastUpdated)
            else if (!repository.isOnline()) califFinalUiState = FeatureState.Error("Sin conexión")
            else califFinalUiState = FeatureState.Loading

            if (repository.isOnline()) {
                monitorSync("calif_final") { califFinalUiState = it }
            }
        }
    }

    private fun monitorSync(type: String, onUpdate: (FeatureState) -> Unit) {
        viewModelScope.launch {
            repository.syncData(type).collectLatest { workInfo ->
                when (workInfo?.state) {
                    WorkInfo.State.SUCCEEDED -> {
                        val data = workInfo.outputData.getString("data")
                        if (data != null) {
                            onUpdate(FeatureState.Success(data, System.currentTimeMillis()))
                        }
                    }
                    WorkInfo.State.FAILED -> {
                        onUpdate(FeatureState.Error("Error en la sincronización"))
                    }
                    WorkInfo.State.RUNNING -> {
                        // Keep current state or show loading if no data
                    }
                    else -> {}
                }
            }
        }
    }
}