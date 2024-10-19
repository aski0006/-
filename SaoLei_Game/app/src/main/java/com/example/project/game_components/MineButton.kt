package com.example.project.game_components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * 地雷按钮的组合函数。
 *
 * 该按钮用于表示地雷游戏中的一个格子，可以是地雷或普通格子。按钮的背景颜色会根据格子的状态（是否有旗帜、是否打开、是否是地雷等）进行变化。
 * 点击按钮时可以切换旗帜状态或打开格子。
 *
 * @param isMine 该格子是否是地雷。
 * @param isOpen 该格子是否已打开的可变状态。
 * @param hasFlag 该格子是否被标记旗帜的可变状态。
 * @param showMine 是否显示地雷，用于在游戏结束时显示所有地雷的位置。
 * @param onToggleFlag 切换旗帜状态的回调函数。
 * @param onClick 打开格子的回调函数。
 */
@Composable
fun MineButton(
    isMine: Boolean,
    isOpen: MutableState<Boolean>,
    hasFlag: MutableState<Boolean>,
    showMine: Boolean,
    onToggleFlag: () -> Unit,
    onClick: () -> Unit
) {

    // 根据不同的条件设置按钮的背景颜色
    val backgroundColor = when {
        hasFlag.value -> Color.Red // 如果有旗帜，背景为红色
        isMine && showMine -> Color.Blue // 如果是地雷且要显示，背景为蓝色
        isOpen.value -> Color.LightGray // 打开时背景为浅灰色
        else -> Color.Gray // 默认背景为灰色
    }

    // 创建一个按钮，并根据状态设置其样式和行为
    Button(
        modifier = Modifier
            .size(32.dp) // 设置按钮的大小
            .padding(2.dp), // 设置按钮的内边距
        onClick = {
            onToggleFlag() // 切换旗帜状态
            onClick() // 执行打开操作
        },
        shape = RoundedCornerShape(0.dp), // 设置按钮的形状，无边框圆角
        colors = ButtonDefaults.buttonColors(containerColor = backgroundColor) // 根据状态设置背景颜色
    ) {
        // 按钮内容为空，仅使用背景颜色表示状态
    }
}