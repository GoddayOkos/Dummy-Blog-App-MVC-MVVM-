package dev.decagon.godday.mvc.ui

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputLayout
import dev.decagon.godday.mvc.R
import dev.decagon.godday.mvc.adapter.PostAdapter
import dev.decagon.godday.mvc.controller.Controller
import dev.decagon.godday.mvc.model.Post
import kotlinx.coroutines.cancel


/**
 * This class represent the first screen in the application. It displays a list of post
 * with the post id, title and body using recycler view. Clicking on a post takes the
 * user to the post detail page where they can view all the comments on the post as well
 * as add new comments to the post. This application follows the MVVM architecture design pattern.
 */
class MainActivity : AppCompatActivity() {
    private lateinit var controller: Controller
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PostAdapter
    private lateinit var fab: FloatingActionButton
    private lateinit var toolbar: Toolbar
    private lateinit var textField: TextInputLayout
    private lateinit var dialogBuilder: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize an instance of the controller and refresh the data in the repository
        controller = Controller(this.application, null)
        controller.refreshPostFromRepository()

        // Setup up the custom toolbar and provide a name for the activity
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        title = "Posts - MVC"

        // Initialize and implement the search field for filtering posts based on
        // user id and return all post if invalid inputs are provided and notify the
        // the user with a toast
        textField = findViewById(R.id.text_field)
        textField.setEndIconOnClickListener {
            val input = textField.editText!!.text.toString()
            if (input == "" || input == "0") {
                textField.editText!!.text.clear()
                adapter.loadData(controller.posts.value!!)
                Toast.makeText(this, "Invalid input", Toast.LENGTH_LONG).show()
                return@setEndIconOnClickListener
            }
            val userId = input.toInt()
            controller.loadPosts(userId)
            textField.editText!!.text.clear()
        }

        /*
            Initialize and setup the recyclerview adapter, implement the click listener
            which it takes as a parameter, define the logic for navigating to the next
            activity and pass in the necessary data needed for the next activity
         */
        recyclerView = findViewById(R.id.recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = PostAdapter(PostAdapter.OnClickListener {
            val intent = CommentsActivity.newIntent(this, it.title, it.body, it.id!!)
            startActivity(intent)
        })

        // Observe the live data by way of the controller and assign it to the recyclerview
        // adapter and stop the progress bar from loading
        controller.posts.observe(this, {
            findViewById<ProgressBar>(R.id.loader).visibility = View.GONE
            adapter.loadData(it)
            recyclerView.adapter = adapter
        })

        // Observe the filtered used in filtering post base on user id
        // and let recyclerview displayed it when ever there is a change in the list
        controller.filteredList.observe(this, {
            adapter.loadData(it)
        })

        // Setup the floating action button
        fab = findViewById(R.id.fab)
        fab.setOnClickListener {
            customDialogBuilder()
        }
    }

    // Close the coroutine opened by the controller
    override fun onDestroy() {
        super.onDestroy()
        controller.scope.cancel()
    }

    /*
        This method defines the action that occurs when the floating action button is clicked.
        It also validates the inputs and handles the logic for creating new post using the
        helper method provided by the controller
     */
    private fun customDialogBuilder() {
        dialogBuilder = MaterialAlertDialogBuilder(this)
            .setTitle("Create a new post")
            .setView(R.layout.dialog_layout)
            .setCancelable(false)
            .setNegativeButton("CANCEL") { _, _ -> }
            .setPositiveButton("DONE") { _, _ ->
                val id: TextInputLayout = dialogBuilder.findViewById(R.id.user_id)!!
                val titleTemp: TextInputLayout = dialogBuilder.findViewById(R.id.title)!!
                val bodyTemp: TextInputLayout = dialogBuilder.findViewById(R.id.body)!!
                val userId = try {
                    id.editText!!.text.toString().toInt()
                } catch (e: NumberFormatException) {
                    0
                }
                val title = titleTemp.editText!!.text.toString().trim()
                val body = bodyTemp.editText!!.text.toString().trim()

                if (userId !in 1..10 || title.isEmpty() || body.isEmpty()) {
                    Toast.makeText(this, "Invalid inputs!", Toast.LENGTH_LONG).show()
                    return@setPositiveButton
                }
                val post = Post(userId, null, title, body)
                controller.createPost(post)
                Toast.makeText(this, "Post created!", Toast.LENGTH_LONG).show()
            }
            .show()
    }

}