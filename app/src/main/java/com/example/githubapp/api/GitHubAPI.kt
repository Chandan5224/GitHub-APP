package com.example.githubapp.api

import com.example.githubapp.model.User
import com.example.githubapp.model.UserData
import com.example.githubapp.model.UserSearchData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GitHubAPI {

    @GET("search/users")
    suspend fun getUserBySearch(
        @Query("q") username: String
    ): Response<UserSearchData>

    @GET("users/{username}")
    suspend fun getUserByUsername(
        @Path("username") username: String
    ): Response<UserData>

    @GET("users/{username}/followers")
    suspend fun getUserFollowers(
        @Path("username") username: String
    ): Response<List<User>>

    @GET("users/{username}/following")
    suspend fun getUserFollowing(
        @Path("username") username: String
    ): Response<List<User>>

}