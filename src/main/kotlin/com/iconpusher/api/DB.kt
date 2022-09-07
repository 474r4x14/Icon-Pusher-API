package com.iconpusher.api

import org.jetbrains.exposed.sql.Database

object DB {
    var isConnected = false
    fun start()
    {
        if (!isConnected) {
            Database.connect("jdbc:mysql://192.99.11.191:3306/android_apps", driver = "com.mysql.cj.jdbc.Driver",
                user = "icons", password = "kfL9k9dzXR4q7T")
            isConnected = true
        }
    }
}