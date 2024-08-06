package io.noroutine.plugins

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.apache.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.resources.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.plugins.autohead.*
import io.ktor.server.resources.*
import io.ktor.server.resources.Resources
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import io.ktor.util.logging.*
import io.noroutine.models.UserInfo
import kotlinx.serialization.Serializable
import java.io.File

internal val LOGGER = KtorSimpleLogger("io.noroutine.Routing")

private suspend fun getPersonalGreeting(
    httpClient: HttpClient,
    userSession: UserSession
): UserInfo = httpClient.get("https://www.googleapis.com/oauth2/v2/userinfo") {
    headers {
        append(HttpHeaders.Authorization, "Bearer ${userSession.accessToken}")
    }
}.body<UserInfo>().also { LOGGER.info("UserInfo: $it") }

fun Application.configureRouting() {
    install(Resources)
    install(AutoHeadResponse)
    install(Sessions) {
        cookie<MySession>("MY_SESSION") {
            cookie.extensions["SameSite"] = "lax"
        }
        cookie<UserSession>(
            "MY_USER",
            directorySessionStorage(File(System.getenv("SESSION_STORAGE_DIR") ?: "build/.sessions"))
        )
    }
    routing {
        get("/") {
            val userSession: UserSession? = call.sessions.get()
            if (userSession != null) {
                val httpClient = HttpClient(Apache) {
                    install(ContentNegotiation) {
                        json()
                    }
                }
                val userInfo: UserInfo = getPersonalGreeting(httpClient, userSession)
                call.respondText("Hello, ${userInfo.name}!")
            } else {
                call.respondText("Hello World!")
            }
        }
        get("/session/increment") {
            val session = call.sessions.get<MySession>() ?: MySession()
            call.sessions.set(session.copy(count = session.count + 1))
            call.respondText("Counter is ${session.count}. Refresh to increment.")
        }
        get<Articles> { article ->
            // Get all articles ...
            call.respond("List of articles sorted starting from ${article.sort}")
        }
        // Static plugin. Try to access `/static/index.html`
        staticResources("/static", "static")
    }
}

@Serializable
@Resource("/articles")
class Articles(val sort: String? = "new")
