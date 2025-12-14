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

import me.sizableshrimp.adventofcode2025.helper.LinearAlgebra
import me.sizableshrimp.adventofcode2025.templates.SeparatedDay
import me.sizableshrimp.adventofcode2025.util.*

class Day10 : SeparatedDay() {
    private val machines by lazy {
        this.lines.map { l ->
            val parts = l.split(" ")
            val target = parts[0].substring(1, parts[0].length - 1).map { it == '#' }
            val buttons = parts.subList(1, parts.size - 1).map { b -> b.substring(1, b.length - 1).split(",").toInts().toSet() }
            val joltages = parts.last().substring(1, parts.last().length - 1).split(",").toInts()
            Triple(target, buttons, joltages)
        }
    }

    override fun part1() = this.machines.sumOf { (target, buttons, _) ->
        val queue = ArrayDeque<StateP1>()
        val seen = mutableSetOf<List<Boolean>>()
        queue.add(StateP1(target.map { false }, -1, 0))
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
                    queue.add(StateP1(next, i, nextFlipped))
                }
            }
        }

        best
    }

    override fun part2() = this.machines.sumOf { (_, buttons, joltages) ->
        val matrix = Array(joltages.size) { IntArray(buttons.size + 1) }

        for ((y, jolts) in joltages.withIndex()) {
            matrix[y][matrix[0].lastIndex] = jolts;
        }

        for ((x, button) in buttons.withIndex()) {
            for (y in button) {
                matrix[y][x] = 1
            }
        }

        check(LinearAlgebra.solve(matrix))

        val freeButtonsSet = buttons.indices.toMutableSet()
        val staticButtons = mutableSetOf<Int>()
        val dependentButtons = mutableSetOf<Int>()
        val leadingXs = IntArray(matrix.size) { -1 }
        var leadingX = 0
        for ((y, row) in matrix.withIndex()) {
            while (leadingX in row.indices && row[leadingX] == 0)
                leadingX++
            if (leadingX in row.indices && row[leadingX] != 0) {
                leadingXs[y] = leadingX
                freeButtonsSet.remove(leadingX)
                if (((leadingX + 1)..<(row.size - 1)).all { row[it] == 0 }) {
                    staticButtons.add(leadingX)
                } else {
                    dependentButtons.add(leadingX)
                }
            }
        }
        val freeButtons = freeButtonsSet.toList()

        // println(matrix.contentDeepToString().replace("], [", "]\n["))
        // println("Free vars: ${freeButtons.size}, static vars: ${staticButtons.size}, dependent vars: ${dependentButtons.size}")

        var bestTotal = -1
        var best: StateP2? = null
        val start = StateP2(0, 0.repeat(freeButtons.size), 0)

        if (freeButtons.isEmpty() || matrix.withIndex().all { (y, row) ->
                val leadingX = leadingXs[y]
                if (leadingX == -1) return@all true

                val rhs = row.last()

                rhs >= 0 && rhs % row[leadingX] == 0
            }) {
            bestTotal = matrix.withIndex().sumOf { (y, row) ->
                val leadingX = leadingXs[y]
                if (leadingX == -1) return@sumOf 0

                val rhs = row.last()

                rhs / row[leadingX]
            }
            best = start
        }

        if (freeButtons.isEmpty())
            return@sumOf bestTotal

        val queue = ArrayDeque<StateP2>()
        queue.add(start)

        outer@ while (queue.isNotEmpty()) {
            val state = queue.removeFirst()

            for (i in state.minIncrement..<state.currFree.size) {
                val nextFree = state.currFree.toMutableList()
                nextFree[i]++
                val nextTotal = state.total + 1

                val isFeasible = matrix.withIndex().all { (y, row) ->
                    val leadingX = leadingXs[y]
                    if (leadingX == -1) return@all true

                    val lhs = nextFree.withIndex().sumOf { (i, num) -> num * row[freeButtons[i]] }
                    val rhs = row.last() - lhs

                    rhs >= 0 && rhs % row[leadingX] == 0
                }
                val next = StateP2(nextTotal, nextFree, i)

                if (isFeasible) {
                    val nextAllTotal = nextTotal + matrix.withIndex().sumOf { (y, row) ->
                        val leadingX = leadingXs[y]
                        if (leadingX == -1) return@sumOf 0

                        val lhs = nextFree.withIndex().sumOf { (i, num) -> num * row[freeButtons[i]] }
                        val rhs = row.last() - lhs

                        rhs / row[leadingX]
                    }
                    if (bestTotal == -1 || nextAllTotal < bestTotal) {
                        best = next
                        bestTotal = nextAllTotal
                    }
                }

                // TODO: Figure out a better way to determine when to cut off the search;
                //  this is an entirely arbitrary cutoff that works on my input,
                //  but I would prefer to have something more mathematical and guaranteed.
                if (best != null && best.total + 10 < nextTotal) {
                    break@outer
                }

                queue.add(next)
            }
        }

        bestTotal
    }

    private data class StateP1(val current: List<Boolean>, val lastFlipped: Int, val totalFlipped: Int)

    private data class StateP2(val total: Int, val currFree: List<Int>, val minIncrement: Int)

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