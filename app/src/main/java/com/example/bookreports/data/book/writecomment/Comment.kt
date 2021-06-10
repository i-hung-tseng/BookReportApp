package com.example.bookreports.data.book.writecomment

data class Comment(
    val bookID: String,
    val comment: Any,
    val created_at: String,
    val id: Int,
    val rate: String,
    val updated_at: String,
    val userID: Int
)