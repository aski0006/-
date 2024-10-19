package com.example.project.game_components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.example.project.GameState
import com.example.project.OperatorState
import com.example.project.game_functions.MineFieldData

/**
 * 显示游戏结束屏幕的组合函数。
 *
 * 该函数在游戏结束时显示，提供以下功能：
 * - 显示“游戏结束”的文本。
 * - 提供一个按钮用于切换显示或隐藏答案。
 * - 当显示答案时，展示地雷场（MineField）。
 * - 当游戏结束时，更新操作状态为 [OperatorState.OVER]。
 *
 * @param showAnswer 控制是否显示答案的可变状态。
 * @param operatorState 操作状态的可变状态，用于跟踪游戏操作的变化。
 * @param gameState 游戏状态的可变状态，用于跟踪游戏的状态变化。
 * @param mineData 地雷场的数据，包含地雷的布局信息。
 */
@Composable
fun GameOverScreen(
    showAnswer: MutableState<Boolean>,
    operatorState: MutableState<OperatorState>,
    gameState: MutableState<GameState>,
    mineData: MineFieldData
) {
    // 检查游戏状态是否为 ENDING，如果是则将操作状态设置为 OVER
    if (gameState.value == GameState.ENDING) {
        operatorState.value = OperatorState.OVER
    }

    // 使用 Column 布局来组织界面元素
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 显示“游戏结束”的文本
        Text(
            text = "游戏结束",
            fontSize = 32.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(16.dp)
        )

        // 显示切换显示或隐藏答案的按钮
        Button(
            onClick = { showAnswer.value = !showAnswer.value },
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = if (showAnswer.value) "隐藏答案" else "显示答案")
        }

        // 如果 showAnswer 为 true，则显示地雷场
        if (showAnswer.value) {
            MineField(showAnswer, operatorState, gameState, mineData)
        }

        // 添加一个垂直间隔以增加布局的间距
        Spacer(modifier = Modifier.height(50.dp))
    }
}