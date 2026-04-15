package com.curator.data

class BooksRepository(private val apiService: BooksApiService = RetrofitClient.booksApi) {
    suspend fun searchBooks(query: String) = apiService.searchBooks(query)
    suspend fun getBook(id: String) = apiService.getBook(id)
}
