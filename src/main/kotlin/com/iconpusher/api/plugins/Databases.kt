package com.iconpusher.api.plugins

import com.iconpusher.api.models.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction


fun Application.configureDatabases() {
    val hostname = environment.config.property("db.host").getString()
    val username = environment.config.property("db.username").getString()
    val password = environment.config.property("db.password").getString()
    val database = environment.config.property("db.database").getString()


    val db = Database.connect(
        url = "jdbc:mysql://$hostname:3306/$database",
        user = username,
        driver = "com.mysql.cj.jdbc.Driver",
        password = password
    )

    transaction(db) {

//        SchemaUtils.create(AppTable)
//        SchemaUtils.create(ComponentTable)
//        SchemaUtils.create(ContactTable)

        SchemaUtils.createMissingTablesAndColumns(AppTable)
        SchemaUtils.createMissingTablesAndColumns(ComponentTable)
        SchemaUtils.createMissingTablesAndColumns(ContactTable)
        SchemaUtils.createMissingTablesAndColumns(VersionTable)
    }
}
