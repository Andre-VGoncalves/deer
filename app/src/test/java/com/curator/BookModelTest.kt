package com.curator

import com.curator.data.Book
import org.junit.Test
import org.junit.Assert.*

class BookModelTest {
    @Test
    fun testBookInstantiation() {
        val book = Book(
            id = "test-1",
            title = "Test Book",
            author = "Test Author",
            coverUrl = "http://example.com/cover.png",
            progress = "10% Done",
            pages = 100
        )

        assertEquals("test-1", book.id)
        assertEquals("Test Book", book.title)
        assertEquals("Test Author", book.author)
        assertEquals("10% Done", book.progress)
        assertEquals(100, book.pages)
    }
}
