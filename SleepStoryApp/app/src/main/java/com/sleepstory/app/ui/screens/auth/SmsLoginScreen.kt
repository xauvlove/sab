package com.sleepstory.app.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.delay

/**
 * 验证码登录页面
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SmsLoginScreen(
    onNavigateToPasswordLogin: () -> Unit,
    onLoginSuccess: () -> Unit,
    viewModel: SmsLoginViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

    // 处理登录成功
    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            onLoginSuccess()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF1a237e),
                        Color(0xFF0d1642)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(60.dp))

            // Logo和标题
            Icon(
                imageVector = Icons.Default.Nightlight,
                contentDescription = null,
                modifier = Modifier.size(80.dp),
                tint = Color(0xFF9fa8da)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "手机号登录",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Text(
                text = "未注册的手机号将自动创建账号",
                fontSize = 16.sp,
                color = Color(0xFF9fa8da),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(48.dp))

            // 登录表单
            SmsLoginForm(
                phone = uiState.phone,
                onPhoneChange = viewModel::onPhoneChange,
                code = uiState.code,
                onCodeChange = viewModel::onCodeChange,
                countdownSeconds = uiState.countdownSeconds,
                isCountingDown = uiState.isCountingDown,
                isLoading = uiState.isLoading,
                onSendCode = viewModel::sendCode,
                onSubmit = viewModel::login
            )

            // 错误提示
            if (uiState.errorMessage != null) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = uiState.errorMessage!!,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // 密码登录链接
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "使用密码登录？",
                    color = Color(0xFF9fa8da),
                    fontSize = 14.sp
                )
                TextButton(onClick = onNavigateToPasswordLogin) {
                    Text(
                        text = "点击切换",
                        color = Color(0xFF7986cb),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SmsLoginForm(
    phone: String,
    onPhoneChange: (String) -> Unit,
    code: String,
    onCodeChange: (String) -> Unit,
    countdownSeconds: Int,
    isCountingDown: Boolean,
    isLoading: Boolean,
    onSendCode: () -> Unit,
    onSubmit: () -> Unit
) {
    // 验证手机号格式
    val isPhoneValid = phone.length == 11 && phone.startsWith("1")

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 手机号输入
        OutlinedTextField(
            value = phone,
            onValueChange = { value ->
                if (value.length <= 11 && value.all { it.isDigit() }) {
                    onPhoneChange(value)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("手机号") },
            placeholder = { Text("请输入手机号") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Phone,
                    contentDescription = null,
                    tint = Color(0xFF9fa8da)
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Phone,
                imeAction = ImeAction.Next
            ),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF7986cb),
                unfocusedBorderColor = Color(0xFF5c6bc0),
                focusedLabelColor = Color(0xFF7986cb),
                unfocusedLabelColor = Color(0xFF9fa8da),
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            ),
            shape = RoundedCornerShape(12.dp)
        )

        // 验证码输入
        OutlinedTextField(
            value = code,
            onValueChange = { value ->
                if (value.length <= 6 && value.all { it.isDigit() }) {
                    onCodeChange(value)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("验证码") },
            placeholder = { Text("请输入验证码") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                    tint = Color(0xFF9fa8da)
                )
            },
            trailingIcon = {
                // 发送验证码按钮
                TextButton(
                    onClick = onSendCode,
                    enabled = isPhoneValid && !isCountingDown && !isLoading
                ) {
                    if (isCountingDown) {
                        Text(
                            text = "${countdownSeconds}s",
                            color = Color(0xFF9fa8da),
                            fontSize = 14.sp
                        )
                    } else {
                        Text(
                            text = "获取验证码",
                            color = if (isPhoneValid) Color(0xFF7986cb) else Color(0xFF9fa8da),
                            fontSize = 14.sp
                        )
                    }
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF7986cb),
                unfocusedBorderColor = Color(0xFF5c6bc0),
                focusedLabelColor = Color(0xFF7986cb),
                unfocusedLabelColor = Color(0xFF9fa8da),
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            ),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 登录按钮
        Button(
            onClick = onSubmit,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF7986cb),
                disabledContainerColor = Color(0xFF7986cb).copy(alpha = 0.5f)
            ),
            enabled = !isLoading && isPhoneValid && code.length == 6
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = Color.White,
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    text = "登录/注册",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
