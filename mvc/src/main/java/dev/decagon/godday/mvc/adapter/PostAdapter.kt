package dev.decagon.godday.mvc.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import dev.decagon.godday.mvc.R
import dev.decagon.godday.mvc.model.Post


/**
 * A recyclerView adapter class for holding and displaying posts on the screen
 */
class PostAdapter(private val onClickListener: OnClickListener) :
    RecyclerView.Adapter<PostAdapter.PostViewHolder>() {
    private var data = mutableListOf<Post>()

    class PostViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

        fun onBind(post: Post) {
            val title: TextView = view.findViewById(R.id.title)
            title.text = post.title

            val userId: TextView = view.findViewById(R.id.user_id)
            userId.text = view.context.getString(R.string.user_id, post.userId, post.id)

            val postMessage: TextView = view.findViewById(R.id.post)
            postMessage.text = post.body
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_item, parent, false)
        return PostViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = data[position]
        holder.itemView.setOnClickListener { onClickListener.onClick(post) }
        holder.onBind(post)
    }

    override fun getItemCount(): Int = data.size

    fun loadData(post: List<Post>) {
        this.data = post as MutableList<Post>
        notifyDataSetChanged()
    }

    class OnClickListener(val clickListener: (post: Post) -> Unit) {
        fun onClick(post: Post) = clickListener(post)
    }
}