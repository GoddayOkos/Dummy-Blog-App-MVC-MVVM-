package dev.decagon.godday.mvvm.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import dev.decagon.godday.mvvm.database.MainDatabase
import dev.decagon.godday.mvvm.model.Comment
import dev.decagon.godday.mvvm.repository.DataRepository
import kotlinx.coroutines.launch


/**
 * This view model is responsible for managing the data displayed by the CommentActivity activity.
 * It interacts with the repository for its data
 */
class CommentViewModel(application: Application, id: Int?) : AndroidViewModel(application) {

    // Initialize an instance of the repository
    private val dataRepository = DataRepository(MainDatabase.getInstance(application), id)

    // Get the list of comments from the repository
    val allComments = dataRepository.commentList

    init {
        // Refreshes the list of comments in the repository to be in sync with the server
        // using the helper method from the repository
        refreshCommentsFromData()
    }

    // Method to refresh the comments in the repository keeping it up to date with the server
    private fun refreshCommentsFromData() {
        viewModelScope.launch {
            dataRepository.refreshComment()
        }
    }

    // Method to create a new comment by inserting the post into the database
    // using the helper method from the repository
    fun createComment(comment: Comment) {
        viewModelScope.launch {
            dataRepository.insertComment(comment)
        }
    }


    /**
     * Factory for constructing CommentViewModel with parameter
     */
    class Factory(val app: Application, val id: Int?) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CommentViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return CommentViewModel(app, id) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}