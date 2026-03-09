package com.sleepstory.app.ui.screens.discover

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.sleepstory.app.data.model.Story
import com.sleepstory.app.data.model.StoryCategory
import com.sleepstory.app.ui.components.*
import com.sleepstory.app.ui.navigation.Screen
import com.sleepstory.app.ui.theme.*

@Composable
fun DiscoverScreen(
    navController: NavController
) {
    var searchQuery by remember { mutableStateOf("") }

    val hotTags = listOf("#雨声助眠", "#森林漫步", "#星空幻想", "#海浪轻拍", "#冥想引导", "#古风故事")

    val categories = listOf(
        Triple("🌲", "自然之声", "128个故事"),
        Triple("🏰", "奇幻世界", "96个故事"),
        Triple("🧘", "冥想疗愈", "84个故事"),
        Triple("📚", "经典文学", "156个故事")
    )

    val topStories = remember {
        listOf(
            Story(
                id = "1",
                title = "深海的呢喃",
                description = "潜入深海，聆听海洋的心跳...",
                category = StoryCategory.NATURE,
                duration = 25,
                icon = "🌊",
                gradientColors = listOf("#A78BFA", "#EC4899"),
                rating = 4.9f,
                playCount = 23000
            ),
            Story(
                id = "2",
                title = "月光森林",
                description = "在古老的森林深处...",
                category = StoryCategory.FANTASY,
                duration = 18,
                icon = "🌲",
                gradientColors = listOf("#4ADE80", "#14B8A6"),
                rating = 4.8f,
                playCount = 18000
            ),
            Story(
                id = "3",
                title = "云端小屋",
                description = "一座漂浮在云端的小屋...",
                category = StoryCategory.WARM,
                duration = 32,
                icon = "☁️",
                gradientColors = listOf("#FB923C", "#EF4444"),
                rating = 4.9f,
                playCount = 15000
            )
        )
    }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                currentRoute = "discover",
                onNavigate = { route ->
                    when (route) {
                        "home" -> navController.navigate(Screen.Home.route)
                        "discover" -> { /* Already on discover */ }
                        "generate" -> navController.navigate(Screen.Generate.route)
                        "profile" -> navController.navigate(Screen.Profile.route)
                    }
                }
            )
        },
        containerColor = BackgroundDark
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Search bar
            item {
                GlassCard {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = TextMuted,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        BasicTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            modifier = Modifier.weight(1f),
                            decorationBox = { innerTextField ->
                                if (searchQuery.isEmpty()) {
                                    Text(
                                        text = "搜索故事、主题、场景...",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = TextMuted
                                    )
                                }
                                innerTextField()
                            }
                        )
                    }
                }
            }

            // Hot tags
            item {
                Column {
                    Text(
                        text = "热门标签",
                        style = MaterialTheme.typography.titleLarge,
                        color = TextPrimary,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        hotTags.forEachIndexed { index, tag ->
                            val isFirst = index == 0
                            TagChip(
                                text = tag,
                                highlighted = isFirst,
                                onClick = { /* Filter by tag */ }
                            )
                        }
                    }
                }
            }

            // Categories
            item {
                Column {
                    Text(
                        text = "分类浏览",
                        style = MaterialTheme.typography.titleLarge,
                        color = TextPrimary,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        categories.take(2).forEach { (icon, title, count) ->
                            CategoryCard(
                                icon = icon,
                                title = title,
                                count = count,
                                modifier = Modifier.weight(1f),
                                onClick = { /* Navigate to category */ }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        categories.drop(2).forEach { (icon, title, count) ->
                            CategoryCard(
                                icon = icon,
                                title = title,
                                count = count,
                                modifier = Modifier.weight(1f),
                                onClick = { /* Navigate to category */ }
                            )
                        }
                    }
                }
            }

            // Top stories
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "本周热门",
                        style = MaterialTheme.typography.titleLarge,
                        color = TextPrimary
                    )
                    TextButton(onClick = { /* View all */ }) {
                        Text("查看全部", color = PurpleLight)
                    }
                }
            }

            items(topStories) { story ->
                TopStoryItem(
                    rank = topStories.indexOf(story) + 1,
                    story = story,
                    onClick = {
                        navController.navigate(Screen.Player.createRoute(story.id))
                    }
                )
            }
        }
    }
}

@Composable
private fun TagChip(
    text: String,
    highlighted: Boolean = false,
    onClick: () -> Unit
) {
    val backgroundColor = if (highlighted) {
        PurplePrimary.copy(alpha = 0.2f)
    } else {
        Color.White.copy(alpha = 0.05f)
    }

    val borderColor = if (highlighted) {
        PurplePrimary.copy(alpha = 0.5f)
    } else {
        Color.White.copy(alpha = 0.1f)
    }

    val textColor = if (highlighted) PurpleLight else TextSecondary

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50.dp))
            .background(backgroundColor)
            .border(1.dp, borderColor, RoundedCornerShape(50.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = textColor
        )
    }
}

@Composable
private fun CategoryCard(
    icon: String,
    title: String,
    count: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    GlassCard(
        modifier = modifier.clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = icon, fontSize = 32.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = TextPrimary
            )
            Text(
                text = count,
                style = MaterialTheme.typography.bodySmall,
                color = TextMuted
            )
        }
    }
}

@Composable
private fun TopStoryItem(
    rank: Int,
    story: Story,
    onClick: () -> Unit
) {
    val rankColor = when (rank) {
        1 -> Color(0xFFFFD700) // Gold
        2 -> Color(0xFFC0C0C0) // Silver
        3 -> Color(0xFFCD7F32) // Bronze
        else -> TextMuted
    }

    GlassCard(
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Rank
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(rankColor, rankColor.copy(alpha = 0.5f))
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = rank.toString(),
                    style = MaterialTheme.typography.labelMedium,
                    color = if (rank <= 3) Color.White else TextPrimary
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Icon
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        brush = Brush.linearGradient(
                            colors = story.gradientColors.map {
                                Color(android.graphics.Color.parseColor(it))
                            }
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(text = story.icon, fontSize = 24.sp)
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Info
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = story.title,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextPrimary
                )
                Text(
                    text = "${story.category.name} · ${story.duration}分钟",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextMuted
                )
            }

            // Play count
            Text(
                text = "🔥 ${story.playCount / 1000}万",
                style = MaterialTheme.typography.labelSmall,
                color = Color(0xFFFFD700)
            )
        }
    }
}

@Composable
private fun FlowRow(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    content: @Composable () -> Unit
) {
    Layout(
        content = content,
        modifier = modifier
    ) { measurables, constraints ->
        val hGapPx = 8.dp.roundToPx()
        val vGapPx = 8.dp.roundToPx()

        val rows = mutableListOf<List<androidx.compose.ui.layout.Placeable>>()
        val rowWidths = mutableListOf<Int>()
        val rowHeights = mutableListOf<Int>()

        var currentRow = mutableListOf<androidx.compose.ui.layout.Placeable>()
        var currentRowWidth = 0
        var currentRowHeight = 0

        measurables.forEach { measurable ->
            val placeable = measurable.measure(constraints)

            if (currentRow.isNotEmpty() &&
                currentRowWidth + hGapPx + placeable.width > constraints.maxWidth) {
                rows.add(currentRow)
                rowWidths.add(currentRowWidth)
                rowHeights.add(currentRowHeight)
                currentRow = mutableListOf()
                currentRowWidth = 0
                currentRowHeight = 0
            }

            currentRow.add(placeable)
            currentRowWidth += if (currentRow.size == 1) placeable.width else hGapPx + placeable.width
            currentRowHeight = maxOf(currentRowHeight, placeable.height)
        }

        if (currentRow.isNotEmpty()) {
            rows.add(currentRow)
            rowWidths.add(currentRowWidth)
            rowHeights.add(currentRowHeight)
        }

        val height = rowHeights.sum() + (rowHeights.size - 1).coerceAtLeast(0) * vGapPx

        layout(constraints.maxWidth, height) {
            var y = 0
            rows.forEachIndexed { rowIndex, row ->
                var x = when (horizontalArrangement) {
                    Arrangement.Start -> 0
                    Arrangement.End -> constraints.maxWidth - rowWidths[rowIndex]
                    Arrangement.Center -> (constraints.maxWidth - rowWidths[rowIndex]) / 2
                    else -> 0
                }

                row.forEach { placeable ->
                    placeable.placeRelative(x, y)
                    x += placeable.width + hGapPx
                }

                y += rowHeights[rowIndex] + vGapPx
            }
        }
    }
}