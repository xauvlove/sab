package com.sleepstory.app.ui.screens.home

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
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.sleepstory.app.data.model.Story
import com.sleepstory.app.data.model.StoryCategory
import com.sleepstory.app.ui.components.*
import com.sleepstory.app.ui.navigation.Screen
import com.sleepstory.app.ui.theme.*

@Composable
fun HomeScreen(
    navController: NavController
) {
    val scrollState = rememberScrollState()
    var selectedCategory by remember { mutableStateOf("推荐") }

    val categories = listOf("推荐", "自然", "奇幻", "冥想", "科幻", "温暖")

    val recommendedStories = remember {
        listOf(
            Story(
                id = "1",
                title = "月光森林的守护者",
                description = "在古老的森林深处，有一位守护者...",
                category = StoryCategory.NATURE,
                duration = 18,
                icon = "🌲",
                gradientColors = listOf("#4ADE80", "#14B8A6"),
                rating = 4.9f,
                playCount = 2300
            ),
            Story(
                id = "2",
                title = "深海的呢喃",
                description = "潜入深海，聆听海洋的心跳...",
                category = StoryCategory.NATURE,
                duration = 25,
                icon = "🌊",
                gradientColors = listOf("#A78BFA", "#EC4899"),
                rating = 4.8f,
                playCount = 1800
            ),
            Story(
                id = "3",
                title = "云端之上的小屋",
                description = "一座漂浮在云端的小屋，等待着你...",
                category = StoryCategory.FANTASY,
                duration = 32,
                icon = "🏔️",
                gradientColors = listOf("#FB923C", "#EF4444"),
                rating = 4.9f,
                playCount = 1500
            )
        )
    }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                currentRoute = "home",
                onNavigate = { route ->
                    when (route) {
                        "home" -> { /* Already on home */ }
                        "discover" -> navController.navigate(Screen.Discover.route)
                        "generate" -> navController.navigate(Screen.Generate.route)
                        "profile" -> navController.navigate(Screen.Profile.route)
                    }
                }
            )
        },
        containerColor = BackgroundDark
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState)
        ) {
            // Header
            HeaderSection()

            Spacer(modifier = Modifier.height(24.dp))

            // Quick action card
            QuickActionCard(
                onClick = { navController.navigate(Screen.Generate.route) }
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 他人创作入口
            CommunityEntryCard(
                onClick = { navController.navigate(Screen.Community.route) }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Category tabs
            LazyRow(
                modifier = Modifier.padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(categories) { category ->
                    CategoryChip(
                        text = category,
                        selected = selectedCategory == category,
                        onClick = { selectedCategory = category }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Recommended stories
            Column(
                modifier = Modifier.padding(horizontal = 24.dp)
            ) {
                Text(
                    text = "今晚推荐",
                    style = MaterialTheme.typography.titleLarge,
                    color = TextPrimary
                )

                Spacer(modifier = Modifier.height(16.dp))

                recommendedStories.forEach { story ->
                    StoryCard(
                        title = story.title,
                        description = story.description,
                        icon = story.icon,
                        gradientColors = story.gradientColors.map { Color(android.graphics.Color.parseColor(it)) },
                        duration = "${story.duration}分钟",
                        rating = story.rating.toString(),
                        onClick = {
                            navController.navigate(Screen.Player.createRoute(story.id))
                        },
                        onPlayClick = {
                            navController.navigate(Screen.Player.createRoute(story.id))
                        }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun HeaderSection() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "晚上好",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )
            Text(
                text = "今晚想听什么故事？",
                style = MaterialTheme.typography.headlineSmall,
                color = TextPrimary
            )
        }

        // Avatar
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(PurplePrimary, BlueAccent)
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "User",
                style = MaterialTheme.typography.labelMedium,
                color = Color.White
            )
        }
    }
}

@Composable
private fun QuickActionCard(
    onClick: () -> Unit
) {
    GlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(PurplePrimary.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.FlashOn,
                    contentDescription = null,
                    tint = PurpleLight,
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "快速入睡模式",
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "根据你的偏好智能生成",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
            }

            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(PurplePrimary, BlueAccent)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Play",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}