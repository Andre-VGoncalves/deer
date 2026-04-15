package com.curator.data

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitProvider(
    private val baseUrl: String = "https://www.googleapis.com/books/v1/",
    private val httpClient: OkHttpClient = OkHttpClient()
) {
    fun provideBooksApi(): BooksApiService =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BooksApiService::class.java)
}
