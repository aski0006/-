package com.example.project.game_components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * 显示扫雷成功的界面。
 *
 * 该可组合函数渲染一个表示玩家成功完成扫雷游戏的界面。
 * 它显示一条居中的文本消息“扫雷成功”，并在下方添加一个间距，以改善布局的视觉效果。
 */
@Composable
fun GameWinScreen() {
    // 显示一条居中的文本，表示游戏成功，字体大小为32sp。
    Text(
        text = "扫雷成功", // 显示的文本内容，表示“扫雷成功”。
        fontSize = 32.sp, // 文本的字体大小设置为32sp。
        textAlign = TextAlign.Center // 将文本水平居中对齐。
    )

    // 添加一个高度为50dp的垂直间距，以改善视觉布局。
    Spacer(modifier = Modifier.height(50.dp))
}