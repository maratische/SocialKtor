package maratische.socialktor

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import maratische.socialktor.plugins.configureRouting

//fun main() {
//    embeddedServer(Netty, port = 8088, host = "localtest.me", module = Application::module)
//        .start(wait = true)
//}
fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module() {
    install(ContentNegotiation) {
        json()
    }
    configureRouting()
}
