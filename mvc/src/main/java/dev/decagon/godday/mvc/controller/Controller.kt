package dev.decagon.godday.mvc.controller

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dev.decagon.godday.mvc.database.MainDatabase
import dev.decagon.godday.mvc.model.Comment
import dev.decagon.godday.mvc.model.Post
import dev.decagon.godday.mvc.repository.DataRepository
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch


/**
 * This Controller is responsible for managing the data displayed by the Activities.
 * It interacts with the repository for its data
 */
class Controller(application: Application, id: Int?) {

    // Initialize an instance of the repository
    private val dataRepository = DataRepository(MainDatabase.getInstance(application), id)

    // Get the list of post from the repository
    val posts = dataRepository.postList

    // Get the list of comments from the repository
    val allComments = dataRepository.commentList
    private val _filteredPost = MutableLiveData<List<Post>>()
    val filteredList = _filteredPost as LiveData<List<Post>>
    private val TAG = "Controller"
    val scope = MainScope()            // create a coroutine scope

    // Method to refresh the posts in the repository keeping it up to date with the server
    fun refreshPostFromRepository() {
        scope.launch {
            try {
                dataRepository.refreshPost()
            } catch (e: Exception) {
                logIt("ERROR: ${e.message}")
            }
        }
    }

    // Method to filter post by userId
    fun loadPosts(userId: Int) {
        _filteredPost.value = posts.value!!.filter { it.userId == userId }
    }

    // Method to create a new post by inserting the post into the database
    // using the helper method from the repository
    fun createPost(post: Post) {
        scope.launch {
            try {
                dataRepository.insertPost(post)
            } catch (e: Exception) {
                logIt("Error: ${e.message}")
            }
        }
    }

    private fun logIt(msg: String) {
        Log.d(TAG, msg)
    }

    // Method to refresh the comments in the repository keeping it up to date with the server
    fun refreshCommentsFromData() {
        scope.launch {
            try {
                dataRepository.refreshComment()
            } catch (e: Exception) {
                logIt("Error: ${e.message}")
            }
        }
    }

    // Method to create a new comment by inserting the post into the database
    // using the helper method from the repository
    fun createComment(comment: Comment, postId: Int) {
        scope.launch {
            try {
                dataRepository.insertComment(comment)
            } catch (e: Exception) {
                logIt("Error: ${e.message}")
            }
        }
    }
}