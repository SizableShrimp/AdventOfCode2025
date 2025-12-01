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
import kotlin.math.abs

class Day01 : Day() {
    // One-liner solution
    // override fun evaluate() =
    //     this.lines.fold(Triple(50, 0, 0)) { (d, p1, p2), l ->
    //         (d + (if (l[0] == 'L') -1 else 1) * l.substring(1).toInt()).let {
    //             it.mod(100).let { m ->
    //                 Triple(
    //                     m,
    //                     p1 + 1 - m.coerceAtMost(1),
    //                     p2 + abs(it) / 100 + if (d != 0 && it <= 0) 1 else 0
    //                 )
    //             }
    //         }
    //     }.toList().drop(1).toResult()

    override fun evaluate(): Result {
        var dial = 50
        var part1 = 0
        var part2 = 0

        this.lines.forEach { l ->
            val num = l.substring(1).toInt()

            val next = dial + (if (l[0] == 'L') -num else num)

            if (dial != 0 && next <= 0)
                part2++

            part2 += abs(next) / 100

            dial = next.mod(100)

            if (dial == 0)
                part1++
        }

        return Result.of(part1, part2)
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Day01().run()
        }

        // Dummy to ensure util package always stays imported
        @Suppress("UNUSED")
        private fun never() = 0.repeat(0)
    }
}