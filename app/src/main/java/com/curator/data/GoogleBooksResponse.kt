package com.curator.data

data class GoogleBooksResponse(
    val items: List<BookItem>?
)

data class BookItem(
    val id: String,
    val volumeInfo: VolumeInfo
)

data class VolumeInfo(
    val title: String,
    val authors: List<String>?,
    val description: String?,
    val imageLinks: ImageLinks?,
    val averageRating: Float?,
    val ratingsCount: Int?,
    val categories: List<String>?,
    val pageCount: Int?,
    val language: String?,
    val publishedDate: String?
)

data class ImageLinks(
    val smallThumbnail: String?,
    val thumbnail: String?
)
