package com.curator.data

data class Book(
    val id: String,
    val title: String,
    val author: String,
    val coverUrl: String,
    val progress: String? = null,
    val description: String = "",
    val rating: Float = 0f,
    val reviewCount: String = "",
    val genre: String = "",
    val pages: Int = 0,
    val readTime: String = "",
    val language: String = "EN",
    val format: String = "Hard"
)

data class Note(
    val id: String,
    val authorName: String,
    val authorAvatarUrl: String,
    val date: String,
    val content: String,
    val likes: Int
)
