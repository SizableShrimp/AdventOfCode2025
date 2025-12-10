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

import com.microsoft.z3.Context
import com.microsoft.z3.IntNum
import com.microsoft.z3.Status
import me.sizableshrimp.adventofcode2025.templates.Day
import me.sizableshrimp.adventofcode2025.util.*

class Day10 : Day() {
    override fun evaluate(): Result {
        val machines = this.lines.map { l ->
            val parts = l.split(" ")
            val target = parts[0].substring(1, parts[0].length - 1).map { it == '#' }
            val buttons = parts.subList(1, parts.size - 1).map { b -> b.substring(1, b.length - 1).split(",").toInts().toSet() }
            val joltages = parts.last().substring(1, parts.last().length - 1).split(",").toInts()
            Triple(target, buttons, joltages)
        }

        var part1 = 0L
        var part2 = 0L

        for ((target, buttons, joltages) in machines) {
            val queue = ArrayDeque<State>()
            val seen = mutableSetOf<List<Boolean>>()
            queue.add(State(target.map { false }, -1, 0))
            var best = Int.MAX_VALUE

            while (!queue.isEmpty()) {
                val state = queue.removeFirst()
                val nextFlipped = state.totalFlipped + 1
                if (nextFlipped >= best) continue
                for (i in buttons.indices) {
                    if (i == state.lastFlipped) continue
                    val next = state.current.toMutableList()
                    for (j in buttons[i]) {
                        next[j] = !next[j]
                    }
                    if (next == target) {
                        best = nextFlipped
                    } else if (seen.add(next)) {
                        queue.add(State(next, i, nextFlipped))
                    }
                }
            }

            part1 += best

            val context = Context()
            val optimize = context.mkOptimize()

            val dependents = buttons.indices.map { context.mkIntConst(('a'.code + it).toChar().toString()) }
            for ((i, jolts) in joltages.withIndex()) {
                val sum = context.mkAdd(*dependents.filterIndexed { j, _ -> i in buttons[j] }.toTypedArray())
                optimize.Add(context.mkEq(sum, context.mkInt(jolts)))
            }
            dependents.forEach { optimize.Add(context.mkGe(it, context.mkInt(0)))}
            val objective = optimize.MkMinimize(context.mkAdd(*dependents.toTypedArray()))

            check(optimize.Check() == Status.SATISFIABLE) {
                "Failed to find solution"
            }

            part2 += (objective.value as IntNum).int
        }

        return Result.of(part1, part2)
    }

    private data class State(val current: List<Boolean>, val lastFlipped: Int, val totalFlipped: Int)

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Day10().run()
        }

        // Dummy to ensure util package always stays imported
        @Suppress("UNUSED")
        private fun never() = 0.repeat(0)
    }
}