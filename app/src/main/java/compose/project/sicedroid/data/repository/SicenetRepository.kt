package compose.project.sicedroid.data.repository

import compose.project.sicedroid.data.model.soap.AccesoLoginData
import compose.project.sicedroid.data.model.soap.LoginRequestEnvelope
import compose.project.sicedroid.data.model.soap.ProfileRequestEnvelope
import compose.project.sicedroid.data.remote.NetworkProvider
import compose.project.sicedroid.data.remote.SicenetApiService

class SicenetRepository(
    private val apiService: SicenetApiService = NetworkProvider.apiService
) {

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
                Result.success(resultString)
            } else {
                Result.failure(Exception("Error de autenticación o credenciales incorrectas."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun fetchAcademicProfile(): Result<String> {
        return try {
            val request = ProfileRequestEnvelope()

            val response = apiService.getPerfil(request)
            val resultString = response.body?.response?.result

            if (!resultString.isNullOrEmpty()) {
                Result.success(resultString)
            } else {
                Result.failure(Exception("No se recibieron datos en el perfil académico."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}