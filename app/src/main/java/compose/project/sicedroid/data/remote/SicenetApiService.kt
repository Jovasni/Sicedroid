package compose.project.sicedroid.data.remote

import compose.project.sicedroid.data.model.soap.LoginRequestEnvelope
import compose.project.sicedroid.data.model.soap.LoginResponseEnvelope
import compose.project.sicedroid.data.model.soap.ProfileRequestEnvelope
import compose.project.sicedroid.data.model.soap.ProfileResponseEnvelope
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface SicenetApiService {

    @Headers(
        "Content-Type: text/xml; charset=utf-8",
        "SOAPAction: \"http://tempuri.org/accesoLogin\""
    )
    @POST("ws/wsalumnos.asmx")
    suspend fun login(@Body envelope: LoginRequestEnvelope): LoginResponseEnvelope

    @Headers(
        "Content-Type: text/xml; charset=utf-8",
        "SOAPAction: \"http://tempuri.org/getAlumnoAcademicoWithLineamiento\""
    )
    @POST("ws/wsalumnos.asmx")
    suspend fun getPerfil(@Body envelope: ProfileRequestEnvelope): ProfileResponseEnvelope
}