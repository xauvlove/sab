package com.sleepstory.app.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.sleepstory.app.ui.components.GlassCard
import com.sleepstory.app.ui.components.BottomNavigationBar
import com.sleepstory.app.ui.navigation.Screen
import com.sleepstory.app.ui.theme.*

@Composable
fun ProfileScreen(
    navController: NavController
) {
    val scrollState = rememberScrollState()
    var sleepReminderEnabled by remember { mutableStateOf(true) }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                currentRoute = "profile",
                onNavigate = { route ->
                    when (route) {
                        "home" -> navController.navigate(Screen.Home.route)
                        "discover" -> navController.navigate(Screen.Discover.route)
                        "generate" -> navController.navigate(Screen.Generate.route)
                        "profile" -> { /* Already on profile */ }
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
            // User info section
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Avatar
                    Box(
                        modifier = Modifier
                            .size(64.dp)
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
                            style = MaterialTheme.typography.labelLarge,
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "晚安旅人",
                            style = MaterialTheme.typography.headlineSmall,
                            color = TextPrimary
                        )
                        Text(
                            text = "已陪伴入睡 28 天",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary
                        )
                    }

                    IconButton(onClick = { /* Settings */ }) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings",
                            tint = TextSecondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Stats card
                GlassCard {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "睡眠统计",
                                style = MaterialTheme.typography.titleMedium,
                                color = TextPrimary
                            )
                            TextButton(onClick = { }) {
                                Text("本周", color = PurpleLight)
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            StatItem(
                                value = "6.5",
                                label = "平均睡眠(小时)",
                                color = PurpleLight
                            )
                            StatItem(
                                value = "18",
                                label = "听完故事",
                                color = BlueLight
                            )
                            StatItem(
                                value = "85%",
                                label = "入睡成功率",
                                color = SuccessGreen
                            )
                        }
                    }
                }
            }

            // Menu items
            Column(
                modifier = Modifier.padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Sleep reminder toggle
                MenuItemWithToggle(
                    icon = Icons.Default.Timer,
                    iconBackgroundColor = PurplePrimary.copy(alpha = 0.2f),
                    iconTint = PurpleLight,
                    title = "睡眠提醒",
                    checked = sleepReminderEnabled,
                    onCheckedChange = { sleepReminderEnabled = it }
                )

                // Favorites
                MenuItem(
                    icon = Icons.Default.Favorite,
                    iconBackgroundColor = BlueAccent.copy(alpha = 0.2f),
                    iconTint = BlueLight,
                    title = "我的收藏",
                    badge = "12",
                    onClick = { /* Navigate to favorites */ }
                )

                // Sleep report
                MenuItem(
                    icon = Icons.Default.BarChart,
                    iconBackgroundColor = SuccessGreen.copy(alpha = 0.2f),
                    iconTint = SuccessGreen,
                    title = "睡眠报告",
                    onClick = { /* Navigate to sleep report */ }
                )

                // Preferences
                MenuItem(
                    icon = Icons.Default.Tune,
                    iconBackgroundColor = WarningYellow.copy(alpha = 0.2f),
                    iconTint = WarningYellow,
                    title = "偏好设置",
                    onClick = { /* Navigate to preferences */ }
                )

                // Feedback
                MenuItem(
                    icon = Icons.Default.Chat,
                    iconBackgroundColor = Color(0xFFEC4899).copy(alpha = 0.2f),
                    iconTint = Color(0xFFEC4899),
                    title = "反馈与建议",
                    onClick = { /* Open feedback */ }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // About section
            GlassCard(
                modifier = Modifier.padding(horizontal = 24.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = "关于眠语",
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPrimary,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    Text(
                        text = "眠语是一款专为改善睡眠质量而设计的AI助眠应用。我们通过个性化的故事生成，帮助你在夜晚放松身心，安然入睡。",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "版本",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary
                        )
                        Text(
                            text = "1.0.0",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextPrimary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun StatItem(
    value: String,
    label: String,
    color: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.headlineMedium,
            color = color
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = TextMuted
        )
    }
}

@Composable
private fun MenuItem(
    icon: ImageVector,
    iconBackgroundColor: Color,
    iconTint: Color,
    title: String,
    badge: String? = null,
    onClick: () -> Unit
) {
    GlassCard(
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(iconBackgroundColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                color = TextPrimary,
                modifier = Modifier.weight(1f)
            )

            if (badge != null) {
                Text(
                    text = badge,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextMuted,
                    modifier = Modifier.padding(end = 8.dp)
                )
            }

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = TextMuted,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
private fun MenuItemWithToggle(
    icon: ImageVector,
    iconBackgroundColor: Color,
    iconTint: Color,
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    GlassCard {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(iconBackgroundColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                color = TextPrimary,
                modifier = Modifier.weight(1f)
            )

            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = PurplePrimary,
                    checkedTrackColor = PurplePrimary.copy(alpha = 0.5f)
                )
            )
        }
    }
}