package com.example.bookreports.data.book.detailbook

data class Book(
    val author: String,
    val bookname: String,
    val category: String,
    val comments: List<Comment>,
    val created_at: String,
    val description: String,
    val id: Int,
    val image: String,
    val isbn: String,
    val publish_date: String,
    val publisher: String,
    val updated_at: String
)