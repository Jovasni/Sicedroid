package compose.project.sicedroid.ui

sealed interface LoginState {
    object Idle : LoginState
    object Loading : LoginState
    object Success : LoginState
    data class Error(val message: String) : LoginState
}

sealed interface ProfileState {
    object Idle : ProfileState
    object Loading : ProfileState
    data class Success(val rawProfileData: String) : ProfileState
    data class Error(val message: String) : ProfileState
}