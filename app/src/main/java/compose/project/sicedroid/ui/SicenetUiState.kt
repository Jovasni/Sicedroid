package compose.project.sicedroid.ui

sealed interface LoginState {
    object Idle : LoginState
    object Loading : LoginState
    object Success : LoginState
    data class Error(val message: String) : LoginState
}

sealed interface FeatureState {
    object Idle : FeatureState
    object Loading : FeatureState
    data class Success(val data: String, val lastUpdated: Long? = null) : FeatureState
    data class Error(val message: String) : FeatureState
}
