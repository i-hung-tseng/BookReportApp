package com.example.bookreports.data.userprofile

data class User(
    val comments: List<Comment>?,
    val created_at: String?,
    val email: String?,
    val email_verified_at: Any?,
    val id: Int?,
    val image: String?,
    val name: String?,
    val point: Int?,
    val region: String?,
    val updated_at: String?
)