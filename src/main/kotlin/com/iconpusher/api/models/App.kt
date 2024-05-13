package com.iconpusher.api.models

import com.iconpusher.api.models.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import com.google.gson.Gson
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class App
{
    var id = 0
	var name: String = ""
    var packageName = ""
    var version = ""
    var components = ArrayList<String>()
    var icon = ""
    //var rating: Float = 50f
	//var approved:Boolean = false

    companion object {
        fun load(packageName: String): App? {
            val appData = transaction {
                AppTable
                .join(VersionTable, JoinType.LEFT, additionalConstraint = {
                    (VersionTable.appId eq AppTable.id) and
                    (VersionTable.latest eq true)
                })
            .select{AppTable.packageName eq packageName}
                .singleOrNull()
            }
            if (appData != null) {
                val app = App()
                app.id = appData[AppTable.id].value
                app.name = appData[AppTable.name]
                app.packageName = appData[AppTable.packageName]
                app.version = appData[VersionTable.name]
                if (appData[AppTable.iconRemoved]) {
                    app.icon = "https://img.iconpusher.com/removed.png"
                } else {
                    app.icon = "https://img.iconpusher.com/${app.packageName.lowercase()}/${appData[VersionTable.name]}.${appData[VersionTable.extension]}"
                }
                populateComponents(app)
                return app
            }
            return null
        }

        fun populateComponents(app:App)
        {
            val results = transaction {
                ComponentTable
                .select{ComponentTable.appId eq app.id}
                .toList()
            }
            for (result in results) {
                val name = result[ComponentTable.componentInfo]
                //val component = Component()
                //component.name = name
                //component.appFilter = "<item component=\"ComponentInfo{${app.packageName}/${name}}\" drawable=\"king_of_thieves\" />"
                //app.components.add(component)
                app.components.add(name)
            }
        }
    }
}