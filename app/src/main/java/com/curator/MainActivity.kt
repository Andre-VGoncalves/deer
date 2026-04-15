package com.curator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.curator.ui.screens.BookDetailScreen
import com.curator.ui.screens.HomeScreen
import com.curator.ui.screens.LibraryScreen
import com.curator.ui.screens.SearchResultsScreen
import com.curator.ui.theme.CuratorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CuratorTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CuratorApp()
                }
            }
        }
    }
}

@Composable
fun CuratorApp() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(
                onBookClick = { book ->
                    // For static books on home screen, we use their IDs
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
                    navController.navigate("searchResults/$query")
                }
            )
        }
        composable(
            route = "searchResults/{query}",
            arguments = listOf(navArgument("query") { type = NavType.StringType })
        ) { backStackEntry ->
            val query = backStackEntry.arguments?.getString("query") ?: ""
            SearchResultsScreen(
                query = query,
                onBackClick = { navController.popBackStack() },
                onBookClick = { bookItem ->
                    navController.navigate("bookDetail/${bookItem.id}")
                }
            )
        }
        composable(
            route = "bookDetail/{bookId}",
            arguments = listOf(navArgument("bookId") { type = NavType.StringType })
        ) { backStackEntry ->
            val bookId = backStackEntry.arguments?.getString("bookId")
            BookDetailScreen(
                bookId = bookId,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}
