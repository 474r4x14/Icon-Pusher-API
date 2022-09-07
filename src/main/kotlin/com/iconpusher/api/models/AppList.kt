package com.iconpusher.api.models

import com.iconpusher.api.DB
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class AppList {
    val apps = ArrayList<App>()
    companion object {
        fun search(keyword:String):AppList
        {
            val appList = AppList()

            DB.start()
            val results = transaction {
                ComponentTable
                    .join(AppTable, JoinType.LEFT,additionalConstraint = {
                        ComponentTable.appId eq AppTable.id
                    })
                    .select {
                        (
                            (AppTable.hasImage eq true) or
                            (AppTable.latestImage neq null)
                        ) and (
                            (AppTable.name like("%$keyword%")) or
                            (ComponentTable.componentInfo like("%$keyword%"))
                        )
                    }
                    .groupBy(AppTable.id)
                    .toList()
            }
            for (result in results) {
                val app = App()
                app.id = result[AppTable.id].value
                app.name = result[AppTable.name]
                //app.packages.add(result[ComponentTable.componentInfo])
//                populateComponents(app)
                // TODO might want to change this to get all app packages in one query
                appList.apps.add(app)
            }
            populateComponents(appList)
            return appList
        }

        fun populateComponents(app:App)
        {
            DB.start()
            val results = transaction {
                ComponentTable
                .select{ComponentTable.appId eq app.id}
                .toList()
            }
            for (result in results) {
                app.packages.add(result[ComponentTable.componentInfo])
            }
        }

        fun populateComponents(appList: AppList)
        {
            // Map for easy App access
            val appMap = HashMap<Int, App>()
            // Build up a list of app ids
            val appIds = ArrayList<Int>()
            for (app in appList.apps) {
                appIds.add(app.id)
                appMap[app.id] = app
            }

            DB.start()
            val results = transaction {
                ComponentTable
                .select{ComponentTable.appId inList appIds}
                .orderBy(ComponentTable.dateAdded,SortOrder.DESC)
                .toList()
            }
            for (result in results) {
                val appId = result[ComponentTable.appId].value
                if (appMap.containsKey(appId)) {
                    appMap[appId]?.packages?.add(result[ComponentTable.componentInfo])
                }
            }
        }
    }
}