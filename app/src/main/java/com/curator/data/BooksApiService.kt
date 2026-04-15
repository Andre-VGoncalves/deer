package com.curator.data

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface BooksApiService {
    @GET("volumes")
    suspend fun searchBooks(
        @Query("q") query: String
    ): GoogleBooksResponse

    @GET("volumes/{id}")
    suspend fun getBook(
        @Path("id") id: String
    ): BookItem
}
