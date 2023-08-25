package com.iconpusher.api.models

//import kotlinx.serialization.Serializable
//import org.jetbrains.exposed.dao.id.IntIdTable
//import org.jetbrains.exposed.dao.Table
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.jodatime.datetime


//@Serializable
//data class Image(val id: String, val firstName: String, val lastName: String, val email: String)
object VersionTable : Table(){

    @Transient
    override val tableName = "version"
    var name = varchar("name", 128)
    var extension = varchar("extension", 128)
    var appId = reference("app_id",AppTable.id)
    var dateCreated = datetime("date_created")
    var latest = bool("latest")
}
