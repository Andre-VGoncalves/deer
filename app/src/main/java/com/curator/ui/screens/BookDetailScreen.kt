package com.curator.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.curator.data.Book
import com.curator.data.Note

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookDetailScreen(
    bookId: String?,
    onBackClick: () -> Unit
) {
    // In a real app, we would fetch the book by ID
    val book = remember {
        Book(
            id = "1",
            title = "The Whispering Library",
            author = "Evelyn Thorne",
            coverUrl = "",
            description = "In a forgotten corner of Victorian London, a librarian discovers a collection of books that breathe when opened. A mesmerizing tale of secrets, archival magic, and the quiet power of the written word. Thorne crafts a world so tactile you can almost smell the old parchment and beeswax.",
            rating = 4.8f,
            reviewCount = "2.4k reviews",
            genre = "Historical Fiction",
            pages = 412,
            readTime = "12h",
            language = "EN",
            format = "Hard"
        )
    }

    val notes = remember {
        listOf(
            Note("1", "Julian Marcus", "", "2 days ago", "The prose in chapter 4 is absolutely breathtaking. Thorne has this way of describing inanimate objects as if they possess souls.", 24),
            Note("2", "Sarah Chen", "", "1 week ago", "Does anyone else feel like Clara’s decision at the end of Part II was a bit out of character?", 12)
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    TextButton(onClick = onBackClick) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("BACK", style = MaterialTheme.typography.labelSmall)
                        }
                    }
                },
                actions = {
                    IconButton(onClick = {}) { Icon(Icons.Default.Share, contentDescription = "Share") }
                    IconButton(onClick = {}) { Icon(Icons.Default.MoreVert, contentDescription = "More") }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            // Hero Section
            Row(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // Tilted Book Cover
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(3f / 4f)
                        .rotate(-2f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .shadow(elevation = 16.dp)
                )

                // Book Info
                Column(modifier = Modifier.weight(1.2f)) {
                    Surface(
                        color = MaterialTheme.colorScheme.tertiaryContainer,
                        shape = CircleShape
                    ) {
                        Text(
                            "BEST SELLER",
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = book.title, style = MaterialTheme.typography.headlineLarge)
                    Text(
                        text = "by ${book.author}",
                        style = MaterialTheme.typography.titleMedium,
                        fontStyle = FontStyle.Italic,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Rating and Genre
            Row(
                modifier = Modifier.padding(horizontal = 24.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    repeat(4) { Icon(Icons.Default.Star, contentDescription = null, tint = MaterialTheme.colorScheme.tertiary, modifier = Modifier.size(16.dp)) }
                    Icon(Icons.Default.StarHalf, contentDescription = null, tint = MaterialTheme.colorScheme.tertiary, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("${book.rating} (${book.reviewCount})", style = MaterialTheme.typography.labelSmall)
                }
                Text("|", color = MaterialTheme.colorScheme.outlineVariant)
                Surface(
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        book.genre,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = book.description,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(horizontal = 24.dp),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Action Buttons
            Row(
                modifier = Modifier.padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = { },
                    modifier = Modifier.weight(1f).height(56.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("ADD TO LIBRARY")
                }
                Button(
                    onClick = { },
                    modifier = Modifier.weight(1f).height(56.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh, contentColor = MaterialTheme.colorScheme.onSurface)
                ) {
                    Icon(Icons.Default.Update, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("UPDATE STATUS")
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Bento Stats Grid
            Row(
                modifier = Modifier.padding(horizontal = 24.dp).fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                StatItem(label = "PAGES", value = book.pages.toString(), modifier = Modifier.weight(1f))
                StatItem(label = "READ TIME", value = book.readTime, modifier = Modifier.weight(1f))
                StatItem(label = "LANGUAGE", value = book.language, modifier = Modifier.weight(1f))
                StatItem(label = "FORMAT", value = book.format, modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(64.dp))

            // Reader Notes
            Row(
                modifier = Modifier.padding(horizontal = 24.dp).fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Reader Notes", style = MaterialTheme.typography.headlineSmall)
                    Text("Observations from the community", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                TextButton(onClick = {}) {
                    Text("VIEW ALL", color = MaterialTheme.colorScheme.primary)
                    Icon(Icons.Default.ChevronRight, contentDescription = null, modifier = Modifier.size(16.dp))
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Write Note
            Surface(
                modifier = Modifier.padding(horizontal = 24.dp).fillMaxWidth(),
                color = MaterialTheme.colorScheme.surfaceContainerLowest,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.15f)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(modifier = Modifier.padding(16.dp), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Surface(modifier = Modifier.size(40.dp), shape = CircleShape, color = MaterialTheme.colorScheme.primaryContainer) {
                        Box(contentAlignment = Alignment.Center) { Text("JD", fontWeight = FontWeight.Bold) }
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Leave a note or reflection...", color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f))
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Row {
                                Icon(Icons.Default.FormatItalic, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                                Spacer(modifier = Modifier.width(8.dp))
                                Icon(Icons.Default.Link, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            Button(onClick = {}, shape = RoundedCornerShape(32.dp)) {
                                Text("POST NOTE", style = MaterialTheme.typography.labelSmall)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Notes List
            notes.forEach { note ->
                NoteItem(note = note)
                Spacer(modifier = Modifier.height(32.dp))
            }

            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}

@Composable
fun StatItem(label: String, value: String, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.height(8.dp))
            Text(value, style = MaterialTheme.typography.titleMedium)
        }
    }
}

@Composable
fun NoteItem(note: Note) {
    Row(modifier = Modifier.padding(horizontal = 24.dp), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        Box(modifier = Modifier.size(48.dp).clip(RoundedCornerShape(8.dp)).background(MaterialTheme.colorScheme.surfaceVariant))
        Column(modifier = Modifier.weight(1f)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(note.authorName, fontWeight = FontWeight.Bold)
                Text(note.date, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(note.content, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.ThumbUp, contentDescription = null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(note.likes.toString(), style = MaterialTheme.typography.labelSmall)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Reply, contentDescription = null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("REPLY", style = MaterialTheme.typography.labelSmall)
                }
            }
        }
    }
}
