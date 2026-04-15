package com.curator.ui

import com.curator.data.BookItem
import com.curator.data.BooksRepository
import com.curator.data.VolumeInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BookDetailViewModelTest {
    private val testDispatcher = StandardTestDispatcher()

    private val mockRepository = object : BooksRepository(object : com.curator.data.BooksApiService {
        override suspend fun searchBooks(query: String, apiKey: String?) = throw Exception()
        override suspend fun getBook(id: String, apiKey: String?) = BookItem(id, VolumeInfo("Title", null, null, null, null, null, null, null, null, null))
    }) {
        override suspend fun getBook(id: String) = BookItem(id, VolumeInfo("Title", null, null, null, null, null, null, null, null, null))
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
    fun `loadBook updates state to Success`() = runTest {
        val viewModel = BookDetailViewModel(mockRepository)
        viewModel.loadBook("123")

        assertTrue(viewModel.uiState.value is BookDetailUiState.Loading)

        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state is BookDetailUiState.Success)
        assertEquals("123", (state as BookDetailUiState.Success).book.id)
    }

    @Test
    fun `loadBook with error updates state to Error`() = runTest {
        val errorRepo = object : BooksRepository(object : com.curator.data.BooksApiService {
            override suspend fun searchBooks(query: String, apiKey: String?) = throw Exception()
            override suspend fun getBook(id: String, apiKey: String?) = throw Exception("Detail Error")
        }) {
            override suspend fun getBook(id: String) = throw Exception("Detail Error")
        }

        val viewModel = BookDetailViewModel(errorRepo)
        viewModel.loadBook("123")

        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state is BookDetailUiState.Error)
        assertEquals("Detail Error", (state as BookDetailUiState.Error).message)
    }
}
