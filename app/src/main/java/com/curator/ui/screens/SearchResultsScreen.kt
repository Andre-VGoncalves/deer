package com.curator.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.curator.data.BookItem
import com.curator.ui.SearchUiState
import com.curator.ui.SearchViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchResultsScreen(
    query: String,
    onBackClick: () -> Unit,
    onBookClick: (BookItem) -> Unit,
    viewModel: SearchViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(query) {
        viewModel.searchBooks(query)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("The Curator", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = MaterialTheme.colorScheme.primary)
                    }
                },
                actions = {
                    IconButton(onClick = {}) { Icon(Icons.Default.Search, contentDescription = "Search", tint = MaterialTheme.colorScheme.primary) }
                }
            )
        }
    ) { padding ->
        when (val state = uiState) {
            is SearchUiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is SearchUiState.Error -> {
                Box(modifier = Modifier.fillMaxSize().padding(24.dp), contentAlignment = Alignment.Center) {
                    Text("Error: ${state.message}", color = MaterialTheme.colorScheme.error)
                }
            }
            is SearchUiState.Success -> {
                val searchResults = state.books
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 24.dp)
                ) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "Search Results",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        letterSpacing = 2.sp
                    )
                    Text(
                        "“$query”",
                        style = MaterialTheme.typography.displaySmall,
                        fontStyle = FontStyle.Italic,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "Found ${searchResults.size} matching titles across your curated shelves and world library.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(40.dp))

                    SectionDivider(title = "On Your Shelf", count = "2 Items")

                    Column(verticalArrangement = Arrangement.spacedBy(32.dp)) {
                        searchResults.take(2).forEachIndexed { index, book ->
                            ShelfBookCard(
                                book = book,
                                onClick = { onBookClick(book) },
                                modifier = if (index % 2 != 0) Modifier.padding(top = 32.dp) else Modifier
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(64.dp))

                    SectionDivider(title = "World Library", count = "Global Collection")

                    val remainingBooks = searchResults.drop(2)
                    val chunked = remainingBooks.chunked(2)

                    chunked.forEach { rowBooks ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(24.dp)
                        ) {
                            rowBooks.forEachIndexed { colIndex, book ->
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(top = if (colIndex % 2 != 0) 32.dp else 0.dp)
                                ) {
                                    WorldBookCard(book = book, onClick = { onBookClick(book) })
                                }
                            }
                            if (rowBooks.size < 2) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                        Spacer(modifier = Modifier.height(24.dp))
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    if (searchResults.size > 4) {
                        FeaturedChoice(book = searchResults[4])
                    }

                    Spacer(modifier = Modifier.height(48.dp))
                }
            }
        }
    }
}

@Composable
fun SectionDivider(title: String, count: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.width(32.dp).height(1.dp).background(MaterialTheme.colorScheme.outlineVariant))
            Spacer(modifier = Modifier.width(12.dp))
            Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        }
        Surface(
            color = MaterialTheme.colorScheme.secondaryContainer,
            shape = RoundedCornerShape(32.dp)
        ) {
            Text(
                count,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
    }
}

@Composable
fun ShelfBookCard(book: BookItem, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        horizontalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Box(
            modifier = Modifier
                .width(160.dp)
                .aspectRatio(3f / 4f)
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            AsyncImage(
                model = book.volumeInfo.imageLinks?.thumbnail?.replace("http:", "https:"),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Surface(
                modifier = Modifier.align(Alignment.TopEnd).offset(x = 12.dp, y = (-12).dp),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.tertiaryContainer,
                shadowElevation = 8.dp
            ) {
                Icon(
                    Icons.Default.Bookmark,
                    contentDescription = null,
                    modifier = Modifier.padding(8.dp).size(16.dp),
                    tint = MaterialTheme.colorScheme.onTertiaryContainer
                )
            }
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(book.volumeInfo.title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text(
                "${book.volumeInfo.authors?.firstOrNull() ?: "Unknown"}, ${book.volumeInfo.publishedDate?.take(4) ?: ""}",
                style = MaterialTheme.typography.bodyMedium,
                fontStyle = FontStyle.Italic,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.weight(1f))
            Column {
                LinearProgressIndicator(
                    progress = { 0.65f },
                    modifier = Modifier.fillMaxWidth().height(4.dp).clip(CircleShape),
                    color = MaterialTheme.colorScheme.tertiaryContainer,
                    trackColor = MaterialTheme.colorScheme.surfaceContainerHigh
                )
                Text(
                    "65% PROGRESS",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 8.dp),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun WorldBookCard(book: BookItem, onClick: () -> Unit) {
    Column(modifier = Modifier.clickable { onClick() }) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(3f / 4f)
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            AsyncImage(
                model = book.volumeInfo.imageLinks?.thumbnail?.replace("http:", "https:"),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Brush.verticalGradient(listOf(Color.Transparent, Color.Black.copy(alpha = 0.4f)))),
                contentAlignment = Alignment.BottomCenter
            ) {
                Surface(
                    modifier = Modifier.padding(12.dp).fillMaxWidth(),
                    shape = RoundedCornerShape(32.dp),
                    color = MaterialTheme.colorScheme.surface
                ) {
                    Text(
                        "VIEW DETAILS",
                        modifier = Modifier.padding(vertical = 8.dp),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(book.volumeInfo.title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, maxLines = 1)
        Text(book.volumeInfo.authors?.firstOrNull() ?: "Unknown", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
fun FeaturedChoice(book: BookItem) {
    Surface(
        modifier = Modifier.padding(top = 32.dp).fillMaxWidth(),
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    "CURATOR'S CHOICE",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(book.volumeInfo.title, style = MaterialTheme.typography.headlineSmall, fontStyle = FontStyle.Italic, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    book.volumeInfo.description?.take(100) ?: "",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 3
                )
                Spacer(modifier = Modifier.height(24.dp))
                Button(onClick = {}, colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary), shape = RoundedCornerShape(8.dp)) {
                    Text("RESERVE TITLE")
                }
            }
            Box(
                modifier = Modifier
                    .weight(0.8f)
                    .aspectRatio(3f / 4f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
                 AsyncImage(
                    model = book.volumeInfo.imageLinks?.thumbnail?.replace("http:", "https:"),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}
