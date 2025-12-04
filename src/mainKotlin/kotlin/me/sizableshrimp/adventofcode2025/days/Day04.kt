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

import me.sizableshrimp.adventofcode2025.templates.Coordinate
import me.sizableshrimp.adventofcode2025.templates.SeparatedDay
import me.sizableshrimp.adventofcode2025.util.*

class Day04 : SeparatedDay() {
    lateinit var grid: Array<BooleanArray>

    override fun parse() {
        this.grid = this.lines.toBooleanGrid { it == '@' }
    }

    override fun part1() = this.grid.count2D(::isAccessible)

    override fun part2() = generateSequence(0) { part2 ->
        val toAdd = this.grid.count2D { coord, isRoll ->
            if (isAccessible(coord, isRoll)) {
                this.grid[coord] = false
                true
            } else false
        }

        if (toAdd == 0) null else part2 + toAdd
    }.last()

    private fun isAccessible(coord: Coordinate, isRoll: Boolean): Boolean {
        if (!isRoll) return false

        return this.grid.getCardinalOrdinalNeighbors(coord).count { (_, neighbor) -> this.grid[neighbor] } < 4
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Day04().run()
        }

        // Dummy to ensure util package always stays imported
        @Suppress("UNUSED")
        private fun never() = 0.repeat(0)
    }
}