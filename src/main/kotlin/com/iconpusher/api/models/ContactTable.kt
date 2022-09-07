package com.iconpusher.api.models

//import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.jodatime.CurrentDateTime
import org.jetbrains.exposed.sql.jodatime.datetime


//@Serializable
//data class Image(val id: String, val firstName: String, val lastName: String, val email: String)
object ContactTable : IntIdTable(){

    @Transient
    override val tableName = "contact"
    var name = varchar("name", 128)
    var email = varchar("email", 128)
    var message = text("message")
    var dateSubmitted = datetime("date_submitted")
}
