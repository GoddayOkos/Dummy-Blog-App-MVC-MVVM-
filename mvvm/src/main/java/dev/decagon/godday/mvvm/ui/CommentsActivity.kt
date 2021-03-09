package dev.decagon.godday.mvvm.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputLayout
import dev.decagon.godday.mvvm.R
import dev.decagon.godday.mvvm.adapter.CommentAdapter
import dev.decagon.godday.mvvm.model.Comment
import dev.decagon.godday.mvvm.viewmodel.CommentViewModel

/**
 * This class represents the comment activity. It displays a post with a list
 * of all it comments and an interface for the users to add new comments to
 * the post.
 */

class CommentsActivity : AppCompatActivity() {
    private lateinit var viewModel: CommentViewModel
    private val adapter = CommentAdapter()
    private lateinit var titles: TextView
    private lateinit var post: TextView
    private lateinit var fab: FloatingActionButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var dialogBuilder: AlertDialog
    private var postId: Int = 0

    companion object {
        private const val TITLE = "title"
        private const val POST = "post"
        private const val ID = "postId"

        // Define the logic of navigating to this activity and a means to
        // get the needed data for the creation of the activity.
        fun newIntent(context: Context, title: String, post: String, id: Int): Intent {
            return Intent(context, CommentsActivity::class.java).also {
                it.putExtra(TITLE, title)
                it.putExtra(POST, post)
                it.putExtra(ID, id)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comments)

        postId = intent.getIntExtra(ID, 0)

        // Initialize the view model
        viewModel = ViewModelProvider(this, CommentViewModel.Factory(this.application, postId))
            .get(CommentViewModel::class.java)

        title = "Comments"       // Set the title of the activity

        // Initialize fields and get data passed on by the previous activity
        titles = findViewById(R.id.title2)
        titles.text = intent.getStringExtra(TITLE)
        post = findViewById(R.id.post2)
        post.text = intent.getStringExtra(POST)

        // Setup recyclerview
        recyclerView = findViewById(R.id.list_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        // Setup the floating action button
        fab = findViewById(R.id.fab)
        fab.setOnClickListener {
            customDialogBuilder()
        }

        // Observe the live data by way of view model and assign it to the recyclerview
        // adapter and stop the progress bar from loading
        viewModel.allComments.observe(this, {
            findViewById<ProgressBar>(R.id.loader2).visibility = View.GONE
            adapter.loadData(it)
            recyclerView.adapter = adapter
        })

    }

    /*
        This method defines the action that occurs when the floating action button is clicked.
        It also validates the inputs and handles the logic for creating new comments using the
        helper method provided by the view model
     */
    private fun customDialogBuilder() {
        dialogBuilder = MaterialAlertDialogBuilder(this)
            .setTitle("Add a new comment")
            .setView(R.layout.comment_dialog)
            .setCancelable(false)
            .setNegativeButton("CANCEL") { _, _ -> }
            .setPositiveButton("DONE") { _, _ ->
                val nameTemp: TextInputLayout = dialogBuilder.findViewById(R.id.name)!!
                val emailTemp: TextInputLayout = dialogBuilder.findViewById(R.id.email)!!
                val bodyTemp: TextInputLayout = dialogBuilder.findViewById(R.id.body)!!
                val name = nameTemp.editText!!.text.toString().trim()
                val email = emailTemp.editText!!.text.toString().trim()
                val body = bodyTemp.editText!!.text.toString().trim()

                if (name.isEmpty() || email.isEmpty() || body.isEmpty()) {
                    Toast.makeText(this, "Invalid inputs!", Toast.LENGTH_LONG).show()
                    return@setPositiveButton
                }
                val comment = Comment(postId,null, name, email, body)
                viewModel.createComment(comment)
                Toast.makeText(this, "Comment added!", Toast.LENGTH_LONG).show()
            }
            .show()
    }
}