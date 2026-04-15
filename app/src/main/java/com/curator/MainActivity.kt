package com.curator

import android.os.Bundle
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.curator.data.BooksApiService
import com.curator.data.BooksRepository
import com.curator.data.RetrofitProvider
import com.curator.ui.BookDetailViewModel
import com.curator.ui.SearchViewModel
import com.curator.ui.screens.BookDetailScreen
import com.curator.ui.screens.HomeScreen
import com.curator.ui.screens.LibraryScreen
import com.curator.ui.screens.SearchResultsScreen
import com.curator.ui.theme.CuratorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Manual DI for simplicity in this project
        val retrofitProvider = RetrofitProvider()
        val apiService = retrofitProvider.provideBooksApi()
        val repository = BooksRepository(apiService)

        setContent {
            CuratorTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CuratorApp(repository)
                }
            }
        }
    }
}

@Composable
fun CuratorApp(repository: BooksRepository) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(
                onBookClick = { book ->
                    navController.navigate("bookDetail/${book.id}")
                },
                onLibraryClick = {
                    navController.navigate("library")
                }
            )
        }
        composable("library") {
            LibraryScreen(
                onBookClick = { book ->
                    navController.navigate("bookDetail/${book.id}")
                },
                onHomeClick = {
                    navController.navigate("home")
                },
                onSearch = { query ->
                    val encodedQuery = Uri.encode(query)
                    navController.navigate("searchResults/$encodedQuery")
                }
            )
        }
        composable(
            route = "searchResults/{query}",
            arguments = listOf(navArgument("query") { type = NavType.StringType })
        ) { backStackEntry ->
            val query = backStackEntry.arguments?.getString("query") ?: ""
            val searchViewModel: SearchViewModel = viewModel(
                factory = remember { SearchViewModel.Factory(repository) }
            )
            SearchResultsScreen(
                query = query,
                onBackClick = { navController.popBackStack() },
                onBookClick = { bookItem ->
                    navController.navigate("bookDetail/${bookItem.id}")
                },
                viewModel = searchViewModel
            )
        }
        composable(
            route = "bookDetail/{bookId}",
            arguments = listOf(navArgument("bookId") { type = NavType.StringType })
        ) { backStackEntry ->
            val bookId = backStackEntry.arguments?.getString("bookId")
            val detailViewModel: BookDetailViewModel = viewModel(
                factory = remember { BookDetailViewModel.Factory(repository) }
            )
            BookDetailScreen(
                bookId = bookId,
                onBackClick = {
                    navController.popBackStack()
                },
                viewModel = detailViewModel
            )
        }
    }
}
