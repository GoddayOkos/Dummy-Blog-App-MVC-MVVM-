package dev.decagon.godday.mvc.network

import dev.decagon.godday.mvc.model.Comment
import retrofit2.Call
import retrofit2.http.*
import rx.Observable


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