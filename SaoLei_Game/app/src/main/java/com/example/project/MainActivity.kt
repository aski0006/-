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
enum class Difficulty(val gridSize: Int, val mineNumber: Int, val gameTime: Int) {
    /**
     * 简单难度，网格尺寸为 8，地雷数量为 10.
     */
    EASY(8, 10, 3),

    /**
     * 中等难度，网格尺寸为 10，地雷数量为 15.
     */
    MEDIUM(10, 15, 4),

    /**
     * 困难难度，网格尺寸为 12，地雷数量为 20.
     */
    HARD(12, 20, 5)
}

/**
 * 主活动类，应用的入口点。
 *
 * 该类负责设置应用的主题，布局以及初始状态。它包含游戏标题、难度面板、开始游戏按钮和档案按钮。
 */
class MainActivity : ComponentActivity() {

    /**
     * 活动创建时调用。
     *
     * 在此方法中设置应用的界面，初始化状态，并定义用户交互的组件。
     *
     * @param savedInstanceState 如果活动之前被销毁并且正在重新创建，则此包裹中包含先前保存的状态。
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // 启用全屏边缘模式
        setContent {
            ProjectTheme { // 应用自定义主题
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding -> // 使用 Scaffold 安排内容
                    val context = LocalContext.current // 获取当前上下文
                    val difficulty = remember { mutableStateOf(Difficulty.MEDIUM) } // 初始难度状态为中等

                    Column(
                        modifier = Modifier
                            .padding(innerPadding) // 设置内部填充
                            .fillMaxSize(), // 填充满屏幕
                        horizontalAlignment = Alignment.CenterHorizontally // 水平居中对齐内容
                    ) {
                        Spacer(modifier = Modifier.height(100.dp)) // 顶部间隙100.dp

                        GameTitle() // 显示游戏标题

                        Spacer(modifier = Modifier.weight(1f)) // 中间间隙，权重设置为1f

                        DifficultyPanel(difficulty) // 显示难度选择面板

                        Spacer(modifier = Modifier.weight(1f)) // 中间间隙，权重设置为1f

                        GameStartButton(context, difficulty) // 显示开始游戏按钮

                        ArchivesStartButton(context) // 显示查看游戏记录按钮
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

/**
 * 游戏开始按钮的可组合函数。
 *
 * 此函数创建一个按钮，点击后将启动游戏活动，并将当前的难度状态参数传递给该活动。
 *
 * @param context 当前的上下文，用于启动新的活动。
 * @param difficulty 当前选中的难度状态的可变引用。
 */
@Composable
fun GameStartButton(context: Context, difficulty: MutableState<Difficulty>) {
    Button(
        onClick = {
            val intent = Intent(context, GameActivity::class.java)
            intent.putExtra("gridSize", difficulty.value.gridSize)
            intent.putExtra("mineNumber", difficulty.value.mineNumber)
            intent.putExtra("gameTime", difficulty.value.gameTime)
            context.startActivity(intent)
        },
        modifier = Modifier
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

/**
 * 游戏记录按钮的可组合函数。
 *
 * 此函数创建一个按钮，点击后将启动游戏记录活动。
 *
 * @param context 当前的上下文，用于启动新的活动。
 */
@Composable
fun ArchivesStartButton(context: Context) {
    Button(
        onClick = {
            val intent = Intent(context, ArchivesActivity::class.java)
            context.startActivity(intent)
        },
        modifier = Modifier
            .padding(bottom = 100.dp)
    ) {
        Text(
            text = "游戏记录",
            fontSize = 26.sp,
            fontFamily = FontFamily.Serif,
            textAlign = TextAlign.Center
        )
    }
}