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

class Day02 : Day() {
    override fun evaluate(): Result {
        val ranges = this.lines[0].split(",").map { l -> l.split("-").toLongs().toPair() }
        var part1 = 0L
        val part2 = ranges.sumOf { (l, r) ->
            (l..r).sumOf {
                val str = it.toString()

                loop@for ((j, size) in (str.length / 2).downTo(1).withIndex()) {
                    if (str.length % size != 0)
                        continue

                    for (i in 0..<(str.length / size - 1)) {
                        val sub = str.subSequence(i * size, (i + 1) * size)
                        val next = str.subSequence((i + 1) * size, (i + 2) * size)

                        if (sub != next)
                            continue@loop
                    }

                    if (j == 0 && str.length % 2 == 0)
                        part1 += it

                    return@sumOf it
                }

                0L
            }
        }

        return Result.of(part1, part2)
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Day02().run()
        }

        // Dummy to ensure util package always stays imported
        @Suppress("UNUSED")
        private fun never() = 0.repeat(0)
    }
}