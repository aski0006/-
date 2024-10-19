package com.example.project

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.project.game_components.DifficultyOption
import com.example.project.game_components.Title
import com.example.project.ui.theme.ProjectTheme

/**
 * 代表游戏的难度级别.
 *
 * @property gridSize 网格的尺寸
 * @property mineNumber 地雷的数量
 */
enum class Difficulty(val gridSize: Int, val mineNumber: Int) {
    /**
     * 简单难度，网格尺寸为 8，地雷数量为 10.
     */
    EASY(8, 10),

    /**
     * 中等难度，网格尺寸为 10，地雷数量为 15.
     */
    MEDIUM(10, 15),

    /**
     * 困难难度，网格尺寸为 12，地雷数量为 20.
     */
    HARD(12, 20)
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProjectTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val context = LocalContext.current
                    val difficulty = remember { mutableStateOf(Difficulty.MEDIUM) }
                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.height(100.dp)) // 距离顶部100.dp

                        GameTitle()

                        Spacer(modifier = Modifier.weight(1f)) // 中间隔开

                        DifficultyPanel(difficulty)

                        Spacer(modifier = Modifier.weight(1f)) // 中间隔开

                        GameStartButton(context, difficulty)
                    }
                }
            }
        }
    }
}

@Composable
fun GameTitle() {
    Title("扫雷游戏")
}

/**
 * 显示难度选择面板的可组合函数。
 *
 * 此函数在一个垂直列中显示难度选择的标题和三个难度选项（简单、中等、困难）。
 *
 * @param difficulty 当前选中的难度状态的可变引用。
 */
@Composable
fun DifficultyPanel(difficulty: MutableState<Difficulty>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "选择难度",
            fontSize = 24.sp,
            fontFamily = FontFamily.Serif,
            textAlign = TextAlign.Start,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        DifficultyOption(difficulty, Difficulty.EASY, "简单")
        DifficultyOption(difficulty, Difficulty.MEDIUM, "中等")
        DifficultyOption(difficulty, Difficulty.HARD, "困难")
    }
}

@Composable
fun GameStartButton(context: Context, difficulty: MutableState<Difficulty>) {
    Button(
        onClick = {
            val intent = Intent(context, GameActivity::class.java)
            intent.putExtra("gridSize", difficulty.value.gridSize)
            intent.putExtra("mineNumber", difficulty.value.mineNumber)
            context.startActivity(intent)
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 100.dp)
    ) {
        Text(
            text = "开始游戏",
            fontSize = 26.sp,
            fontFamily = FontFamily.Serif,
            textAlign = TextAlign.Center
        )
    }
}