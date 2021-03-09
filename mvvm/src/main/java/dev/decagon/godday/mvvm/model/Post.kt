package dev.decagon.godday.mvvm.model

import androidx.room.Entity
import androidx.room.PrimaryKey


/**
 * This class servers as a model for the post objects received from the server.
 * It is also used as an entity/ table for the local database
 */
@Entity(tableName = "Post_Database")
data class Post(
    val userId: Int,
    @PrimaryKey(autoGenerate = true)
    var id: Int?,
    val title: String,
    val body: String
)