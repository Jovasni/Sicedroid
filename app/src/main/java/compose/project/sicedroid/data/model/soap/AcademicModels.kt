package compose.project.sicedroid.data.model.soap

import org.simpleframework.xml.Element
import org.simpleframework.xml.Namespace
import org.simpleframework.xml.NamespaceList
import org.simpleframework.xml.Root
import org.simpleframework.xml.Path

// --- Carga Académica ---

@Root(name = "soap:Envelope")
@NamespaceList(
    Namespace(prefix = "soap", reference = "http://schemas.xmlsoap.org/soap/envelope/"),
    Namespace(prefix = "xsi", reference = "http://www.w3.org/2001/XMLSchema-instance"),
    Namespace(prefix = "xsd", reference = "http://www.w3.org/2001/XMLSchema")
)
data class CargaRequestEnvelope(
    @field:Element(name = "getCargaAcademicaByAlumno")
    @field:Namespace(reference = "http://tempuri.org/")
    @field:Path("soap:Body")
    var requestData: GetCargaData = GetCargaData()
)

@Root(name = "getCargaAcademicaByAlumno")
class GetCargaData

@Root(name = "Envelope", strict = false)
@Namespace(prefix = "soap", reference = "http://schemas.xmlsoap.org/soap/envelope/")
data class CargaResponseEnvelope(
    @field:Element(name = "Body", required = false)
    @field:Namespace(prefix = "soap")
    var body: CargaResponseBody? = null
)

@Root(name = "Body", strict = false)
data class CargaResponseBody(
    @field:Element(name = "getCargaAcademicaByAlumnoResponse", required = false)
    @field:Namespace(reference = "http://tempuri.org/")
    var response: GetCargaResponse? = null
)

@Root(name = "getCargaAcademicaByAlumnoResponse", strict = false)
data class GetCargaResponse(
    @field:Element(name = "getCargaAcademicaByAlumnoResult", required = false)
    var result: String? = null
)

// --- Kardex ---

@Root(name = "soap:Envelope")
@NamespaceList(
    Namespace(prefix = "soap", reference = "http://schemas.xmlsoap.org/soap/envelope/"),
    Namespace(prefix = "xsi", reference = "http://www.w3.org/2001/XMLSchema-instance"),
    Namespace(prefix = "xsd", reference = "http://www.w3.org/2001/XMLSchema")
)
data class KardexRequestEnvelope(
    @field:Element(name = "getAllKardexConPromedioByAlumno")
    @field:Namespace(reference = "http://tempuri.org/")
    @field:Path("soap:Body")
    var requestData: GetKardexData
)

@Root(name = "getAllKardexConPromedioByAlumno")
data class GetKardexData(
    @field:Element(name = "aluLineamiento") var aluLineamiento: Int = 1
)

@Root(name = "Envelope", strict = false)
@Namespace(prefix = "soap", reference = "http://schemas.xmlsoap.org/soap/envelope/")
data class KardexResponseEnvelope(
    @field:Element(name = "Body", required = false)
    @field:Namespace(prefix = "soap")
    var body: KardexResponseBody? = null
)

@Root(name = "Body", strict = false)
data class KardexResponseBody(
    @field:Element(name = "getAllKardexConPromedioByAlumnoResponse", required = false)
    @field:Namespace(reference = "http://tempuri.org/")
    var response: GetKardexResponse? = null
)

@Root(name = "getAllKardexConPromedioByAlumnoResponse", strict = false)
data class GetKardexResponse(
    @field:Element(name = "getAllKardexConPromedioByAlumnoResult", required = false)
    var result: String? = null
)

// --- Calificaciones Unidad ---

@Root(name = "soap:Envelope")
@NamespaceList(
    Namespace(prefix = "soap", reference = "http://schemas.xmlsoap.org/soap/envelope/"),
    Namespace(prefix = "xsi", reference = "http://www.w3.org/2001/XMLSchema-instance"),
    Namespace(prefix = "xsd", reference = "http://www.w3.org/2001/XMLSchema")
)
data class CalifUnidadRequestEnvelope(
    @field:Element(name = "getCalifUnidadesByAlumno")
    @field:Namespace(reference = "http://tempuri.org/")
    @field:Path("soap:Body")
    var requestData: GetCalifUnidadData = GetCalifUnidadData()
)

@Root(name = "getCalifUnidadesByAlumno")
class GetCalifUnidadData

@Root(name = "Envelope", strict = false)
@Namespace(prefix = "soap", reference = "http://schemas.xmlsoap.org/soap/envelope/")
data class CalifUnidadResponseEnvelope(
    @field:Element(name = "Body", required = false)
    @field:Namespace(prefix = "soap")
    var body: CalifUnidadResponseBody? = null
)

@Root(name = "Body", strict = false)
data class CalifUnidadResponseBody(
    @field:Element(name = "getCalifUnidadesByAlumnoResponse", required = false)
    @field:Namespace(reference = "http://tempuri.org/")
    var response: GetCalifUnidadResponse? = null
)

@Root(name = "getCalifUnidadesByAlumnoResponse", strict = false)
data class GetCalifUnidadResponse(
    @field:Element(name = "getCalifUnidadesByAlumnoResult", required = false)
    var result: String? = null
)

// --- Calificación Final ---

@Root(name = "soap:Envelope")
@NamespaceList(
    Namespace(prefix = "soap", reference = "http://schemas.xmlsoap.org/soap/envelope/"),
    Namespace(prefix = "xsi", reference = "http://www.w3.org/2001/XMLSchema-instance"),
    Namespace(prefix = "xsd", reference = "http://www.w3.org/2001/XMLSchema")
)
data class CalifFinalRequestEnvelope(
    @field:Element(name = "getAllCalifFinalByAlumnos")
    @field:Namespace(reference = "http://tempuri.org/")
    @field:Path("soap:Body")
    var requestData: GetCalifFinalData
)

@Root(name = "getAllCalifFinalByAlumnos")
data class GetCalifFinalData(
    @field:Element(name = "bytModEducativo") var bytModEducativo: Int = 1
)

@Root(name = "Envelope", strict = false)
@Namespace(prefix = "soap", reference = "http://schemas.xmlsoap.org/soap/envelope/")
data class CalifFinalResponseEnvelope(
    @field:Element(name = "Body", required = false)
    @field:Namespace(prefix = "soap")
    var body: CalifFinalResponseBody? = null
)

@Root(name = "Body", strict = false)
data class CalifFinalResponseBody(
    @field:Element(name = "getAllCalifFinalByAlumnosResponse", required = false)
    @field:Namespace(reference = "http://tempuri.org/")
    var response: GetCalifFinalResponse? = null
)

@Root(name = "getAllCalifFinalByAlumnosResponse", strict = false)
data class GetCalifFinalResponse(
    @field:Element(name = "getAllCalifFinalByAlumnosResult", required = false)
    var result: String? = null
)
