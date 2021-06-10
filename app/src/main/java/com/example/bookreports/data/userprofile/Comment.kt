package com.example.bookreports.data.userprofile

data class Comment(
    val book: Book?,
    val bookID: Int?,
    val comment: String?,
    val created_at: String?,
    val id: Int?,
    val rate: Float?,
    val updated_at: String?,
    val userID: Int?
)