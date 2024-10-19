package com.example.project.game_components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.project.GameState
import kotlinx.coroutines.delay
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

/**
 * 显示游戏剩余时间的组合函数。
 *
 * 该函数持续更新并显示游戏剩余时间，直至时间耗尽。每当剩余时间减少一秒时，时间会自动更新并显示新的时间。
 * 当剩余时间减少到零时，游戏状态会更新为 [GameState.ENDING]。
 *
 * @param deathTime 游戏的总时长，即倒计时的初始值。
 * @param gameState 游戏状态的可变状态，用于跟踪游戏的状态变化。
 */
@Composable
fun DisplayGameTime(deathTime: Duration, gameState: MutableState<GameState>) {
    // 使用 remember 保存剩余时间的状态，以在组合中保持其值
    val timeLeft: MutableState<Duration> = remember {
        mutableStateOf(deathTime)
    }

    // LaunchedEffect 用于在组合中启动协程，当 timeLeft 状态变化时自动重启
    LaunchedEffect(Unit) {
        // 循环更新剩余时间，直到时间耗尽
        while (timeLeft.value > Duration.ZERO) {
            // 获取当前剩余时间
            val currentTime = timeLeft.value
            // 减少一秒并更新状态
            timeLeft.value = currentTime - 1.seconds
            // 等待一秒后继续
            delay(1000)
        }
        // 当时间耗尽时，将游戏状态更新为 ENDING
        gameState.value = GameState.ENDING
    }

    // 显示倒计时
    val minutes = timeLeft.value.inWholeMinutes
    val seconds = timeLeft.value.inWholeSeconds % 60
    val formattedTime = "%02d:%02d".format(minutes, seconds)

    // 显示剩余时间的文本组件
    Text(
        text = "剩余时间\n$formattedTime",
        fontSize = 24.sp,
        fontFamily = FontFamily.Serif, // 使用黑体字体
        textAlign = TextAlign.Center
    )
}