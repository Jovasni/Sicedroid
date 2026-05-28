package compose.project.sicedroid.data.model.soap

import org.simpleframework.xml.Element
import org.simpleframework.xml.Namespace
import org.simpleframework.xml.Root

@Root(name = "Envelope", strict = false)
@Namespace(prefix = "soap", reference = "http://schemas.xmlsoap.org/soap/envelope/")
data class LoginResponseEnvelope(
    @field:Element(name = "Body", required = false)
    @field:Namespace(prefix = "soap")
    var body: LoginResponseBody? = null
)

@Root(name = "Body", strict = false)
data class LoginResponseBody(
    @field:Element(name = "accesoLoginResponse", required = false)
    @field:Namespace(reference = "http://tempuri.org/")
    var response: AccesoLoginResponse? = null
)

@Root(name = "accesoLoginResponse", strict = false)
data class AccesoLoginResponse(
    @field:Element(name = "accesoLoginResult", required = false)
    var result: String? = null
)
