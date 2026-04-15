package com.curator.data

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class BooksRepositoryTest {

    private val mockApiService = object : BooksApiService {
        override suspend fun searchBooks(query: String): GoogleBooksResponse {
            return GoogleBooksResponse(items = emptyList())
        }

        override suspend fun getBook(id: String): BookItem {
            return BookItem(id = id, volumeInfo = VolumeInfo("Test Title", null, null, null, null, null, null, null, null, null))
        }
    }

    private val repository = BooksRepository(mockApiService)

    @Test
    fun `searchBooks returns response from api`() = runBlocking {
        val result = repository.searchBooks("test")
        assertEquals(0, result.items?.size)
    }

    @Test
    fun `getBook returns book from api`() = runBlocking {
        val result = repository.getBook("123")
        assertEquals("123", result.id)
        assertEquals("Test Title", result.volumeInfo.title)
    }
}
