package com.curator.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CollectionsBookmark
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.curator.R
import com.curator.data.Book
import com.curator.ui.components.CuratorBottomNavBar
import com.curator.ui.components.CuratorTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onBookClick: (Book) -> Unit,
    onLibraryClick: () -> Unit
) {
    val currentlyReading = remember {
        listOf(
            Book("101", "The Architecture of Light", "Elena Rossi", "", "64%"),
            Book("102", "History of the Silent Age", "Julian Barnes", "", "28%"),
            Book("103", "Botanical Narratives", "S. K. Weaver", "", "91%")
        )
    }

    Scaffold(
        topBar = { CuratorTopAppBar() },
        bottomBar = {
            CuratorBottomNavBar(selectedItem = 0, onItemSelected = { if (it == 1) onLibraryClick() })
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White,
                shape = CircleShape
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            // Hero Header
            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    text = stringResource(R.string.good_evening),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    letterSpacing = 2.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = buildAnnotatedString {
                        append(stringResource(R.string.your_library_is))
                        withStyle(style = SpanStyle(fontStyle = FontStyle.Italic, color = MaterialTheme.colorScheme.primary)) {
                            append(stringResource(R.string.waiting))
                        }
                        append(".")
                    },
                    style = MaterialTheme.typography.headlineLarge,
                    lineHeight = 40.sp
                )
            }

            // Currently Reading
            SectionHeader(title = stringResource(R.string.currently_reading))

            LazyRow(
                contentPadding = PaddingValues(horizontal = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                modifier = Modifier.padding(bottom = 32.dp)
            ) {
                items(currentlyReading) { book ->
                    CurrentlyReadingCard(book = book, onClick = { onBookClick(book) })
                }
            }

            // Curated for You (Bento)
            SectionHeader(title = stringResource(R.string.curated_for_you))
            BentoSection()

            Spacer(modifier = Modifier.height(32.dp))

            // Recent Activity
            SectionHeader(title = stringResource(R.string.recent_activity))
            RecentActivityList()

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun SectionHeader(title: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        Text(text = title, style = MaterialTheme.typography.headlineSmall)
        Text(
            text = stringResource(R.string.view_all),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun CurrentlyReadingCard(book: Book, onClick: () -> Unit) {
    Column(modifier = Modifier.width(280.dp).clickable { onClick() }) {
        Box {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.5f) // Adjusted to look like 2/3 ratio in horizontal scroll
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            )

            // Progress Indicator
            Surface(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(x = (-16).dp, y = 16.dp),
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surfaceContainerLowest,
                shadowElevation = 4.dp,
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.15f))
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = book.progress ?: "0%",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold
                    )
                    LinearProgressIndicator(
                        progress = (book.progress?.replace("%", "")?.toFloatOrNull() ?: 0f) / 100f,
                        modifier = Modifier.width(64.dp).height(6.dp).clip(CircleShape),
                        color = MaterialTheme.colorScheme.tertiaryContainer,
                        trackColor = MaterialTheme.colorScheme.surfaceContainerHigh
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(32.dp))
        Text(text = book.title, style = MaterialTheme.typography.titleLarge)
        Text(text = book.author, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
fun BentoSection() {
    Column(modifier = Modifier.padding(horizontal = 24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        // Large item
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.secondaryContainer)
                .padding(24.dp)
        ) {
            Column(modifier = Modifier.fillMaxHeight(), verticalArrangement = Arrangement.SpaceBetween) {
                Column {
                    Text(
                        stringResource(R.string.weekend_special),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        letterSpacing = 1.sp
                    )
                    Text(
                        "The Art of Stillness",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
                Button(
                    onClick = {},
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text(stringResource(R.string.start_reading), style = MaterialTheme.typography.labelSmall)
                }
            }
            // Abstract background element
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .fillMaxHeight()
                    .fillMaxWidth(0.5f)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
            )
        }

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            BentoSmallItem(
                title = stringResource(R.string.new_arrivals),
                subtitle = "12 items",
                icon = Icons.Default.AutoAwesome,
                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                modifier = Modifier.weight(1f)
            )
            BentoSmallItem(
                title = stringResource(R.string.reading_lists),
                subtitle = "4 curated",
                icon = Icons.Default.CollectionsBookmark,
                containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                modifier = Modifier.weight(1f),
                showBorder = true
            )
        }
    }
}

@Composable
fun BentoSmallItem(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    containerColor: Color,
    modifier: Modifier = Modifier,
    showBorder: Boolean = false
) {
    Surface(
        modifier = modifier.aspectRatio(1f),
        color = containerColor,
        shape = RoundedCornerShape(12.dp),
        border = if (showBorder) androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.1f)) else null
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(32.dp))
            Column {
                Text(text = title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(text = subtitle, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Composable
fun RecentActivityList() {
    Column(modifier = Modifier.padding(horizontal = 24.dp), verticalArrangement = Arrangement.spacedBy(32.dp)) {
        RecentActivityItem(
            title = "The Midnight Library",
            action = "Finished Chapter 4",
            time = "2 hours ago"
        )
        RecentActivityItem(
            title = "History of the Silent Age",
            action = "Added Bookmark",
            time = "Yesterday"
        )
    }
}

@Composable
fun RecentActivityItem(title: String, action: String, time: String) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(24.dp)) {
        Box(
            modifier = Modifier
                .size(64.dp, 80.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(MaterialTheme.colorScheme.surfaceContainer)
        )
        Column {
            Text(
                text = action.uppercase(),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            Text(text = title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(text = time, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
