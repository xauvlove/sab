package com.sleepstory.app.ui.screens.assessment

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sleepstory.app.data.model.AssessmentOption
import com.sleepstory.app.data.model.AssessmentQuestion
import com.sleepstory.app.ui.components.GlassCard
import com.sleepstory.app.ui.components.GradientButton
import com.sleepstory.app.ui.components.ProgressBar
import com.sleepstory.app.ui.theme.*

@Composable
fun AssessmentScreen(
    onAssessmentComplete: () -> Unit
) {
    var currentQuestionIndex by remember { mutableIntStateOf(0) }
    var selectedOptions by remember { mutableStateOf<Map<Int, String>>(emptyMap()) }

    val questions = remember {
        listOf(
            AssessmentQuestion(
                id = 1,
                question = "你通常几点准备入睡？",
                subtitle = "这将帮助我们为你推荐合适的音频时长",
                options = listOf(
                    AssessmentOption("1", "21:00 - 22:00"),
                    AssessmentOption("2", "22:00 - 23:00"),
                    AssessmentOption("3", "23:00 - 00:00"),
                    AssessmentOption("4", "00:00 以后")
                )
            ),
            AssessmentQuestion(
                id = 2,
                question = "你入睡通常需要多长时间？",
                subtitle = "帮助我们了解你的睡眠状况",
                options = listOf(
                    AssessmentOption("1", "15分钟以内"),
                    AssessmentOption("2", "15-30分钟"),
                    AssessmentOption("3", "30-60分钟"),
                    AssessmentOption("4", "超过1小时")
                )
            ),
            AssessmentQuestion(
                id = 3,
                question = "你喜欢什么类型的故事？",
                subtitle = "可多选，我们将据此定制内容",
                options = listOf(
                    AssessmentOption("nature", "🌲 自然探索", "🌲"),
                    AssessmentOption("fantasy", "🏰 奇幻冒险", "🏰"),
                    AssessmentOption("meditation", "🧘 冥想疗愈", "🧘"),
                    AssessmentOption("scifi", "🚀 科幻未来", "🚀"),
                    AssessmentOption("classic", "📚 经典文学", "📚"),
                    AssessmentOption("warm", "💕 温暖治愈", "💕")
                )
            ),
            AssessmentQuestion(
                id = 4,
                question = "理想的故事时长是？",
                subtitle = "根据你的入睡时间选择",
                options = listOf(
                    AssessmentOption("short", "短篇\n5-10分钟"),
                    AssessmentOption("medium", "中篇\n15-30分钟"),
                    AssessmentOption("long", "长篇\n30-60分钟")
                )
            ),
            AssessmentQuestion(
                id = 5,
                question = "你希望故事包含背景音乐吗？",
                subtitle = "",
                options = listOf(
                    AssessmentOption("1", "🌧️ 雨声"),
                    AssessmentOption("2", "🌊 海浪"),
                    AssessmentOption("3", "🌲 森林"),
                    AssessmentOption("4", "🎵 轻音乐"),
                    AssessmentOption("5", "❌ 不需要")
                )
            )
        )
    }

    val currentQuestion = questions[currentQuestionIndex]
    val progress = (currentQuestionIndex + 1) / questions.size.toFloat()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .padding(24.dp)
    ) {
        // Progress header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "评估进度",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )
            Text(
                text = "${currentQuestionIndex + 1}/${questions.size}",
                style = MaterialTheme.typography.bodyMedium,
                color = PurpleLight
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
        ProgressBar(progress = progress)
        Spacer(modifier = Modifier.height(32.dp))

        // Question
        AnimatedContent(
            targetState = currentQuestionIndex,
            transitionSpec = {
                if (targetState > initialState) {
                    slideInHorizontally { width -> width } + fadeIn() togetherWith
                            slideOutHorizontally { width -> -width } + fadeOut()
                } else {
                    slideInHorizontally { width -> -width } + fadeIn() togetherWith
                            slideOutHorizontally { width -> width } + fadeOut()
                }
            },
            label = "question"
        ) { index ->
            val question = questions[index]
            Column {
                Text(
                    text = question.question,
                    style = MaterialTheme.typography.headlineMedium,
                    color = TextPrimary
                )

                if (question.subtitle.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = question.subtitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Options
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    question.options.forEach { option ->
                        val isSelected = selectedOptions[question.id] == option.id
                        OptionCard(
                            option = option,
                            isSelected = isSelected,
                            onClick = {
                                selectedOptions = selectedOptions.toMutableMap().apply {
                                    put(question.id, option.id)
                                }
                            }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Navigation buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (currentQuestionIndex > 0) {
                OutlinedButton(
                    onClick = { currentQuestionIndex-- },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("上一步", color = TextSecondary)
                }
                Spacer(modifier = Modifier.width(16.dp))
            }

            val canProceed = selectedOptions.containsKey(currentQuestion.id)
            GradientButton(
                text = if (currentQuestionIndex == questions.size - 1) "完成" else "下一步",
                onClick = {
                    if (currentQuestionIndex < questions.size - 1) {
                        currentQuestionIndex++
                    } else {
                        onAssessmentComplete()
                    }
                },
                enabled = canProceed,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun OptionCard(
    option: AssessmentOption,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected) {
        PurplePrimary.copy(alpha = 0.2f)
    } else {
        Color.White.copy(alpha = 0.05f)
    }

    val borderColor = if (isSelected) PurplePrimary else Color.White.copy(alpha = 0.1f)

    GlassCard {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Radio button
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .clip(CircleShape)
                    .border(
                        width = 2.dp,
                        color = if (isSelected) PurplePrimary else TextMuted,
                        shape = CircleShape
                    )
                    .background(if (isSelected) PurplePrimary else Color.Transparent),
                contentAlignment = Alignment.Center
            ) {
                if (isSelected) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Option text
            if (option.icon != null) {
                Text(
                    text = option.icon,
                    fontSize = 24.sp
                )
                Spacer(modifier = Modifier.width(12.dp))
            }

            Text(
                text = option.text,
                style = MaterialTheme.typography.bodyLarge,
                color = if (isSelected) TextPrimary else TextSecondary,
                textAlign = TextAlign.Start
            )
        }
    }
}