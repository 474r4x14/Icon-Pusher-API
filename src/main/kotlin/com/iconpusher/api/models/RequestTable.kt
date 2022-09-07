package com.iconpusher.api.models

//import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.jodatime.datetime


//@Serializable
//data class Image(val id: String, val firstName: String, val lastName: String, val email: String)
object RequestTable : Table(){

    @Transient
    override val tableName = "request"
    val androidId = varchar("android_id",128)
    val appId = reference("app_id", AppTable.id)
    override val primaryKey = PrimaryKey(
        androidId, appId,
        name = "PK-ANDROID_ID-APP_ID"
    )
}
