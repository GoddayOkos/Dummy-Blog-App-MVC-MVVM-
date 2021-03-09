package dev.decagon.godday.mvc.network

import dev.decagon.godday.mvc.model.Post
import retrofit2.Call
import retrofit2.http.*
import rx.Observable


/**
 * This is the interface/API for getting posts from the server
 */
interface PostService {
    @GET("posts")
    suspend fun getPosts(): List<Post>

    object PostServiceClient {
        val retrofitService: PostService by lazy {
            ApiClients.retrofit.create(PostService::class.java)
        }
    }
}