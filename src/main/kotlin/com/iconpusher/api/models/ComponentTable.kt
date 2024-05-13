package com.iconpusher.api.models

//import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.jodatime.datetime


//@Serializable
//data class Image(val id: String, val firstName: String, val lastName: String, val email: String)
object ComponentTable : IntIdTable(){

    @Transient
    override val tableName = "component_info"
//    var appId = integer("app_id")
    var appId = reference("app_id",AppTable.id)
    var componentInfo = varchar("component_info",1280)
    var dateAdded = datetime("date_added")
    var logoCreatedData = datetime("logo_created_date").nullable()
    var latest = bool("latest").nullable()
    var processed = bool("processed").nullable()
}
