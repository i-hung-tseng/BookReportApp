package com.example.bookreports.data.book.writecomment.editcomment

data class editCommentSuccessful(
    val bookID: Int,
    val comment: String,
    val created_at: String,
    val id: Int,
    val rate: String,
    val updated_at: String,
    val userID: Int
)