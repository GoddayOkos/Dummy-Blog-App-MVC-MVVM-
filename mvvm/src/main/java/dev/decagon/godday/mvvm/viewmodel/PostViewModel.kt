package dev.decagon.godday.mvvm.viewmodel

import android.app.Activity
import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import dev.decagon.godday.mvvm.database.MainDatabase
import dev.decagon.godday.mvvm.model.Post
import dev.decagon.godday.mvvm.network.PostService
import dev.decagon.godday.mvvm.repository.DataRepository
import kotlinx.coroutines.launch

/**
 * This view model is responsible for managing the data displayed by the MainActivity.
 * It interacts with the repository for its data
 */

class PostViewModel(application: Application) : AndroidViewModel(application) {

    // Initialize an instance of the repository
    private val dataRepository = DataRepository(MainDatabase.getInstance(application),null)


    // Get the list of post from the repository
    val posts = dataRepository.postList
    private val _filteredPost = MutableLiveData<List<Post>>()
    val filteredList = _filteredPost as LiveData<List<Post>>

    init {
        // Refreshes the list of post in the repository to be in sync with the server
        // using the helper method from the repository
        refreshPostFromRepository()
    }

    // Method to refresh the posts in the repository keeping it up to date with the server
    private fun refreshPostFromRepository() {
        viewModelScope.launch {
            dataRepository.refreshPost()
        }
    }

    // Method to filter post by userId
    fun loadPosts(userId: Int) {
        _filteredPost.value = posts.value!!.filter { it.userId == userId }
    }

    // Method to create a new post by inserting the post into the database
    // using the helper method from the repository
    fun createPost(post: Post) {
        viewModelScope.launch {
            dataRepository.insertPost(post)
        }
    }

    /**
     * Factory for constructing PostViewModel with parameter
     */
    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(PostViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return PostViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}
