package com.example.githubapp.model

data class UserData(
    val avatar_url: String = "",
    val bio: String = "",
    val email: Any = "",
    val followers: Int = 0,
    val followers_url: String = "",
    val following: Int = 0,
    val following_url: String = "",
    val id: Int = 0,
    val location: String = "",
    val login: String = "",
    val name: String = "",
    val owned_private_repos: Int = 0,
    val public_repos: Int = 0,
    val repos_url: String = "",
    val total_private_repos: Int = 0,
    val type: String = "",
    val updated_at: String = "",
    val url: String = ""
)