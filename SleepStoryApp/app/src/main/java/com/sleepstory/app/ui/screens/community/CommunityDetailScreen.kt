package com.sleepstory.app.ui.screens.community

import androidx.compose.foundation.background
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.sleepstory.app.data.model.CommunityStoryDetailModel
import com.sleepstory.app.data.model.VoiceConfig
import com.sleepstory.app.service.TTSState
import com.sleepstory.app.ui.screens.community.CommunityDetailViewModel

/**
 * 他人创作故事详情页面
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommunityDetailScreen(
    storyId: Long,
    navController: NavController,
    viewModel: CommunityDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val ttsState by viewModel.ttsState.collectAsState()
    val ttsProgress by viewModel.ttsProgress.collectAsState()

    // 加载故事详情
    LaunchedEffect(storyId) {
        viewModel.loadStoryDetail(storyId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("故事详情") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "返回")
                    }
                },
                actions = {
                    // 删除按钮（仅自己的故事显示）
                    if (uiState.story?.isOwner == true) {
                        IconButton(onClick = { viewModel.deleteStory() }) {
                            Icon(Icons.Default.Delete, contentDescription = "删除")
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                uiState.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                uiState.error != null -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = uiState.error ?: "加载失败",
                                color = MaterialTheme.colorScheme.error
                            )
                            Button(onClick = { viewModel.loadStoryDetail(storyId) }) {
                                Text("重试")
                            }
                        }
                    }
                }
                uiState.story != null -> {
                    val story = uiState.story!!
                    val scrollState = rememberScrollState()

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(scrollState)
                            .padding(16.dp)
                    ) {
                        // 标题
                        Text(
                            text = story.title,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // 作者信息
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.primaryContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = story.userNickname.firstOrNull()?.toString() ?: "用户",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }

                            Column {
                                Text(
                                    text = story.userNickname,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Medium
                                )
                                story.createdAt?.let {
                                    Text(
                                        text = it,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // 统计数据
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(24.dp)
                        ) {
                            // 点赞
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = if (story.isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                    contentDescription = "点赞",
                                    tint = if (story.isLiked) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = "${story.likesCount}",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }

                            // 播放
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Headphones,
                                    contentDescription = "播放",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = "${story.playsCount}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            // 分类
                            story.category?.let { category ->
                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = MaterialTheme.colorScheme.secondaryContainer
                                ) {
                                    Text(
                                        text = getCategoryName(category),
                                        style = MaterialTheme.typography.labelMedium,
                                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // 简介
                        story.summary?.let { summary ->
                            Text(
                                text = summary,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                        }

                        Divider()

                        Spacer(modifier = Modifier.height(16.dp))

                        // 故事内容
                        Text(
                            text = story.content,
                            style = MaterialTheme.typography.bodyLarge,
                            lineHeight = MaterialTheme.typography.bodyLarge.lineHeight * 1.5
                        )

                        // 底部播放控制栏
                        Spacer(modifier = Modifier.height(80.dp))
                    }

                    // 底部播放控制栏
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter),
                        shadowElevation = 8.dp,
                        color = MaterialTheme.colorScheme.surface
                    ) {
                        CommunityPlayerControl(
                            ttsState = ttsState,
                            progress = ttsProgress,
                            onPlayClick = {
                                if (ttsState == TTSState.PLAYING) {
                                    viewModel.pauseTTS()
                                } else {
                                    viewModel.playTTS()
                                }
                            },
                            onStopClick = { viewModel.stopTTS() },
                            onLikeClick = { viewModel.toggleLike() }
                        )
                    }
                }
            }

            // 删除成功提示
            if (uiState.isDeleted) {
                LaunchedEffect(Unit) {
                    navController.navigateUp()
                }
            }
        }
    }
}

/**
 * 社区故事播放控制栏
 */
@Composable
fun CommunityPlayerControl(
    ttsState: TTSState,
    progress: Float,
    onPlayClick: () -> Unit,
    onStopClick: () -> Unit,
    onLikeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 进度条
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .clip(RoundedCornerShape(2.dp)),
        )

        Spacer(modifier = Modifier.height(12.dp))

        // 控制按钮
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 点赞按钮
            IconButton(onClick = onLikeClick) {
                Icon(
                    imageVector = Icons.Default.FavoriteBorder,
                    contentDescription = "点赞",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // 停止按钮
            IconButton(
                onClick = onStopClick,
                enabled = ttsState != TTSState.IDLE
            ) {
                Icon(
                    imageVector = Icons.Default.Stop,
                    contentDescription = "停止",
                    tint = if (ttsState != TTSState.IDLE) {
                        MaterialTheme.colorScheme.onSurface
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                    }
                )
            }

            // 播放/暂停按钮
            FilledIconButton(
                onClick = onPlayClick,
                modifier = Modifier.size(56.dp),
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(
                    imageVector = when (ttsState) {
                        TTSState.PLAYING -> Icons.Default.Pause
                        else -> Icons.Default.PlayArrow
                    },
                    contentDescription = if (ttsState == TTSState.PLAYING) "暂停" else "播放",
                    modifier = Modifier.size(32.dp)
                )
            }

            // 状态文本
            Text(
                text = when (ttsState) {
                    TTSState.INITIALIZING -> "初始化中..."
                    TTSState.PLAYING -> "播放中"
                    TTSState.PAUSED -> "已暂停"
                    else -> "点击播放"
                },
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // 占位，保持布局对称
            Box(modifier = Modifier.size(48.dp))
        }
    }
}

/**
 * 获取分类中文名称
 */
private fun getCategoryName(category: String): String {
    return when (category) {
        "NATURE" -> "自然"
        "FANTASY" -> "奇幻"
        "MEDITATION" -> "冥想"
        "WARM" -> "温暖"
        "SCIFI" -> "科幻"
        "CLASSIC" -> "经典"
        else -> category
    }
}
