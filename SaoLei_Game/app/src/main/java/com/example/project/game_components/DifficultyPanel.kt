package com.example.project.game_components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.project.Difficulty

/**
 * 显示一个难度选项的可组合函数，并提供选择该选项的单选按钮。
 *
 * 此函数在水平排列中显示一个单选按钮和对应的标签文本。
 *
 * @param difficulty 当前选中的难度状态的可变引用。
 * @param option 要显示的难度选项。
 * @param label 该难度选项的标签文本。
 */
@Composable
fun DifficultyOption(difficulty: MutableState<Difficulty>, option: Difficulty, label: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = difficulty.value == option,
            onClick = { difficulty.value = option }
        )
        Text(
            text = label,
            fontSize = 18.sp,
            fontFamily = FontFamily.Serif,
            textAlign = TextAlign.Start,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}