package com.iconpusher.api.models

//import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.jodatime.datetime


//@Serializable
//data class Image(val id: String, val firstName: String, val lastName: String, val email: String)
object AppTable : IntIdTable(){

    @Transient
    override val tableName = "app"
    var name = varchar("name", 128)
    var packageName = varchar("package_name", 128)
    var dateAdded = datetime("date_added")
    var hasImage = bool("has_image")
    var latestImage = datetime("latest_image").nullable()
    var dead = bool("dead")

}
