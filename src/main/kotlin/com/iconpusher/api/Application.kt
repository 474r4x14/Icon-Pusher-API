package com.iconpusher.api

import io.ktor.server.engine.*
import io.ktor.server.jetty.*
import com.iconpusher.api.plugins.*
import com.iconpusher.api.routes.registerAppRoutes
import com.iconpusher.api.routes.registerGroupRoutes
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.jetty.EngineMain.main(args)
}

fun Application.module() {
    configureSerialization()
    configureMonitoring()
    configureDatabases()
    configureHTTP()
    configureSecurity()
    configureRouting()
    registerAppRoutes()
    registerGroupRoutes()
}
