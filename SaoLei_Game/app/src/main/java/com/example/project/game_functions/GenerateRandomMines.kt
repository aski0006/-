package com.example.project.game_functions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlin.random.Random

/**
 * 表示一个矿区的数据类。
 *
 * @property mineIndices 矿井在矿区中的索引列表。
 * @property gridSize 矿区的大小（边长）。
 * @property mineNumber 矿井的数量。
 */
data class MineFieldData(
    val mineIndices: List<Int>,
    val gridSize: Int,
    val mineNumber: Int
)

/**
 * 生成随机矿井索引列表的组合函数。
 *
 * 该函数生成一个包含指定数量矿井索引的矿区数据。
 * 矿井的索引在矿区的总格子数范围内随机生成，确保没有重复索引。
 *
 * @param gridSize 矿区的大小（边长），即总格子数为 gridSize * gridSize。
 * @param mineNumber 需要生成的矿井数量。
 * @return 包含随机生成的矿井索引、矿区大小和矿井数量的 [MineFieldData] 对象。
 */
@Composable
fun generateRandomMinesList(
    gridSize: Int,
    mineNumber: Int
): MineFieldData {
    // 使用 remember 保存生成的矿井索引列表，避免重复计算
    val mineIndices = remember {
        // 使用可变集合存储独特的矿井索引
        val indices = mutableSetOf<Int>()

        // 持续生成随机索引直到达到所需的矿井数量
        while (indices.size < mineNumber) {
            // 随机生成索引，范围为 0 到 gridSize * gridSize - 1
            indices.add(Random.nextInt(gridSize * gridSize))
        }

        // 将可变集合转换为不可变列表返回
        indices.toList()
    }

    // 返回一个包含随机矿井索引和其他相关信息的 MineFieldData 对象
    return MineFieldData(mineIndices, gridSize, mineNumber)
}