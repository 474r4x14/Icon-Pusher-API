package com.iconpusher.api

import io.ktor.server.engine.*
import io.ktor.server.jetty.*
import com.iconpusher.api.plugins.*
import com.iconpusher.api.routes.registerAppRoutes
import io.ktor.server.application.*

fun main() {
    embeddedServer(Jetty, port = 8083, host = "0.0.0.0") {
        begin(this)
    }.start(wait = true)
}

/**
 * For running via WAR
 */

fun Application.module() {
    begin(this)
}
fun begin(app: Application)
{
    app.configureSerialization()
    app.configureMonitoring()
    app.configureHTTP()
    app.configureSecurity()
    app.configureRouting()
    app.registerAppRoutes()
}
