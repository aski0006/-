package com.example.project.game_components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.project.OperatorState

/**
 * 可组合函数，用于显示一个操作按钮组件。
 *
 * 该函数显示两个单选按钮，分别用于设置操作状态为“插旗”或“挖雷”。
 * 用户点击其中一个单选按钮时，会更新 [operatorState] 的值。
 *
 * @param operatorState 一个 [MutableState] 对象，表示当前的操作状态。
 */
@Composable
fun OperatorButton(operatorState: MutableState<OperatorState>) {
    // 创建一个水平排列的行，填满父容器的宽度，并垂直居中对齐其中的子组件。
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        // 第一个单选按钮，表示“插旗”操作。
        RadioButton(
            selected = operatorState.value == OperatorState.DO_FLAG, // 检查当前操作状态是否为“插旗”。
            onClick = { operatorState.value = OperatorState.DO_FLAG }, // 点击时将操作状态设置为“插旗”。
            modifier = Modifier.padding(8.dp) // 添加8dp的内边距。
        )
        // 显示文本“Flag”，表示“插旗”操作。
        Text(text = "Flag", modifier = Modifier.padding(8.dp))

        // 第二个单选按钮，表示“挖雷”操作。
        RadioButton(
            selected = operatorState.value == OperatorState.DO_MINE, // 检查当前操作状态是否为“挖雷”。
            onClick = { operatorState.value = OperatorState.DO_MINE }, // 点击时将操作状态设置为“挖雷”。
            modifier = Modifier.padding(8.dp) // 添加8dp的内边距。
        )
        // 显示文本“Mine”，表示“挖雷”操作。
        Text(text = "Mine", modifier = Modifier.padding(8.dp))
    }
}