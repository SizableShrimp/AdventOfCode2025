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

import me.sizableshrimp.adventofcode2025.helper.GridHelper
import me.sizableshrimp.adventofcode2025.templates.SeparatedDay
import me.sizableshrimp.adventofcode2025.util.*

class Day12 : SeparatedDay() {
    override fun part1(): Int {
        val (pieces, trees) = this.lines.splitOnBlankLines().let {
            it.subList(0, it.size - 1).map { l ->
                l.drop(1).toBooleanGrid { c -> c == '#' }
            } to it.last().map { l ->
                val (size, counts) = l.split(": ")
                Tree(size.split("x").toInts().toPair(), counts.split(" ").toInts())
            }
        }
        // val allPieces = pieces.flatMapIndexed { i, piece ->
        //     listOf(piece, GridHelper.reflectX(piece), GridHelper.reflectY(piece)).flatMap { p ->
        //         listOf(90, 180, 270).map { d -> GridHelper.rotate(p, d) }
        //     }.distinctBy { it.contentDeepToString() }.map { i to it }
        // }
        val pieceTotals = pieces.map { piece -> GridHelper.countOccurrences(piece, true) }

        return trees.count { tree ->
            if (tree.size.second * tree.size.first < pieceTotals.mapIndexed { i, tot -> tree.counts[i] * tot }.sum())
                return@count false

            true
            // search(
            //     allPieces, Array(tree.size.second) { BooleanArray(tree.size.first) },
            //     Coordinate.ORIGIN, tree.counts.toMutableList()
            // )
        }
    }

    override fun part2() = null // No Part 2 :)

    // private fun search(
    //     allPieces: List<Pair<Int, Array<BooleanArray>>>,
    //     grid: Array<BooleanArray>, pos: Coordinate, counts: MutableList<Int>
    // ): Boolean {
    //     loop@ for ((i, piece) in allPieces) {
    //         if (counts[i] == 0) continue
    //
    //         // Check feasible
    //         piece.forEach2D { x, y, value ->
    //             val next = pos.resolve(x, y)
    //             if (value && (!next.isValid(grid) || grid[pos.y + y][pos.x + x])) {
    //                 continue@loop
    //             }
    //         }
    //
    //         // Place piece and decrease count
    //         piece.forEach2D { x, y, value ->
    //             if (value)
    //                 grid[pos.y + y][pos.x + x] = true
    //         }
    //         counts[i]--
    //
    //         if (counts.all { it == 0 })
    //             return true
    //
    //         if (this.search(allPieces, grid, pos, counts))
    //             return true
    //
    //         // Un-place piece and increment count
    //         piece.forEach2D { x, y, value ->
    //             if (value)
    //                 grid[pos.y + y][pos.x + x] = false
    //         }
    //         counts[i]++
    //     }
    //
    //     var nextPos = pos.right()
    //     // This assumes all pieces are 3x3
    //     if (pos.x == grid[0].size - 2 || !nextPos.isValid(grid)) {
    //         nextPos = Coordinate(0, pos.y + 1)
    //         if (pos.y == grid.size - 2)
    //             return false
    //     }
    //
    //     return nextPos.isValid(grid) && search(allPieces, grid, nextPos, counts)
    // }

    private data class Tree(val size: Pair<Int, Int>, val counts: List<Int>)

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Day12().run()
        }

        // Dummy to ensure util package always stays imported
        @Suppress("UNUSED")
        private fun never() = 0.repeat(0)
    }
}