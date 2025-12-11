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

class Day11 : Day() {
    override fun evaluate(): Result {
        val nodes = this.lines.associate { l ->
            val (label, rest) = l.split(": ")
            label to rest.split(" ")
        }

        val part1 = searchMemoizing("you", { it }) { curr, submit ->
            nodes[curr]!!.sumOf { next -> if (next == "out") 1L else submit(next) }
        }

        val part2 = searchMemoizing(State("svr", seenDac = false, seenFft = false), { it }) { curr, submit ->
            nodes[curr.node]!!.sumOf { next ->
                if (next == "out") {
                    if (curr.seenDac && curr.seenFft) 1L else 0L
                } else {
                    submit(State(next, curr.seenDac || next == "dac", curr.seenFft || next == "fft"))
                }
            }
        }

        return Result.of(part1, part2)
    }

    private data class State(val node: String, val seenDac: Boolean, val seenFft: Boolean)

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Day11().run()
        }

        // Dummy to ensure util package always stays imported
        @Suppress("UNUSED")
        private fun never() = 0.repeat(0)
    }
}