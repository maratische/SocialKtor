package maratische.socialktor.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import java.net.URI

fun Route.httpBinRouting() {
    route("/httpbin/get") {
        get {
//            val response = HttpBinResponse()
//            val baos = ByteArrayOutputStream()
//            copy(request.inputStream, baos)
//            val string = String(
//                baos.toByteArray(), StandardCharsets.UTF_8)
//            response["data"] = string
//            response["json"] = string
//            response["args"] = mapParametersToJSON(request)
//            response["headers"] = mapHeadersToJSON(request)
//            response["origin"] = request.remoteAddr
            call.request.origin.host
            val response = HttpBinResponse(
                call.request.queryParameters.entries(),
                call.request.headers.entries(),
                "data",
                call.request.origin.host,
                "json",
                getFullURL( call.request.origin)
            )
            call.respond(response)
        }
    }
}

private fun getFullURL(request: RequestConnectionPoint): String {
    val url = URI(request.scheme,
        null, request.host, request.port,
        request.uri, null, null)
    return url.toString()
}

@Serializable
data class HttpBinResponse(
    val args: Set<Map.Entry<String, List<String>>>?,
    val headers: Set<Map.Entry<String, List<String>>>?,
    val data: String?,
    val origin: String?,
    val json: String?,
    val url: String
)
