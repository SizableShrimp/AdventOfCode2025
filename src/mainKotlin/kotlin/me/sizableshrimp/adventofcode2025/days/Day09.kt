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

import me.sizableshrimp.adventofcode2025.helper.Itertools
import me.sizableshrimp.adventofcode2025.templates.Day
import me.sizableshrimp.adventofcode2025.templates.LongCoordinate
import me.sizableshrimp.adventofcode2025.util.*
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class Day09 : Day() {
    override fun evaluate(): Result {
        val coords = this.lines.map { LongCoordinate.parse(it) }
        val horizRanges = mutableMapOf<Long, MutableList<Pair<Long, Long>>>()

        (coords + listOf(coords[0])).windowed(2) { (a, b) ->
            if (a.x == b.x) {
                for (y in (min(a.y, b.y) + 1)..<max(a.y, b.y)) {
                    horizRanges.getOrPut(y, ::mutableListOf).add(a.x to a.x)
                }
            } else {
                horizRanges.getOrPut(a.y, ::mutableListOf).add(min(a.x, b.x) to max(a.x, b.x))
            }
        }

        horizRanges.values.forEach { l -> l.sortBy { (a, _) -> a } }

        val combos = Itertools.combinations(coords, 2)
        var part1 = 0L
        var part2 = 0L

        loop@ for ((a, b) in combos) {
            val dimensions = (b - a)
            val size = (abs(dimensions.x) + 1) * (abs(dimensions.y) + 1)
            part1 = max(part1, size)

            // Don't bother doing an expensive calculation for part 2 if it's impossible to be better
            if (size < part2) continue

            val minX = min(a.x, b.x)
            val minY = min(a.y, b.y)
            val maxX = max(a.x, b.x)
            val maxY = max(a.y, b.y)

            for (y in minY..maxY) {
                val horiz = horizRanges[y]!!
                if (minX < horiz.first().first) continue@loop
                val (evenOdds, wow) = horiz.withIndex().first { (_, l) -> l.first <= minX }
                // If fully contained in the range, we are done
                if (maxX <= wow.second) continue
                // Point-in-polygon (but even and odd is flipped here since first entry in the list means "in", and it has index 0)
                if (evenOdds % 2 == 1) continue@loop
                // Check the next one
                if ((evenOdds + 1) !in horiz.indices || horiz[evenOdds + 1].second < maxX) continue@loop
            }

            part2 = size
        }

        return Result.of(part1, part2)
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Day09().run()
        }

        // Dummy to ensure util package always stays imported
        @Suppress("UNUSED")
        private fun never() = 0.repeat(0)
    }
}