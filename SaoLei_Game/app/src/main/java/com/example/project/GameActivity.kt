package com.example.project

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.project.game_components.DisplayGameTime
import com.example.project.game_components.GameOverScreen
import com.example.project.game_components.GameWinScreen
import com.example.project.game_components.MineField
import com.example.project.game_components.OperatorButton
import com.example.project.game_functions.generateRandomMinesList
import com.example.project.ui.theme.ProjectTheme
import kotlin.time.Duration.Companion.minutes

/**
 * 游戏状态的枚举类。
 *
 * 该枚举类定义了游戏可能的几种状态：
 * - PROCESSING: 游戏正在处理中。
 * - ENDING: 游戏即将结束。
 * - SUCCESSFUL: 游戏成功完成。
 */
enum class GameState {
    /** 游戏正在处理中 */
    PROCESSING,

    /** 游戏即将结束 */
    ENDING,

    /** 游戏成功完成 */
    SUCCESSFUL
}

/**
 * 操作状态的枚举类。
 *
 * 该枚举类定义了操作可能的几种状态：
 * - DO_FLAG: 进行旗帜操作。
 * - DO_MINE: 进行扫雷操作。
 * - OVER: 操作结束。
 */
enum class OperatorState {
    /** 进行旗帜操作 */
    DO_FLAG,

    /** 进行扫雷操作 */
    DO_MINE,

    /** 操作结束 */
    OVER
}

class GameActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val gridSize = intent.getIntExtra("gridSize", 8) // 默认值为8
        val mineNumber = intent.getIntExtra("mineNumber", 10) // 默认值为10
        val gameTime = (intent.getIntExtra("gameTime", 4)).minutes
        setContent {
            ProjectTheme {
                val gameState = remember { mutableStateOf(GameState.PROCESSING) }
                val operatorState = remember { mutableStateOf(OperatorState.DO_MINE) }
                val showAnswer = remember { mutableStateOf(false) }
                val mineData = generateRandomMinesList(gridSize, mineNumber)
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally // 水平居中
                    ) {
                        Spacer(modifier = Modifier.height(50.dp))
                        if (gameState.value == GameState.PROCESSING) {
                            DisplayGameTime(gameTime, gameState)
                        }
                        Spacer(modifier = Modifier.height(50.dp))

                        when (gameState.value) {
                            GameState.PROCESSING -> {
                                MineField(showAnswer, operatorState, gameState, mineData)
                                OperatorButton(operatorState)
                            }

                            GameState.ENDING -> {
                                GameOverScreen(showAnswer, operatorState, gameState, mineData)
                            }

                            GameState.SUCCESSFUL -> {
                                GameWinScreen()
                            }
                        }

                        ReturnMainActivity()
                    }
                }
            }
        }
    }
}

@Composable
fun ReturnMainActivity() {
    val context = LocalContext.current
    Button(
        onClick = {
            context.startActivity(Intent(context, MainActivity::class.java))
        }
    ) {
        Text(
            text = "返回主界面",
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )
    }
}
