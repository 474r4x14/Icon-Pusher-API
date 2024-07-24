package com.iconpusher.api.routes

import com.iconpusher.api.models.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import com.google.gson.Gson

fun Route.GroupRouting() {
    get("/latest") {
        val app = AppList.latest()
        call.respondText(Gson().toJson(app))
    }
    // TODO replace the original latest (above) once change is established
    get("/latest-new") {
        val app = AppList.latestNew()
        call.respondText(Gson().toJson(app))
    }
}

fun Application.registerGroupRoutes() {
    routing {
        GroupRouting()
    }
}
