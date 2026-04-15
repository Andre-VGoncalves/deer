package com.curator

import com.curator.data.BookItem
import com.curator.data.GoogleBooksResponse
import com.curator.data.VolumeInfo
import com.google.gson.Gson
import org.junit.Test
import org.junit.Assert.*

class ApiTest {
    @Test
    fun testJsonMapping() {
        val json = """
            {
              "items": [
                {
                  "id": "123",
                  "volumeInfo": {
                    "title": "Test Book",
                    "authors": ["Author One"],
                    "description": "A description"
                  }
                }
              ]
            }
        """.trimIndent()

        val gson = Gson()
        val response = gson.fromJson(json, GoogleBooksResponse::class.java)

        assertNotNull(response.items)
        assertEquals(1, response.items!!.size)
        assertEquals("123", response.items!![0].id)
        assertEquals("Test Book", response.items!![0].volumeInfo.title)
        assertEquals("Author One", response.items!![0].volumeInfo.authors!![0])
    }
}
