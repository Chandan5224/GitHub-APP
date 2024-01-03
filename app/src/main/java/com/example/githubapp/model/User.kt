package com.example.githubapp.model

data class User(
    val avatar_url: String,
    val followers_url: String,
    val following_url: String,
    val id: Int,
    val login: String,
    val type: String,
    val url: String
)