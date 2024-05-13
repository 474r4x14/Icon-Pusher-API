package com.iconpusher.api.models

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class AppList {
    val apps = ArrayList<App>()
    companion object {
        fun search(keyword:String):AppList
        {
            val appList = AppList()

//            DB.start()
            val results = transaction {
                ComponentTable
                    .join(AppTable, JoinType.LEFT,additionalConstraint = {
                        ComponentTable.appId eq AppTable.id
                    })
                    .join(VersionTable, JoinType.INNER, additionalConstraint = {
                        (VersionTable.appId eq AppTable.id) and
                        (VersionTable.latest eq true)
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
                app.packageName = result[AppTable.packageName]
                if (result[AppTable.iconRemoved]) {
                    app.icon = "https://img.iconpusher.com/removed.png"
                } else {
                    app.icon = "https://img.iconpusher.com/${app.packageName.lowercase()}/${result[VersionTable.name]}.${result[VersionTable.extension]}"
                }
                app.version = result[VersionTable.name]
                //app.packages.add(result[ComponentTable.componentInfo])
//                populateComponents(app)
                // TODO might want to change this to get all app packages in one query
                appList.apps.add(app)
            }
            populateComponents(appList)
            return appList
        }
        
        
        fun latest():AppList
        {
//            DB.start()
            val appList = AppList()
            val results = transaction {
                AppTable
                .join(VersionTable, JoinType.INNER, additionalConstraint = {
                    (VersionTable.appId eq AppTable.id) and
                    (VersionTable.latest eq true)
                })
                .selectAll()
                .orderBy(AppTable.dateAdded, SortOrder.DESC)
                .limit(24)
                .toList()
            }
            
            for (result in results) {
                val app = App()
                app.id = result[AppTable.id].value
                app.name = result[AppTable.name]
                app.packageName = result[AppTable.packageName]
                app.icon = "https://img.iconpusher.com/${app.packageName}/${result[VersionTable.name]}.${result[VersionTable.extension]}"
                app.version = result[VersionTable.name]
                //app.packages.add(result[ComponentTable.componentInfo])
//                populateComponents(app)
                // TODO might want to change this to get all app packages in one query
                appList.apps.add(app)
            }
            populateComponents(appList)
            
            
            return appList
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

//            DB.start()
            val results = transaction {
                ComponentTable
                .select{ComponentTable.appId inList appIds}
                .orderBy(ComponentTable.dateAdded,SortOrder.DESC)
                .toList()
            }
            for (result in results) {
                val appId = result[ComponentTable.appId].value
                if (appMap.containsKey(appId)) {
                    appMap[appId]?.components?.add(result[ComponentTable.componentInfo])
                }
            }
        }
    }
}