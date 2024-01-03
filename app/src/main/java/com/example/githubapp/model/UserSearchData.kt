package com.example.githubapp.model

data class UserSearchData(
    val incomplete_results: Boolean,
    val items: List<User>,
    val total_count: Int
)