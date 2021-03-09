package dev.decagon.godday.mvvm.model

import androidx.room.Entity
import androidx.room.PrimaryKey


/**
 * This class servers as a model for the comment objects received from the server.
 * It is also used as an entity/ table for the local database
 */
@Entity(tableName = "Comment_Database")
data class Comment(
    val postId: Int,
    @PrimaryKey(autoGenerate = true)
    var id: Int?,
    val name: String,
    val email: String,
    val body: String
)