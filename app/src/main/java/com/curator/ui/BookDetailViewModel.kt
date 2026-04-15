package com.curator.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.curator.data.BookItem
import com.curator.data.BooksRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class BookDetailUiState {
    object Loading : BookDetailUiState()
    data class Success(val book: BookItem) : BookDetailUiState()
    data class Error(val message: String) : BookDetailUiState()
}

class BookDetailViewModel(private val repository: BooksRepository = BooksRepository()) : ViewModel() {
    private val _uiState = MutableStateFlow<BookDetailUiState>(BookDetailUiState.Loading)
    val uiState: StateFlow<BookDetailUiState> = _uiState.asStateFlow()

    fun loadBook(bookId: String) {
        viewModelScope.launch {
            _uiState.value = BookDetailUiState.Loading
            try {
                val book = repository.getBook(bookId)
                _uiState.value = BookDetailUiState.Success(book)
            } catch (e: Exception) {
                _uiState.value = BookDetailUiState.Error(e.message ?: "Unknown Error")
            }
        }
    }
}
