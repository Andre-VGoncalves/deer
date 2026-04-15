package com.curator.data

open class BooksRepository(private val apiService: BooksApiService) {
    open suspend fun searchBooks(query: String) = apiService.searchBooks(query)
    open suspend fun getBook(id: String) = apiService.getBook(id)
}
