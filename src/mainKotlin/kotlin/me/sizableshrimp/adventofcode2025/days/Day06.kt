/*
 * AdventOfCode2025
 * Copyright (C) 2025 SizableShrimp
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package me.sizableshrimp.adventofcode2025.days

import me.sizableshrimp.adventofcode2025.templates.Day
import me.sizableshrimp.adventofcode2025.util.*

class Day06 : Day() {
    override fun evaluate(): Result {
        val totalWidth = this.lines[0].length
        val grid = Array<MutableList<String>>(this.lines.size) { mutableListOf() }
        var nextSplitIdx = 0
        for (strIdx in 0..totalWidth) {
            if (strIdx == totalWidth || this.lines.all { it[strIdx] == ' ' }) {
                for (i in this.lines.indices) {
                    grid[i].add(this.lines[i].substring(nextSplitIdx, strIdx))
                }
                nextSplitIdx = strIdx + 1
            }
        }

        var part1 = 0L
        var part2 = 0L
        for (x in grid[0].indices) {
            val plus = grid.last()[x].contains("+")

            val width = grid[0][x].length
            val vertNums = IntArray(width) { 0 }
            for (i in vertNums.indices) {
                for (y in 0..<grid.size - 1) {
                    val c = grid[y][x][i]
                    if (c == ' ') continue
                    vertNums[i] = vertNums[i] * 10 + c.digitToInt()
                }
            }

            var res = if (plus) 0L else 1L
            for (y in 0..<grid.size - 1) {
                val num = grid[y][x].trim().toInt()
                if (plus) {
                    res += num
                } else {
                    res *= num
                }
            }
            part1 += res

            res = if (plus) 0L else 1L
            for (num in vertNums) {
                if (plus) {
                    res += num
                } else {
                    res *= num
                }
            }
            part2 += res
        }

        return Result.of(part1, part2)
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Day06().run()
        }

        // Dummy to ensure util package always stays imported
        @Suppress("UNUSED")
        private fun never() = 0.repeat(0)
    }
}