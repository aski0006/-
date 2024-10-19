package com.example.game.my_functions

import androidx.compose.runtime.MutableState
import com.example.project.game_functions.MineFieldData

/**
 * 使用深度优先搜索 (DFS) 打开非地雷按钮的函数。
 *
 * 该函数会递归地打开所有非地雷的按钮，并更新游戏状态。只有在当前位置周围没有地雷的情况下，才会继续递归打开相邻的按钮。
 *
 * @param mineData 地雷场的数据对象，包含网格大小等信息。
 * @param surroundingMineCounts 每个位置周围地雷数量的数组，-1 表示该位置是地雷。
 * @param visited 记录哪些位置已经被访问过的二维布尔数组。
 * @param index 当前位置的索引。
 * @param winCount 记录游戏中已打开的非地雷格子数量的可变状态。
 * @param isOpenList 记录每个按钮是否已打开的数组，每个元素是一个可变状态。
 */
fun dfsOpenNotMineButton(
    mineData: MineFieldData,
    surroundingMineCounts: IntArray,
    visited: Array<BooleanArray>,
    index: Int,
    winCount: MutableState<Int>,
    isOpenList: Array<MutableState<Boolean>>
) {
    // 计算当前位置的行和列
    val row = index / mineData.gridSize
    val col = index % mineData.gridSize

    // 如果当前位置已经访问过，直接返回
    if (visited[row][col]) {
        return
    }

    // 如果当前位置是地雷，不再继续递归
    if (surroundingMineCounts[index] == -1) {
        return
    }

    // 标记当前位置为已访问
    visited[row][col] = true

    // 打开当前按钮
    isOpenList[index].value = true

    // 更新已打开的非地雷格子数量
    winCount.value--

    // 如果当前位置周围有雷，不再继续递归
    if (surroundingMineCounts[index] > 0) {
        return
    }

    // 递归打开周围的按钮
    for (i in -1..1) {
        for (j in -1..1) {
            // 跳过当前位置
            if (i == 0 && j == 0) {
                continue
            }

            // 计算新位置的行和列
            val newRow = row + i
            val newCol = col + j

            // 如果新位置超出网格边界，跳过
            if (newRow < 0 || newRow >= mineData.gridSize || newCol < 0 || newCol >= mineData.gridSize) {
                continue
            }

            // 计算新位置的索引
            val newIndex = newRow * mineData.gridSize + newCol

            // 递归调用函数以打开新位置的按钮
            dfsOpenNotMineButton(
                mineData,
                surroundingMineCounts,
                visited,
                newIndex,
                winCount,
                isOpenList
            )
        }
    }
}