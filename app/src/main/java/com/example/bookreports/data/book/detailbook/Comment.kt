package com.example.bookreports.data.book.detailbook

data class Comment(
    val bookID: Int,
    val comment: String?,
    val created_at: String,
    val id: Int,
    val rate: Float,
    val updated_at: String,
    val user: User,
    val userID: Int
)