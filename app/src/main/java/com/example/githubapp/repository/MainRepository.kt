package com.example.githubapp.repository

import com.example.githubapp.api.RetrofitInstance


class MainRepository {
    suspend fun getUserBySearch(username: String) = RetrofitInstance.api.getUserBySearch(username)
    suspend fun getUserByUserName(username: String) = RetrofitInstance.api.getUserByUsername(username)
    suspend fun getUserFollowers(username: String) = RetrofitInstance.api.getUserFollowers(username)
    suspend fun getUserFollowing(username: String) = RetrofitInstance.api.getUserFollowing(username)
}