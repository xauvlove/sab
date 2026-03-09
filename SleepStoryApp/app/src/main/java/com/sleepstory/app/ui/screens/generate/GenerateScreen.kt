package com.sleepstory.app.ui.screens.generate

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.sleepstory.app.data.model.StoryDuration
import com.sleepstory.app.data.model.StoryMood
import com.sleepstory.app.data.model.StoryScene
import com.sleepstory.app.ui.components.GlassCard
import com.sleepstory.app.ui.components.GradientButton
import com.sleepstory.app.ui.navigation.Screen
import com.sleepstory.app.ui.theme.*
import java.util.UUID

@Composable
fun GenerateScreen(
    navController: NavController
) {
    val scrollState = rememberScrollState()
    var keywords by remember { mutableStateOf("") }
    var selectedScene by remember { mutableStateOf<StoryScene?>(null) }
    var selectedMood by remember { mutableFloatStateOf(0.5f) }
    var selectedDuration by remember { mutableStateOf(StoryDuration.MEDIUM) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = TextSecondary
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .verticalScroll(scrollState)
        ) {
            Text(
                text = "定制你的专属故事",
                style = MaterialTheme.typography.headlineMedium,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "AI将根据你的选择创作独一无二的故事",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Keywords input
            Text(
                text = "故事关键词",
                style = MaterialTheme.typography.titleMedium,
                color = TextPrimary,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            GlassCard {
                BasicTextField(
                    value = keywords,
                    onValueChange = { keywords = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    decorationBox = { innerTextField ->
                        if (keywords.isEmpty()) {
                            Text(
                                text = "例如：星空、猫咪、温暖的茶...",
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextMuted
                            )
                        }
                        innerTextField()
                    }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Scene selection
            Text(
                text = "选择场景",
                style = MaterialTheme.typography.titleMedium,
                color = TextPrimary,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            val scenes = listOf(
                Triple(StoryScene.MOONLIGHT, "🌙", "月夜"),
                Triple(StoryScene.MOUNTAIN, "🏔️", "山间"),
                Triple(StoryScene.BEACH, "🏖️", "海边"),
                Triple(StoryScene.COTTAGE, "🏡", "小屋"),
                Triple(StoryScene.JOURNEY, "🚂", "旅途"),
                Triple(StoryScene.GARDEN, "🌸", "花园")
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                scenes.take(3).forEach { (scene, icon, label) ->
                    SceneButton(
                        icon = icon,
                        label = label,
                        selected = selectedScene == scene,
                        onClick = { selectedScene = scene },
                        modifier = Modifier.weight(1f)
                    )
                    if (scene != scenes[2].first) Spacer(modifier = Modifier.width(8.dp))
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                scenes.drop(3).forEach { (scene, icon, label) ->
                    SceneButton(
                        icon = icon,
                        label = label,
                        selected = selectedScene == scene,
                        onClick = { selectedScene = scene },
                        modifier = Modifier.weight(1f)
                    )
                    if (scene != scenes[5].first) Spacer(modifier = Modifier.width(8.dp))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Mood slider
            Text(
                text = "故事基调",
                style = MaterialTheme.typography.titleMedium,
                color = TextPrimary,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            GlassCard {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("平静", style = MaterialTheme.typography.bodySmall, color = TextMuted)
                        Text("温暖", style = MaterialTheme.typography.bodySmall, color = TextMuted)
                        Text("奇幻", style = MaterialTheme.typography.bodySmall, color = TextMuted)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Slider(
                        value = selectedMood,
                        onValueChange = { selectedMood = it },
                        colors = SliderDefaults.colors(
                            thumbColor = PurplePrimary,
                            activeTrackColor = PurplePrimary,
                            inactiveTrackColor = Color.White.copy(alpha = 0.1f)
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Duration selection
            Text(
                text = "故事长度",
                style = MaterialTheme.typography.titleMedium,
                color = TextPrimary,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                DurationButton(
                    title = "短篇",
                    subtitle = "5-10分",
                    selected = selectedDuration == StoryDuration.SHORT,
                    onClick = { selectedDuration = StoryDuration.SHORT },
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(12.dp))

                DurationButton(
                    title = "中篇",
                    subtitle = "15-30分",
                    selected = selectedDuration == StoryDuration.MEDIUM,
                    onClick = { selectedDuration = StoryDuration.MEDIUM },
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(12.dp))

                DurationButton(
                    title = "长篇",
                    subtitle = "30-60分",
                    selected = selectedDuration == StoryDuration.LONG,
                    onClick = { selectedDuration = StoryDuration.LONG },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Generate button
            GradientButton(
                text = "开始生成",
                onClick = {
                    val requestId = UUID.randomUUID().toString()
                    navController.navigate(Screen.Generating.createRoute(requestId))
                },
                icon = {
                    Icon(
                        imageVector = Icons.Default.FlashOn,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun SceneButton(
    icon: String,
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (selected) {
        PurplePrimary.copy(alpha = 0.2f)
    } else {
        Color.White.copy(alpha = 0.05f)
    }

    val borderColor = if (selected) PurplePrimary else Color.White.copy(alpha = 0.1f)
    val textColor = if (selected) TextPrimary else TextSecondary

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .clickable(onClick = onClick)
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = icon, fontSize = 28.sp)
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = textColor
        )
    }
}

@Composable
private fun DurationButton(
    title: String,
    subtitle: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (selected) {
        PurplePrimary.copy(alpha = 0.2f)
    } else {
        Color.White.copy(alpha = 0.05f)
    }

    val borderColor = if (selected) PurplePrimary else Color.White.copy(alpha = 0.1f)
    val textColor = if (selected) TextPrimary else TextSecondary

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .clickable(onClick = onClick)
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            color = textColor
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = subtitle,
            style = MaterialTheme.typography.labelSmall,
            color = TextMuted
        )
    }
}