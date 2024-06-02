package com.iconpusher.api.routes

import com.google.gson.Gson
import com.iconpusher.api.models.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.CoroutineStart
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.jodatime.CurrentDateTime
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.File
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
        val appCheck = App.load(app.packageName)
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
                        it[ComponentTable.appId] = appId
                        it[ComponentTable.componentInfo] = app.componentInfo
                        it[ComponentTable.dateAdded] = CurrentDateTime
                    }
                }
            }

        } else {
            // App is new, let's add it
            addImage = true
            App.create(app)
        }

        // Add in the request data



        if (appCheck is App && addImage) {
            // While crossing over from v1 to v2, we need to handle both method for images
            // Because each version has a different naming convention & location

            // V1 image handling
            val appId = 0
//            val oldImgDir = "/var/www/vhosts/iconpusher.com/www/htdocs/images/icons/$appId.png"
            val oldImgDir = "C:/Users/os/tmp/icons/${appCheck.id}.png"

            // V2 image handling
            val pkg = "my.package"
            val version = "1"
//            val newImgDir = "/var/www/vhosts/iconpusher.com/img/htdocs/$pkg/$version.png"
            val newImgDir = "/var/www/vhosts/iconpusher.com/img/htdocs/$pkg/$version.png"

            // Convert the image from Base64 to data

            // decode base64 string
            val decodedBytes = Base64.getDecoder().decode(app.icon)


            File(oldImgDir).writeBytes(decodedBytes)
            File(newImgDir).writeBytes(decodedBytes)
        }



//        val app = App.load(packageName)
        call.respondText(Gson().toJson(app))



    }
}

fun Application.registerAppRoutes() {
    routing {
        appRouting()
    }
}
