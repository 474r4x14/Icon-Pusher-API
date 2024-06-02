package com.iconpusher.api.models

import com.iconpusher.api.models.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import com.google.gson.Gson
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

data class UploadApp(val packageName:String)
{
	var appName: String = ""
	var iconPack: String = ""
    var androidId = ""
    var version = ""
    var componentInfo = ""
    var icon = ""
    //var rating: Float = 50f
	//var approved:Boolean = false

}