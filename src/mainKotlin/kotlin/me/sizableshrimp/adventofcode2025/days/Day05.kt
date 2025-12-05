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
import kotlin.math.max

class Day05 : Day() {
    override fun evaluate(): Result {
        val (rangeStrs, idStrs) = this.lines.splitOnBlankLines()
        val ranges = rangeStrs.map { l ->
            val (start, end) = l.split("-")
            LongRange(start.toLong(), end.toLong())
        }.sortedBy { it.first }
        val ids = idStrs.map { it.toLong() }.sorted()

        val result = mutableListOf<LongRange>()
        var current = ranges[0]
        for (range in ranges.drop(1)) {
            if (range.first !in current) {
                result.add(current)
                current = range
            } else {
                current = LongRange(current.first, max(range.last, current.last))
            }
        }
        result.add(current)

        var part1 = 0
        var idx = 0
        for (id in ids) {
            var next = idx
            while (next in ranges.indices && id !in ranges[next]) {
                next++
            }
            if (next >= ranges.size) continue

            part1++
            idx = next
        }

        return Result.of(part1, result.sumOf { it.size })
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Day05().run()
        }

        // Dummy to ensure util package always stays imported
        @Suppress("UNUSED")
        private fun never() = 0.repeat(0)
    }
}