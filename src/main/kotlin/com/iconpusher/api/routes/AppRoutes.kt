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

    route ("/device") {
        get("/{token}") {
            val token = call.parameters["token"] ?: return@get call.respondText(
                "Missing or malformed search",
                status = HttpStatusCode.BadRequest
            )

            val results = AppList.device(token)
            call.respondText(Gson().toJson(results))
        }
    }

    get("/package/{packageName}") {
        val packageName = call.parameters["packageName"] ?: return@get call.respondText(
            "Missing or malformed package name",
            status = HttpStatusCode.BadRequest
        )

        val app = App.load(packageName)
        call.respondText(Gson().toJson(app))
    }
}

fun Application.registerAppRoutes() {
    routing {
        appRouting()
    }
}
