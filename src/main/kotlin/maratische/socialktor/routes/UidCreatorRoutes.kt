package maratische.socialktor.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import maratische.socialktor.models.ExternalHttpUidRequest
import maratische.socialktor.models.ResponseHttpUidRequest
import maratische.socialktor.utils.SecurityUtils
import java.util.*

private val keys = ArrayList<String>()
private val requests = ArrayList<ExternalHttpUidRequest>()

fun Route.uidCreatorRouting() {
    route("/uidcreator") {
        get {
            val data = call.parameters["data"] ?: return@get call.respondText(
                "Missing data",
                status = HttpStatusCode.BadRequest
            )
            val json = keys.map {
                try {
                    SecurityUtils.decrypt(it, data)
                } catch (e: Exception) {
                    null
                }
            }.findLast { it != null }?: return@get call.respondText(
                "not encrypted",
                status = HttpStatusCode.BadRequest
            )
            val request = Json.decodeFromString<ExternalHttpUidRequest>(json)
            requests.add(request)
            while (requests.size > 20) {
                requests.removeAt(0)
            }

            var uid: String? = null
            if (request.email.indexOf("uuid") > -1) {
                uid = UUID.randomUUID().toString()
            } else if (request.email.indexOf("zero") > -1) {
                uid = "0"
            } else if (request.email.indexOf("wait") > -1) {
                Thread.sleep(10000)
            }
            uid = uid ?: request.email.replace(".", "").replace("@", "")
            call.respond(ResponseHttpUidRequest(uid, false, false))
        }
        get("/key") {
            val privateKey = call.parameters["privateKey"] ?: return@get call.respondText(
                "Missing privateKey",
                status = HttpStatusCode.BadRequest
            )
            keys.add(privateKey)
            while (keys.size > 20) {
                keys.removeAt(0)
            }
            call.respond(privateKey)
        }
        get("/requests") {
            call.respond(requests)
        }
    }
}
