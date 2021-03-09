package dev.decagon.godday.mvvm.network

import dev.decagon.godday.mvvm.model.Comment
import retrofit2.http.GET

/**
 * This is the interface/API for getting comments from the server
 */
interface CommentService {
    @GET("comments")
    suspend fun getComment(): List<Comment>

    object CommentServiceClient {
        val retrofitService: CommentService by lazy {
            ApiClients.retrofit.create(CommentService::class.java)
        }
    }
}