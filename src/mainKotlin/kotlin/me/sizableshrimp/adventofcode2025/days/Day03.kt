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

class Day03 : Day() {
    override fun evaluate(): Result {
        val banks = this.lines.map { l -> l.map { it.digitToInt() } }

        return Result.of(
            banks.sumOf { calc(it, 2, 10L) },
            banks.sumOf { calc(it, 12, 100_000_000_000L) }
        )
    }

    private fun calc(bank: List<Int>, depth: Int, scale: Long, idx: Int = 0): Long {
        val bestIdx = idx + bank.slice(idx..<bank.size - depth + 1).argmax()

        return if (scale == 1L) bank[bestIdx].toLong() else scale * bank[bestIdx] + calc(bank, depth - 1, scale / 10, bestIdx + 1)
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Day03().run()
        }

        // Dummy to ensure util package always stays imported
        @Suppress("UNUSED")
        private fun never() = 0.repeat(0)
    }
}