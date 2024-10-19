package com.example.project.game_components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.game.my_functions.dfsOpenNotMineButton
import com.example.project.GameState
import com.example.project.OperatorState
import com.example.project.game_functions.MineFieldData
import com.example.project.game_functions.getSurroundingMimeCounts


/**
 * 雷区组件，用于展示和处理扫雷游戏的雷区。
 *
 * @param showAnswer 是否显示答案的标志，用于在游戏结束后显示所有雷的位置。
 * @param operatorState 当前操作状态（挖雷或插旗），用于控制玩家当前的操作模式。
 * @param gameState 游戏状态（进行中、结束、成功），用于控制游戏的流程。
 * @param mineData 雷区数据，包含雷区的大小、雷的数量和位置等信息。
 */
@Composable
fun MineField(
    showAnswer: MutableState<Boolean>, // 是否显示答案的标志
    operatorState: MutableState<OperatorState>, // 当前操作状态（挖雷或插旗）
    gameState: MutableState<GameState>, // 游戏状态（进行中、结束、成功）
    mineData: MineFieldData // 雷区数据
) {
    // 生成雷区索引列表，用于遍历雷区中的每个格子。
    val items = (0 until mineData.gridSize * mineData.gridSize).toList()

// 计算每个格子周围的雷数，使用remember函数进行内存优化。
    val surroundingMineCounts = remember { getSurroundingMimeCounts(mineData, items) }

// 记录每个格子是否被打开，初始化为false。
    val isOpenList =
        remember { Array(mineData.gridSize * mineData.gridSize) { mutableStateOf(false) } }

// 记录需要打开的非雷格子数，用于判断游戏是否胜利。
    val winCount =
        remember { mutableIntStateOf(mineData.gridSize * mineData.gridSize - mineData.mineNumber) }

// 记录当前插旗数，用于控制旗子的数量。
    val flagCount = remember { mutableIntStateOf(0) }

// 记录正确插旗数，用于判断游戏是否胜利。
    val winFlag = remember { mutableIntStateOf(0) }

// 使用Column布局包裹整个雷区组件。
    Column {
        // 使用Row布局展示剩余旗子数量，仅在游戏进行中显示。
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            if (gameState.value == GameState.PROCESSING) {
                Text(
                    text = "剩余旗子数量: ${mineData.mineNumber - flagCount.intValue}",
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    fontSize = 18.sp
                )
            }
        }

        // 使用LazyVerticalGrid布局展示雷区，设置网格列数和内容内边距。
        LazyVerticalGrid(
            columns = GridCells.Fixed(mineData.gridSize),
            contentPadding = PaddingValues(5.dp)
        ) {
            // 遍历雷区中的每个格子。
            items(items) { index ->
                // 记录当前格子是否有旗。
                val hasFlag = remember { mutableStateOf(false) }

                // 判断当前格子是否是雷。
                val isMine = index in mineData.mineIndices

                // 获取当前格子是否被打开。
                val isOpen = isOpenList[index]

                // 获取当前格子周围的雷数。
                val surroundingMineCount = surroundingMineCounts[index]

                // 记录DFS访问状态。
                val visited = Array(mineData.gridSize) { BooleanArray(mineData.gridSize) { false } }

                // 检查游戏是否胜利。
                if (winCount.intValue == 0) {
                    gameState.value = GameState.SUCCESSFUL
                }
                // 检查是否所有雷都被正确插旗。
                if (winFlag.intValue == mineData.mineNumber) {
                    gameState.value = GameState.SUCCESSFUL
                }

                // 渲染每个格子。
                MineButton(
                    isMine = isMine,
                    isOpen = isOpen,
                    hasFlag = hasFlag,
                    showMine = showAnswer.value,
                    onToggleFlag = {
                        // 切换旗子。
                        if (operatorState.value == OperatorState.DO_FLAG && !isOpen.value && flagCount.intValue <= mineData.mineNumber) {
                            val oldWinFlag = winFlag.intValue
                            // 只有在旗子数量小于20时才允许插入新的旗子。
                            if (flagCount.intValue < mineData.mineNumber || hasFlag.value) {
                                flagCount.intValue += if (hasFlag.value) -1 else 1
                                hasFlag.value = !hasFlag.value

                                if (isMine) {
                                    winFlag.intValue = oldWinFlag + (if (hasFlag.value) 1 else -1)
                                }
                            }
                        }
                    },
                    onClick = {
                        // 点击格子。
                        if (operatorState.value == OperatorState.DO_MINE) {
                            if (isMine) {
                                // 点到雷，游戏结束。
                                gameState.value = GameState.ENDING
                                showAnswer.value = true
                            } else {
                                // 打开非雷格子。
                                dfsOpenNotMineButton(
                                    mineData,
                                    surroundingMineCounts,
                                    visited,
                                    index,
                                    winCount,
                                    isOpenList
                                )
                            }
                        }
                    }
                )

                // 显示周围的雷数。
                Text(
                    text = if (isOpen.value) {
                        if (surroundingMineCount == 0) "" else surroundingMineCount.toString()
                    } else "",
                    color = when (surroundingMineCount) {
                        0 -> Color.Transparent // 周围没有雷，文本不显示，颜色设为透明
                        1 -> Color.Blue // 雷数为1，设置为蓝色
                        2 -> Color.Green // 雷数为2，设置为绿色
                        3 -> Color.Red // 雷数为3，设置为红色
                        4 -> Color.DarkGray // 雷数为4，设置为深灰色
                        5 -> Color.Magenta // 雷数为5，设置为洋红色
                        6 -> Color.Cyan // 雷数为6，设置为青色
                        7 -> Color.Black // 雷数为7，设置为黑色
                        8 -> Color.Yellow // 雷数为8，设置为黄色
                        else -> Color.Red // 如果超出范围，使用红色作为默认颜色
                    },
                    textAlign = TextAlign.Center,
                    fontSize = 26.sp // 字体大小
                )
            }
        }
    }
}