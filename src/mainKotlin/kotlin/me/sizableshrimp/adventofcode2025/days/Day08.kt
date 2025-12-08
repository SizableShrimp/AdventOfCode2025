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

import me.sizableshrimp.adventofcode2025.helper.DisjointSet
import me.sizableshrimp.adventofcode2025.helper.Itertools
import me.sizableshrimp.adventofcode2025.templates.Day
import me.sizableshrimp.adventofcode2025.templates.ZCoordinate
import me.sizableshrimp.adventofcode2025.util.*

class Day08 : Day() {
    override fun evaluate(): Result {
        val coords = this.lines.map { ZCoordinate.parse(it) }
        val dset = DisjointSet(coords.size)
        val pairs = Itertools.combinations(coords.indices.toList(), 2)
        pairs.sortBy { (i, j) -> coords[i].distanceSquared(coords[j]) }
        var part1 = 0

        for ((i, pair) in pairs.withIndex()) {
            val (a, b) = pair
            dset.union(a, b)

            if (i == 1000)
                part1 = dset.resolveSizes().values.sortedDescending().take(3).reduce(Int::times)

            if (dset.numSets == 1)
                return Result.of(part1, coords[a].x.toLong() * coords[b].x)
        }

        error("")
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Day08().run()
        }

        // Dummy to ensure util package always stays imported
        @Suppress("UNUSED")
        private fun never() = 0.repeat(0)
    }
}