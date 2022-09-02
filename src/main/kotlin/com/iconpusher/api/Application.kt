package com.iconpusher.api

import io.ktor.server.engine.*
import io.ktor.server.jetty.*
import com.iconpusher.api.plugins.*

fun main() {
    embeddedServer(Jetty, port = 8080, host = "0.0.0.0") {
        configureSerialization()
        configureMonitoring()
        configureHTTP()
        configureSecurity()
        configureRouting()
    }.start(wait = true)
}
