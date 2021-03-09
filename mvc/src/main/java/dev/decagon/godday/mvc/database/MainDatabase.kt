package dev.decagon.godday.mvc.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import dev.decagon.godday.mvc.model.Comment
import dev.decagon.godday.mvc.model.Post


/**
 * This is our MainDatabase class. The database consists of two tables/entities
 * which are used for storing posts and comments separately
 */
@Database(entities = [Comment::class, Post::class], version = 1, exportSchema = false)
abstract class MainDatabase : RoomDatabase() {

    abstract val commentDatabaseDao: CommentDatabaseDao
    abstract val postDatabaseDao: PostDatabaseDao

    companion object {

        @Volatile
        private var INSTANCE: MainDatabase? = null

        fun getInstance(context: Context): MainDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(context.applicationContext,
                        MainDatabase::class.java,
                        "post_comment_record")
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}