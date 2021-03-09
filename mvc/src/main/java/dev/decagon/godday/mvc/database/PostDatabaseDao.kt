package dev.decagon.godday.mvc.database

import androidx.lifecycle.LiveData
import androidx.room.*
import dev.decagon.godday.mvc.model.Post


/**
 * DAO for interacting with the Post_Database table in the database
 */
@Dao
interface PostDatabaseDao {

    @Insert
    fun insert(post: Post)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll( posts: List<Post>)

    @Update
    fun update(post: Post)

    @Query("SELECT * from Post_Database WHERE userId = :key")
    fun get(key: Int): List<Post>?

    @Query("SELECT * FROM Post_Database ORDER BY id DESC LIMIT 1")
    fun getLatestPost(): Post

    @Query("DELETE FROM Post_Database")
    fun clear()

    @Query("Select * from Post_Database Order by id desc")
    fun getAllPosts(): LiveData<List<Post>>
}