package com.example.project.game_functions

/**
 * 计算每个位置周围的地雷数量。
 *
 * 该函数会遍历给定的位置列表，并计算每个非地雷位置周围的地雷数量。如果是地雷位置，则将对应的位置标记为 -1。
 *
 * @param mineData 地雷场的数据对象，包含网格大小和地雷位置的信息。
 * @param items 需要计算周围地雷数量的位置索引列表。
 * @return 一个整数数组，表示每个位置周围的地雷数量。如果是地雷位置，则对应值为 -1。
 */
fun getSurroundingMimeCounts(mineData: MineFieldData, items: List<Int>): IntArray {
    // 初始化计数数组，所有值初始为 0
    val counts = IntArray(mineData.gridSize * mineData.gridSize) { 0 }

    // 遍历给定的位置索引列表
    for (index in items) {
        // 如果当前位置不是地雷，计算周围地雷数量
        if (index !in mineData.mineIndices) {
            // 跳过地雷格子

            // 计算当前位置的行和列
            val row = index / mineData.gridSize
            val col = index % mineData.gridSize

            // 遍历当前位置周围的 3x3 区域
            for (i in -1..1) {
                for (j in -1..1) {
                    // 计算新位置的行和列
                    val newRow = row + i
                    val newCol = col + j

                    // 检查新位置是否在网格边界内
                    if (newRow in 0 until mineData.gridSize && newCol in 0 until mineData.gridSize) {
                        // 计算新位置的索引
                        val newIndex = newRow * mineData.gridSize + newCol

                        // 如果新位置是地雷，增加当前位置的地雷数量
                        if (newIndex in mineData.mineIndices) {
                            counts[index]++
                        }
                    }
                }
            }
        }
        else {
            // 如果是地雷位置，将其标记为 -1
            counts[index] = -1
        }
    }

    // 返回计算结果
    return counts
}