package compose.project.sicedroid.data.remote

import compose.project.sicedroid.data.model.soap.CalifFinalRequestEnvelope
import compose.project.sicedroid.data.model.soap.CalifFinalResponseEnvelope
import compose.project.sicedroid.data.model.soap.CalifUnidadRequestEnvelope
import compose.project.sicedroid.data.model.soap.CalifUnidadResponseEnvelope
import compose.project.sicedroid.data.model.soap.CargaRequestEnvelope
import compose.project.sicedroid.data.model.soap.CargaResponseEnvelope
import compose.project.sicedroid.data.model.soap.KardexRequestEnvelope
import compose.project.sicedroid.data.model.soap.KardexResponseEnvelope
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

    @Headers(
        "Content-Type: text/xml; charset=utf-8",
        "SOAPAction: \"http://tempuri.org/getCargaAcademicaByAlumno\""
    )
    @POST("ws/wsalumnos.asmx")
    suspend fun getCarga(@Body envelope: CargaRequestEnvelope): CargaResponseEnvelope

    @Headers(
        "Content-Type: text/xml; charset=utf-8",
        "SOAPAction: \"http://tempuri.org/getAllKardexConPromedioByAlumno\""
    )
    @POST("ws/wsalumnos.asmx")
    suspend fun getKardex(@Body envelope: KardexRequestEnvelope): KardexResponseEnvelope

    @Headers(
        "Content-Type: text/xml; charset=utf-8",
        "SOAPAction: \"http://tempuri.org/getCalifUnidadesByAlumno\""
    )
    @POST("ws/wsalumnos.asmx")
    suspend fun getCalifUnidades(@Body envelope: CalifUnidadRequestEnvelope): CalifUnidadResponseEnvelope

    @Headers(
        "Content-Type: text/xml; charset=utf-8",
        "SOAPAction: \"http://tempuri.org/getAllCalifFinalByAlumnos\""
    )
    @POST("ws/wsalumnos.asmx")
    suspend fun getCalifFinal(@Body envelope: CalifFinalRequestEnvelope): CalifFinalResponseEnvelope
}