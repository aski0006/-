package com.example.project.game_components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

/**
 * 显示标题文本的可组合函数。
 *
 * @param title 要显示的标题文本。这个文本将在屏幕中央以 **36sp** 的字体大小显示，并使用 **Serif** 字体。
 */
@Composable
fun Title(title: String) {
    Text(
        text = title,
        fontSize = 36.sp,
        fontFamily = FontFamily.Serif,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth()
    )
}