package com.curator.ui

import com.curator.data.BookItem
import com.curator.data.BooksRepository
import com.curator.data.GoogleBooksResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModelTest {
    private val testDispatcher = StandardTestDispatcher()

    private val mockRepository = object : BooksRepository(object : com.curator.data.BooksApiService {
        override suspend fun searchBooks(query: String) = GoogleBooksResponse(items = emptyList())
        override suspend fun getBook(id: String): BookItem = throw Exception()
    }) {
        override suspend fun searchBooks(query: String) = GoogleBooksResponse(items = emptyList())
    }

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `searchBooks updates state to Success`() = runTest {
        val viewModel = SearchViewModel(mockRepository)
        viewModel.searchBooks("query")

        assertTrue(viewModel.uiState.value is SearchUiState.Loading)

        advanceUntilIdle()

        assertTrue(viewModel.uiState.value is SearchUiState.Success)
    }
}
