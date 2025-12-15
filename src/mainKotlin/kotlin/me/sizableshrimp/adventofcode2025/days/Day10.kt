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
        val buttonMasks = buttons.map { btn -> btn.sumOf { 1L shl it } }
        val targetMask = target.withIndex().filter { (_, b) ->  b }.sumOf { (i, _) -> 1L shl i }

        fun dfs(totalFlipped: Int, lights: Long, idx: Int = 0, best: Int = Int.MAX_VALUE): Int {
            val nextTotalFlipped = totalFlipped + 1
            if (nextTotalFlipped >= best)
                return best
            var currBest = best

            for (i in idx..<buttonMasks.size) {
                // Flip lights
                val nextLights = lights xor buttonMasks[i]

                currBest = if (nextLights == targetMask) {
                    nextTotalFlipped
                } else {
                    dfs(nextTotalFlipped, nextLights, i + 1, currBest)
                }
            }

            return currBest
        }

        dfs(0, 0L)
    }

    // The general strategy is to convert the button wirings and joltage requirements to a system of linear equations
    // s.t. a valid assignment of button presses x satisfies Ax = b. Then, this system can be represented by the augmented matrix [A | b].
    // Perform Gaussian elimination (using LinearAlgebra#solve) to get the matrix in Hermite normal form.
    // (Effectively, it is reduced row-echelon form, but scaled accordingly by the denominators of the resulting rationals to get an
    // all-integer matrix.) Then, perform a BFS on the remaining free variables to get an assignment that minimizes the total button presses.
    // From talking with others and looking at their solutions, it appears that if you keep track of the square unimodular matrix U
    // that gives H and consider the basis vectors, it's possible to more efficiently solve for the assignment without a BFS.
    override fun part2() = this.machines.sumOf { (_, buttons, joltages) ->
        val matrix = Array(joltages.size) { IntArray(buttons.size + 1) }

        for ((y, jolts) in joltages.withIndex()) {
            matrix[y][matrix[0].lastIndex] = jolts
        }

        for ((x, button) in buttons.withIndex()) {
            for (y in button) {
                matrix[y][x] = 1
            }
        }

        check(LinearAlgebra.solve(matrix))

        val freeButtonsSet = buttons.indices.toMutableSet()
        // val staticButtons = mutableSetOf<Int>()
        // val dependentButtons = mutableSetOf<Int>()
        val leadingXs = IntArray(matrix.size) { -1 }
        var leadingX = 0
        for (y in matrix.indices) {
            val row = matrix[y]
            while (leadingX in row.indices && row[leadingX] == 0)
                leadingX++
            if (leadingX in row.indices && row[leadingX] != 0) {
                leadingXs[y] = leadingX
                freeButtonsSet.remove(leadingX)
                // if (((leadingX + 1)..<(row.size - 1)).all { row[it] == 0 }) {
                //     staticButtons.add(leadingX)
                // } else {
                //     dependentButtons.add(leadingX)
                // }
            }
        }
        val freeButtons = freeButtonsSet.toList()
        val upperBounds = buttons.map { btn -> btn.minOf { joltages.getInt(it) } }

        // println(matrix.contentDeepToString().replace("], [", "]\n["))
        // println("Free vars: ${freeButtons.size}, static vars: ${staticButtons.size}, dependent vars: ${dependentButtons.size}")

        var bestTotal = -1
        var best: State? = null
        val start = State(0, 0.repeat(freeButtons.size), 0)

        var startingFeasible = freeButtons.isEmpty()
        if (!startingFeasible) {
            startingFeasible = true
            for (y in matrix.indices) {
                val row = matrix[y]
                val leadingX = leadingXs[y]
                if (leadingX == -1) continue

                val rhs = row.last()

                if (!(rhs >= 0 && rhs % row[leadingX] == 0)) {
                    startingFeasible = false
                    break
                }
            }
        }

        if (startingFeasible) {
            bestTotal = 0
            for (y in matrix.indices) {
                val row = matrix[y]
                val leadingX = leadingXs[y]
                if (leadingX == -1) continue

                val rhs = row.last()

                bestTotal += rhs / row[leadingX]
            }
            best = start
        }

        if (freeButtons.isEmpty())
            return@sumOf bestTotal

        val queue = ArrayDeque<State>()
        queue.add(start)

        outer@ while (queue.isNotEmpty()) {
            val state = queue.removeLast()

            // Ensure that the assignment of free variables alone is not already worse than the best total (including leading vars) found thus far
            if (bestTotal != -1 && state.total >= bestTotal)
                continue

            inner@ for (i in state.minIncrement..<state.currFree.size) {
                val nextFree = state.currFree.toMutableList()
                if (++nextFree[i] > upperBounds[freeButtons[i]]) continue
                val nextTotal = state.total + 1
                var nextAllTotal = nextTotal

                var isFeasible = true
                for (y in matrix.indices) {
                    val row = matrix[y]
                    val leadingX = leadingXs[y]
                    if (leadingX == -1) continue

                    val leadingVal = row[leadingX]
                    var lhs = 0
                    for (i in nextFree.indices) {
                        lhs += nextFree[i] * row[freeButtons[i]]
                    }
                    val rhs = row.last()
                    val res = rhs - lhs
                    nextAllTotal += res / leadingVal

                    if (res < 0) {
                        var possible = false
                        for (j in i..<freeButtons.size) {
                            if (row[freeButtons[j]] <= 0) {
                                possible = true
                                break
                            }
                        }
                        if (!possible) continue@inner // Impossible
                    } else if (res > leadingVal * upperBounds[leadingX]) {
                        var possible = false
                        for (j in i..<freeButtons.size) {
                            if (row[freeButtons[j]] >= 0) {
                                possible = true
                                break
                            }
                        }
                        if (!possible)
                            continue@inner // Impossible
                    }

                    if (!(res >= 0 && res % leadingVal == 0)) {
                        isFeasible = false
                        break
                    }
                }
                val next = State(nextTotal, nextFree, i)

                if (isFeasible && (bestTotal == -1 || nextAllTotal < bestTotal)) {
                    best = next
                    bestTotal = nextAllTotal
                }

                queue.add(next)
            }
        }

        bestTotal
    }

    private data class State(val total: Int, val currFree: List<Int>, val minIncrement: Int)

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