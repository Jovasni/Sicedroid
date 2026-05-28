package compose.project.sicedroid.data.model.soap

import org.simpleframework.xml.Element
import org.simpleframework.xml.Namespace
import org.simpleframework.xml.NamespaceList
import org.simpleframework.xml.Root
import org.simpleframework.xml.Path

@Root(name = "soap:Envelope")
@NamespaceList(
    Namespace(prefix = "soap", reference = "http://schemas.xmlsoap.org/soap/envelope/"),
    Namespace(prefix = "xsi", reference = "http://www.w3.org/2001/XMLSchema-instance"),
    Namespace(prefix = "xsd", reference = "http://www.w3.org/2001/XMLSchema")
)
data class ProfileRequestEnvelope(
    @field:Element(name = "getAlumnoAcademicoWithLineamiento")
    @field:Namespace(reference = "http://tempuri.org/")
    @field:Path("soap:Body")
    var getAlumnoAcademico: GetAlumnoAcademicoData = GetAlumnoAcademicoData()
)

@Root(name = "getAlumnoAcademicoWithLineamiento")
class GetAlumnoAcademicoData
