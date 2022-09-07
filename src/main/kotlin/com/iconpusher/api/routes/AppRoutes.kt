package com.iconpusher.api.routes

import com.iconpusher.api.models.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import com.google.gson.Gson

fun Route.appRouting() {
    route ("/search") {
        get("/{search}") {
            val search = call.parameters["search"] ?: return@get call.respondText(
                "Missing or malformed search",
                status = HttpStatusCode.BadRequest
            )

            val results = AppList.search(search)
            call.respondText(Gson().toJson(results))
        }
    }
}

fun Application.registerAppRoutes() {
    routing {
        appRouting()
    }
}
