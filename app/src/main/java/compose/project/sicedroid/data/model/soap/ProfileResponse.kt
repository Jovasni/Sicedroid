package compose.project.sicedroid.data.model.soap

import org.simpleframework.xml.Element
import org.simpleframework.xml.Namespace
import org.simpleframework.xml.Root

@Root(name = "Envelope", strict = false)
@Namespace(prefix = "soap", reference = "http://schemas.xmlsoap.org/soap/envelope/")
data class ProfileResponseEnvelope(
    @field:Element(name = "Body", required = false)
    @field:Namespace(prefix = "soap")
    var body: ProfileResponseBody? = null
)

@Root(name = "Body", strict = false)
data class ProfileResponseBody(
    @field:Element(name = "getAlumnoAcademicoWithLineamientoResponse", required = false)
    @field:Namespace(reference = "http://tempuri.org/")
    var response: GetAlumnoAcademicoResponse? = null
)

@Root(name = "getAlumnoAcademicoWithLineamientoResponse", strict = false)
data class GetAlumnoAcademicoResponse(
    @field:Element(name = "getAlumnoAcademicoWithLineamientoResult", required = false)
    var result: String? = null
)
