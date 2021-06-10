package com.example.bookreports.data.book

data class BookItem(
        val author: String,
        val bookname: String,
        val category: String,
        val created_at: String,
        val description: String,
        val id: Int,
        val image: String,
        val isbn: String,
        val publish_date: String,
        val publisher: String,
        val updated_at: String
)