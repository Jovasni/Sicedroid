package compose.project.sicedroid.data.model.soap

import org.simpleframework.xml.Element
import org.simpleframework.xml.Namespace
import org.simpleframework.xml.NamespaceList
import org.simpleframework.xml.Root
import org.simpleframework.xml.Path
import org.simpleframework.xml.Order

@Root(name = "soap:Envelope")
@NamespaceList(
    Namespace(prefix = "soap", reference = "http://schemas.xmlsoap.org/soap/envelope/"),
    Namespace(prefix = "xsi", reference = "http://www.w3.org/2001/XMLSchema-instance"),
    Namespace(prefix = "xsd", reference = "http://www.w3.org/2001/XMLSchema")
)
data class LoginRequestEnvelope(
    @field:Element(name = "accesoLogin")
    @field:Namespace(reference = "http://tempuri.org/")
    @field:Path("soap:Body")
    var accesoLogin: AccesoLoginData
)

@Order(elements = ["strMatricula", "strContrasenia", "tipoUsuario"])
data class AccesoLoginData(
    @field:Element(name = "strMatricula") var strMatricula: String,
    @field:Element(name = "strContrasenia") var strContrasenia: String,
    @field:Element(name = "tipoUsuario") var tipoUsuario: String
)
