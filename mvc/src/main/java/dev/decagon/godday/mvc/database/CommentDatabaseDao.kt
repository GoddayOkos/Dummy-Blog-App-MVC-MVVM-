package dev.decagon.godday.mvc.database

import androidx.lifecycle.LiveData
import androidx.room.*
import dev.decagon.godday.mvc.model.Comment


/**
 * DAO for interacting with the Comment_Database table in the database
 */
@Dao
interface CommentDatabaseDao {

    @Insert
    fun insert(comment: Comment)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll( comments: List<Comment>)

    @Update
    fun update(comment: Comment)

    @Query("SELECT * from Comment_Database WHERE postId = :key")
    fun get(key: Int): List<Comment>?

    @Query("SELECT * FROM Comment_Database ORDER BY id DESC LIMIT 1")
    fun getLatestComment(): Comment

    @Query("DELETE FROM Comment_Database")
    fun clear()

    @Query("Select * from Comment_Database where postId = :key Order by id desc")
    fun getAllComments(key: Int?): LiveData<List<Comment>>
}