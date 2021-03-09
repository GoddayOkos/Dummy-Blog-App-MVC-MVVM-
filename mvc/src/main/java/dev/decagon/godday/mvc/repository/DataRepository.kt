package dev.decagon.godday.mvc.repository

import android.util.Log
import androidx.lifecycle.LiveData
import dev.decagon.godday.mvc.database.MainDatabase
import dev.decagon.godday.mvc.model.Comment
import dev.decagon.godday.mvc.model.Post
import dev.decagon.godday.mvc.network.CommentService
import dev.decagon.godday.mvc.network.PostService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


/**
 * This class holds all the logic for working with the different data sources available.
 * It pulls the data from the server and feeds them into the local database. It them pulls
 * the data from the local database and feeds them to the Controller.
 */
class DataRepository(private val database: MainDatabase, id: Int?) {

    // Get all the posts from the database
    val postList: LiveData<List<Post>> = database.postDatabaseDao.getAllPosts()

    // Get all the comments from the database
    val commentList: LiveData<List<Comment>> = database.commentDatabaseDao.getAllComments(id)

    private val TAG = "DataRepository"

    // This method gets the posts from the server and insert them into the database
    suspend fun refreshPost() {
        withContext(Dispatchers.IO) {
            try {
                val posts = PostService.PostServiceClient.retrofitService.getPosts()
                database.postDatabaseDao.insertAll(posts)
            } catch (e: Exception) {
                logIt("${e.message}")
            }

        }
    }

    // This method gets the comments from the server and insert them into the database
    suspend fun refreshComment() {
        withContext(Dispatchers.IO) {
            try {
                val comments = CommentService.CommentServiceClient.retrofitService.getComment()
                database.commentDatabaseDao.insertAll(comments)
            } catch (e: Exception) {
                logIt("${e.message}")
            }
        }
    }

    // This method is used to insert a post into the database
    suspend fun insertPost(post: Post) {
        withContext(Dispatchers.IO) {
            database.postDatabaseDao.insert(post)
        }
    }

    // This method is used to insert a comment into the database
    suspend fun insertComment(comment: Comment) {
        withContext(Dispatchers.IO) {
            database.commentDatabaseDao.insert(comment)
        }
    }

    private fun logIt(msg: String) {
        Log.d(TAG, msg)
    }
}