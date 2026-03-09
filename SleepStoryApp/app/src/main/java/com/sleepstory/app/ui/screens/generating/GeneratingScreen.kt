package com.sleepstory.app.ui.screens.generating

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.sleepstory.app.ui.navigation.Screen
import com.sleepstory.app.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun GeneratingScreen(
    requestId: String,
    navController: NavController
) {
    var progress by remember { mutableFloatStateOf(0f) }
    var statusText by remember { mutableStateOf("正在构思故事框架...") }

    LaunchedEffect(Unit) {
        // Simulate generation process
        val steps = listOf(
            0.15f to "正在构思故事框架...",
            0.30f to "构建角色与场景...",
            0.50f to "撰写故事情节...",
            0.70f to "润色故事内容...",
            0.85f to "生成配音音频...",
            1.00f to "故事创作完成！"
        )

        for ((targetProgress, text) in steps) {
            delay(1500)
            progress = targetProgress
            statusText = text
        }

        delay(500)
        // Navigate to player with the generated story
        navController.navigate(Screen.Player.createRoute("generated_$requestId")) {
            popUpTo(Screen.Home.route) { inclusive = false }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
    ) {
        // Background animated circles
        val infiniteTransition = rememberInfiniteTransition(label = "background")
        val circle1Scale by infiniteTransition.animateFloat(
            initialValue = 0.8f,
            targetValue = 1.2f,
            animationSpec = infiniteRepeatable(
                animation = tween(3000, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "circle1"
        )
        val circle2Scale by infiniteTransition.animateFloat(
            initialValue = 0.8f,
            targetValue = 1.2f,
            animationSpec = infiniteRepeatable(
                animation = tween(3000, delayMillis = 1500, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "circle2"
        )

        // Background blur circles
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 40.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(300.dp)
                    .scale(circle1Scale)
                    .offset(x = (-50).dp, y = 100.dp)
                    .clip(CircleShape)
                    .background(PurplePrimary.copy(alpha = 0.1f))
                    .blur(100.dp)
            )

            Box(
                modifier = Modifier
                    .size(250.dp)
                    .scale(circle2Scale)
                    .offset(x = 100.dp, y = 400.dp)
                    .clip(CircleShape)
                    .background(BlueAccent.copy(alpha = 0.1f))
                    .blur(100.dp)
            )
        }

        // Main content
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Animated circles
            Box(
                modifier = Modifier.size(200.dp),
                contentAlignment = Alignment.Center
            ) {
                // Outer circle
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .border(4.dp, PurplePrimary.copy(alpha = 0.3f), CircleShape)
                )

                // Middle circle
                Box(
                    modifier = Modifier
                        .size(160.dp)
                        .clip(CircleShape)
                        .border(4.dp, PurplePrimary.copy(alpha = 0.5f), CircleShape)
                )

                // Inner circle with icon
                val scale by infiniteTransition.animateFloat(
                    initialValue = 1f,
                    targetValue = 1.1f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(1000, easing = FastOutSlowInEasing),
                        repeatMode = RepeatMode.Reverse
                    ),
                    label = "pulse"
                )

                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .scale(scale)
                        .clip(CircleShape)
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(PurplePrimary, BlueAccent)
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.FlashOn,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(48.dp)
                    )
                }

                // Rotating dashed ring
                val rotation by infiniteTransition.animateFloat(
                    initialValue = 0f,
                    targetValue = 360f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(8000, easing = LinearEasing)
                    ),
                    label = "rotation"
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .border(
                            width = 2.dp,
                            brush = Brush.sweepGradient(
                                colors = listOf(PurplePrimary, BlueAccent, PurplePrimary)
                            ),
                            shape = CircleShape
                        )
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Title
            Text(
                text = "正在创作你的专属故事",
                style = MaterialTheme.typography.headlineMedium,
                color = TextPrimary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Subtitle
            Text(
                text = "AI正在根据你的偏好编织梦境\n请稍候...",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary,
                textAlign = TextAlign.Center,
                lineHeight = 24.sp
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Progress bar
            Box(
                modifier = Modifier
                    .width(280.dp)
                    .height(6.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.1f))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(progress)
                        .clip(CircleShape)
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(PurplePrimary, BlueAccent)
                            )
                        )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Progress text
            Text(
                text = "${(progress * 100).toInt()}% - $statusText",
                style = MaterialTheme.typography.bodyMedium,
                color = PurpleLight
            )

            Spacer(modifier = Modifier.height(64.dp))

            // Tip
            Text(
                text = "💡 小贴士：深呼吸，放松身心\n故事即将为你呈现",
                style = MaterialTheme.typography.bodySmall,
                color = TextMuted,
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )
        }
    }
}

private fun Modifier.blur(radius: Int): Modifier = this