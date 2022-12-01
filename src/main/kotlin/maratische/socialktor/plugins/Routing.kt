package maratische.socialktor.plugins

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import maratische.socialktor.routes.httpBinRouting
import maratische.socialktor.routes.twitterRouting
import maratische.socialktor.routes.uidCreatorRouting

fun Application.configureRouting() {

    routing {
        uidCreatorRouting()
        httpBinRouting()
        twitterRouting()
        get("/") {
            call.respondText("Hello World!")
        }
        post("/") {
            call.respondText("Hello POST World!")
        }
    }
}
