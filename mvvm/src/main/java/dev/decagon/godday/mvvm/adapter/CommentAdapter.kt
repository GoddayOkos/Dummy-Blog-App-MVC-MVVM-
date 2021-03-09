package dev.decagon.godday.mvvm.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import dev.decagon.godday.mvvm.R
import dev.decagon.godday.mvvm.model.Comment


/**
 * A recyclerView adapter class for holding and displaying comments on the screen
 */
class CommentAdapter : RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {
    private var data = mutableListOf<Comment>()

    class CommentViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        fun onBind(comment: Comment) {
            val commentPost: TextView = view.findViewById(R.id.label)
            commentPost.text = view.context.getString(R.string.comment_string, comment.name, comment.email, comment.body)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_view_items, parent, false)
        return CommentViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val comment = data[position]
        holder.onBind(comment)
    }

    override fun getItemCount(): Int = data.size

    fun loadData(comment: List<Comment>) {
        this.data = comment as MutableList<Comment>
        notifyDataSetChanged()
    }

}