package com.iconpusher.api.routes

import com.google.gson.Gson
import com.iconpusher.api.models.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.utils.io.errors.*
import kotlinx.coroutines.CoroutineStart
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.jodatime.CurrentDateTime
import org.jetbrains.exposed.sql.replace
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*


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

    post("/package/{packageName}") {
        val packageName = call.parameters["packageName"] ?: return@post call.respondText(
            "Missing or malformed package name",
            status = HttpStatusCode.BadRequest
        )

        val app = call.receive<UploadApp>()
        var addImage = true

        // Let's see if the app exists
        var appCheck = App.load(app.packageName)
        if (appCheck is App) {
            // If the app exists, let's check the version
            if (!App.checkVersionExists(appCheck,app.version)) {
                addImage = true
                App.addVersion(appCheck,app.version)
            }

            // Is the component new?
            if (!appCheck.components.contains(app.componentInfo)) {
                // Let's add it
                transaction {
                    ComponentTable.insert {
                        it[ComponentTable.appId] = appCheck!!.id
                        it[ComponentTable.componentInfo] = app.componentInfo
                        it[ComponentTable.dateAdded] = CurrentDateTime
                        it[ComponentTable.latest] = true
                    }
                }
            }

        } else {
            // App is new, let's add it
            addImage = true
            appCheck = App.create(app)
        }

        // Add in the request data
        transaction {
            RequestTable.replace {
                it[androidId] = app.androidId
                it[appId] = appCheck.id
                it[dateCreated] = CurrentDateTime
                it[iconPack] = app.iconPack
            }
        }


//        if (appCheck is App && addImage) {
            // While crossing over from v1 to v2, we need to handle both method for images
            // Because each version has a different naming convention & location

            // V1 image handling
            val oldImgDir = "/var/www/vhosts/iconpusher.com/www/htdocs/images/icons/${appCheck.id}"
            val oldImgFile = "$oldImgDir/${app.version}.png"

            // V2 image handling
            val newImgDir = "/var/www/vhosts/iconpusher.com/img/htdocs/${app.packageName.lowercase()}"
            val newImgFile = "$newImgDir/${app.version.replace("/", "-").replace("#", "-")}.png"

            // Convert the image from Base64 to data
            val decodedBytes = Base64.getDecoder().decode(app.icon)

        if (!File(newImgFile).exists()) {
            File(newImgDir).mkdir()
            File(newImgFile).writeBytes(decodedBytes)
        }

        if (!File(oldImgFile).exists()) {
            File(oldImgDir).mkdir()
            File(oldImgFile).writeBytes(decodedBytes)
        }

        call.respondText(Gson().toJson(app))
    }
}

fun Application.registerAppRoutes() {
    routing {
        appRouting()
    }
}
