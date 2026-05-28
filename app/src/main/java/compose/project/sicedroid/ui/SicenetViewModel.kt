package compose.project.sicedroid.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import compose.project.sicedroid.data.repository.SicenetRepository
import kotlinx.coroutines.launch

class SicenetViewModel(
    private val repository: SicenetRepository = SicenetRepository()
) : ViewModel() {

    var loginUiState by mutableStateOf<LoginState>(LoginState.Idle)
        private set

    var profileUiState by mutableStateOf<ProfileState>(ProfileState.Idle)
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

    private fun fetchProfile() {
        viewModelScope.launch {
            profileUiState = ProfileState.Loading

            repository.fetchAcademicProfile()
                .onSuccess { data ->
                    profileUiState = ProfileState.Success(data)
                }
                .onFailure { error ->
                    profileUiState = ProfileState.Error(error.localizedMessage ?: "Error al obtener perfil")
                }
        }
    }
}