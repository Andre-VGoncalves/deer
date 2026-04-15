package com.curator.data

open class BooksRepository(
    private val apiService: BooksApiService,
    private val apiKey: String? = null
) {
    open suspend fun searchBooks(query: String) = apiService.searchBooks(query, apiKey)
    open suspend fun getBook(id: String) = apiService.getBook(id, apiKey)
}
