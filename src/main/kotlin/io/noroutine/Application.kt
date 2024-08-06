package io.noroutine

import io.ktor.server.application.*
import io.noroutine.plugins.configureRouting
import io.noroutine.plugins.configureSecurity

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureSecurity()
    configureRouting()
}
