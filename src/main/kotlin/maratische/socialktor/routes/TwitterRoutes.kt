package maratische.socialktor.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.html.*
import maratische.socialktor.service.TwitterService

fun Route.twitterRouting() {
    val twitterService = TwitterService()
    route("/twitter") {
        get("/oauth/authenticate") {
            val oauthToken = call.request.queryParameters.get("oauth_token")
            call.respondHtml(HttpStatusCode.OK) {
                head {
                    title {
                        "Fake Twitter"
                    }
                }
                body {
                    h1 {
                        +"Fake Twitter"
                    }
                    br
                    form(action = "/twitter/login", method = FormMethod.post) {
                        label { +"E-mail" }
                        input(type = InputType.text, name = "username_or_email") {
                            id = "username_or_email"
                        }
                        br
                        label { +"Password" }
                        input(type = InputType.password, name = "password") {
                            id = "password"
                        }
                        input(type = InputType.hidden, name = "oauth_token") {
                            value = oauthToken ?: ""
                        }
                        br
                        input(type = InputType.submit) {
                            id = "allow"
                            value = "Submit"
                        }
                    }
                }
            }
        }

        post("/oauth/request_token") {
            val header = call.request.header("Authorization") ?: ""
            call.respondText(twitterService.createToken(header))
        }

        post("/login") {
            val params = call.receiveParameters()
            val usernameEmail =params.get("username_or_email") ?: ""
            val oauthToken = params.get("oauth_token") ?: ""
            call.respondRedirect(twitterService.login(usernameEmail, oauthToken))
        }

        post("/oauth/access_token") {
            val header = call.request.header("Authorization") ?: ""
            call.respondText(twitterService.getAccessToken(header))
        }

        get("/1.1/account/verify_credentials.json") {
            val header = call.request.header("Authorization") ?: ""
            call.respond(twitterService.getInfo(header))
        }
    }
}
