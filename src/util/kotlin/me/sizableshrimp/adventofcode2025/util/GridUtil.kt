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

package me.sizableshrimp.adventofcode2025.util

import me.sizableshrimp.adventofcode2025.helper.GridHelper
import me.sizableshrimp.adventofcode2025.templates.Coordinate
import me.sizableshrimp.adventofcode2025.templates.Direction

inline fun <reified T> List<String>.toGrid(func: (Char) -> T) =
    Array(this.size) { y -> Array(this[y].length) { x -> func(this[y][x]) } }

inline fun List<String>.toIntGrid(func: (Char) -> Int) =
    Array(this.size) { y -> IntArray(this[y].length) { x -> func(this[y][x]) } }

inline fun List<String>.toLongGrid(func: (Char) -> Long) =
    Array(this.size) { y -> LongArray(this[y].length) { x -> func(this[y][x]) } }

inline fun List<String>.toCharGrid(func: (Char) -> Char) =
    Array(this.size) { y -> CharArray(this[y].length) { x -> func(this[y][x]) } }

inline fun List<String>.toByteGrid(func: (Char) -> Byte) =
    Array(this.size) { y -> ByteArray(this[y].length) { x -> func(this[y][x]) } }

inline fun List<String>.toDoubleGrid(func: (Char) -> Double) =
    Array(this.size) { y -> DoubleArray(this[y].length) { x -> func(this[y][x]) } }

inline fun List<String>.toFloatGrid(func: (Char) -> Float) =
    Array(this.size) { y -> FloatArray(this[y].length) { x -> func(this[y][x]) } }

inline fun List<String>.toBooleanGrid(func: (Char) -> Boolean) =
    Array(this.size) { y -> BooleanArray(this[y].length) { x -> func(this[y][x]) } }

inline fun <reified T> List<String>.toGridWithCoord(func: (Coordinate, Char) -> T) =
    Array(this.size) { y -> Array(this[y].length) { x -> func(Coordinate.of(x, y), this[y][x]) } }

fun List<String>.findFirstCoord(target: Char): Coordinate? {
    for ((y, row) in this.withIndex()) {
        for ((x, c) in row.withIndex()) {
            if (c == target)
                return Coordinate.of(x, y)
        }
    }

    return null
}

fun List<String>.findAllCoords(target: Char): Set<Coordinate> {
    val set = mutableSetOf<Coordinate>()

    for ((y, row) in this.withIndex()) {
        for ((x, c) in row.withIndex()) {
            if (c == target)
                set.add(Coordinate.of(x, y))
        }
    }

    return set
}

fun List<String>.toCharGrid(): Array<CharArray> = GridHelper.createCharGrid(this)

/**
 * Creates a grid of single-digit integers based on the integer character
 * at each point in the provided grid.
 *
 * @return the 2D grid of single-digit integers
 */
fun List<String>.toIntGrid(): Array<IntArray> = GridHelper.createIntGrid(this)

operator fun <T> Array<Array<T>>.get(coord: Coordinate) = this[coord.y][coord.x]
operator fun Array<IntArray>.get(coord: Coordinate) = this[coord.y][coord.x]
operator fun Array<LongArray>.get(coord: Coordinate) = this[coord.y][coord.x]
operator fun Array<CharArray>.get(coord: Coordinate) = this[coord.y][coord.x]
operator fun Array<ByteArray>.get(coord: Coordinate) = this[coord.y][coord.x]
operator fun Array<DoubleArray>.get(coord: Coordinate) = this[coord.y][coord.x]
operator fun Array<FloatArray>.get(coord: Coordinate) = this[coord.y][coord.x]
operator fun Array<BooleanArray>.get(coord: Coordinate) = this[coord.y][coord.x]

inline operator fun <reified T> Array<Array<T>>.set(coord: Coordinate, value: T) {
    this[coord.y][coord.x] = value
}

// region Array<Array>#set for primitives
operator fun Array<IntArray>.set(coord: Coordinate, value: Int) {
    this[coord.y][coord.x] = value
}

operator fun Array<LongArray>.set(coord: Coordinate, value: Long) {
    this[coord.y][coord.x] = value
}

operator fun Array<CharArray>.set(coord: Coordinate, value: Char) {
    this[coord.y][coord.x] = value
}

operator fun Array<ByteArray>.set(coord: Coordinate, value: Byte) {
    this[coord.y][coord.x] = value
}

operator fun Array<DoubleArray>.set(coord: Coordinate, value: Double) {
    this[coord.y][coord.x] = value
}

operator fun Array<FloatArray>.set(coord: Coordinate, value: Float) {
    this[coord.y][coord.x] = value
}

operator fun Array<BooleanArray>.set(coord: Coordinate, value: Boolean) {
    this[coord.y][coord.x] = value
}
// endregion

fun <T> Array<Array<T>>.getCardinalNeighbors(coord: Coordinate) = Iterable {
    iterator<Pair<Direction, Coordinate>> {
        for (dir in Direction.cardinalDirections()) {
            val next = coord.resolve(dir)
            if (GridHelper.isValid(this@getCardinalNeighbors, next))
                yield(dir to next)
        }
    }
}

fun <T> Array<Array<T>>.getOrdinalNeighbors(coord: Coordinate) = Iterable {
    iterator<Pair<Direction, Coordinate>> {
        for (dir in Direction.ordinalDirections()) {
            val next = coord.resolve(dir)
            if (GridHelper.isValid(this@getOrdinalNeighbors, next))
                yield(dir to next)
        }
    }
}

fun <T> Array<Array<T>>.getCardinalOrdinalNeighbors(coord: Coordinate) = Iterable {
    iterator<Pair<Direction, Coordinate>> {
        for (dir in Direction.cardinalOrdinalDirections()) {
            val next = coord.resolve(dir)
            if (GridHelper.isValid(this@getCardinalOrdinalNeighbors, next))
                yield(dir to next)
        }
    }
}

// region Array<Array>#getCardinalNeighbors, getOrdinalNeighbors, getCardinalOrdinalNeighbors for primitives
fun Array<IntArray>.getCardinalNeighbors(coord: Coordinate) = Iterable {
    iterator<Pair<Direction, Coordinate>> {
        for (dir in Direction.cardinalDirections()) {
            val next = coord.resolve(dir)
            if (GridHelper.isValid(this@getCardinalNeighbors, next))
                yield(dir to next)
        }
    }
}

fun Array<IntArray>.getOrdinalNeighbors(coord: Coordinate) = Iterable {
    iterator<Pair<Direction, Coordinate>> {
        for (dir in Direction.ordinalDirections()) {
            val next = coord.resolve(dir)
            if (GridHelper.isValid(this@getOrdinalNeighbors, next))
                yield(dir to next)
        }
    }
}

fun Array<IntArray>.getCardinalOrdinalNeighbors(coord: Coordinate) = Iterable {
    iterator<Pair<Direction, Coordinate>> {
        for (dir in Direction.cardinalOrdinalDirections()) {
            val next = coord.resolve(dir)
            if (GridHelper.isValid(this@getCardinalOrdinalNeighbors, next))
                yield(dir to next)
        }
    }
}

fun Array<LongArray>.getCardinalNeighbors(coord: Coordinate) = Iterable {
    iterator<Pair<Direction, Coordinate>> {
        for (dir in Direction.cardinalDirections()) {
            val next = coord.resolve(dir)
            if (GridHelper.isValid(this@getCardinalNeighbors, next))
                yield(dir to next)
        }
    }
}

fun Array<LongArray>.getOrdinalNeighbors(coord: Coordinate) = Iterable {
    iterator<Pair<Direction, Coordinate>> {
        for (dir in Direction.ordinalDirections()) {
            val next = coord.resolve(dir)
            if (GridHelper.isValid(this@getOrdinalNeighbors, next))
                yield(dir to next)
        }
    }
}

fun Array<LongArray>.getCardinalOrdinalNeighbors(coord: Coordinate) = Iterable {
    iterator<Pair<Direction, Coordinate>> {
        for (dir in Direction.cardinalOrdinalDirections()) {
            val next = coord.resolve(dir)
            if (GridHelper.isValid(this@getCardinalOrdinalNeighbors, next))
                yield(dir to next)
        }
    }
}

fun Array<CharArray>.getCardinalNeighbors(coord: Coordinate) = Iterable {
    iterator<Pair<Direction, Coordinate>> {
        for (dir in Direction.cardinalDirections()) {
            val next = coord.resolve(dir)
            if (GridHelper.isValid(this@getCardinalNeighbors, next))
                yield(dir to next)
        }
    }
}

fun Array<CharArray>.getOrdinalNeighbors(coord: Coordinate) = Iterable {
    iterator<Pair<Direction, Coordinate>> {
        for (dir in Direction.ordinalDirections()) {
            val next = coord.resolve(dir)
            if (GridHelper.isValid(this@getOrdinalNeighbors, next))
                yield(dir to next)
        }
    }
}

fun Array<CharArray>.getCardinalOrdinalNeighbors(coord: Coordinate) = Iterable {
    iterator<Pair<Direction, Coordinate>> {
        for (dir in Direction.cardinalOrdinalDirections()) {
            val next = coord.resolve(dir)
            if (GridHelper.isValid(this@getCardinalOrdinalNeighbors, next))
                yield(dir to next)
        }
    }
}

fun Array<ByteArray>.getCardinalNeighbors(coord: Coordinate) = Iterable {
    iterator<Pair<Direction, Coordinate>> {
        for (dir in Direction.cardinalDirections()) {
            val next = coord.resolve(dir)
            if (GridHelper.isValid(this@getCardinalNeighbors, next))
                yield(dir to next)
        }
    }
}

fun Array<ByteArray>.getOrdinalNeighbors(coord: Coordinate) = Iterable {
    iterator<Pair<Direction, Coordinate>> {
        for (dir in Direction.ordinalDirections()) {
            val next = coord.resolve(dir)
            if (GridHelper.isValid(this@getOrdinalNeighbors, next))
                yield(dir to next)
        }
    }
}

fun Array<ByteArray>.getCardinalOrdinalNeighbors(coord: Coordinate) = Iterable {
    iterator<Pair<Direction, Coordinate>> {
        for (dir in Direction.cardinalOrdinalDirections()) {
            val next = coord.resolve(dir)
            if (GridHelper.isValid(this@getCardinalOrdinalNeighbors, next))
                yield(dir to next)
        }
    }
}

fun Array<DoubleArray>.getCardinalNeighbors(coord: Coordinate) = Iterable {
    iterator<Pair<Direction, Coordinate>> {
        for (dir in Direction.cardinalDirections()) {
            val next = coord.resolve(dir)
            if (GridHelper.isValid(this@getCardinalNeighbors, next))
                yield(dir to next)
        }
    }
}

fun Array<DoubleArray>.getOrdinalNeighbors(coord: Coordinate) = Iterable {
    iterator<Pair<Direction, Coordinate>> {
        for (dir in Direction.ordinalDirections()) {
            val next = coord.resolve(dir)
            if (GridHelper.isValid(this@getOrdinalNeighbors, next))
                yield(dir to next)
        }
    }
}

fun Array<DoubleArray>.getCardinalOrdinalNeighbors(coord: Coordinate) = Iterable {
    iterator<Pair<Direction, Coordinate>> {
        for (dir in Direction.cardinalOrdinalDirections()) {
            val next = coord.resolve(dir)
            if (GridHelper.isValid(this@getCardinalOrdinalNeighbors, next))
                yield(dir to next)
        }
    }
}

fun Array<FloatArray>.getCardinalNeighbors(coord: Coordinate) = Iterable {
    iterator<Pair<Direction, Coordinate>> {
        for (dir in Direction.cardinalDirections()) {
            val next = coord.resolve(dir)
            if (GridHelper.isValid(this@getCardinalNeighbors, next))
                yield(dir to next)
        }
    }
}

fun Array<FloatArray>.getOrdinalNeighbors(coord: Coordinate) = Iterable {
    iterator<Pair<Direction, Coordinate>> {
        for (dir in Direction.ordinalDirections()) {
            val next = coord.resolve(dir)
            if (GridHelper.isValid(this@getOrdinalNeighbors, next))
                yield(dir to next)
        }
    }
}

fun Array<FloatArray>.getCardinalOrdinalNeighbors(coord: Coordinate) = Iterable {
    iterator<Pair<Direction, Coordinate>> {
        for (dir in Direction.cardinalOrdinalDirections()) {
            val next = coord.resolve(dir)
            if (GridHelper.isValid(this@getCardinalOrdinalNeighbors, next))
                yield(dir to next)
        }
    }
}

fun Array<BooleanArray>.getCardinalNeighbors(coord: Coordinate) = Iterable {
    iterator<Pair<Direction, Coordinate>> {
        for (dir in Direction.cardinalDirections()) {
            val next = coord.resolve(dir)
            if (GridHelper.isValid(this@getCardinalNeighbors, next))
                yield(dir to next)
        }
    }
}

fun Array<BooleanArray>.getOrdinalNeighbors(coord: Coordinate) = Iterable {
    iterator<Pair<Direction, Coordinate>> {
        for (dir in Direction.ordinalDirections()) {
            val next = coord.resolve(dir)
            if (GridHelper.isValid(this@getOrdinalNeighbors, next))
                yield(dir to next)
        }
    }
}

fun Array<BooleanArray>.getCardinalOrdinalNeighbors(coord: Coordinate) = Iterable {
    iterator<Pair<Direction, Coordinate>> {
        for (dir in Direction.cardinalOrdinalDirections()) {
            val next = coord.resolve(dir)
            if (GridHelper.isValid(this@getCardinalOrdinalNeighbors, next))
                yield(dir to next)
        }
    }
}
// endregion

private class GridIterator<T>(private val height: Int, private val width: Int, private val getter: (Coordinate) -> T) : Iterator<Pair<Coordinate, T>> {
    private var y = 0
    private var x = 0

    override fun hasNext(): Boolean {
        return this.y < this.height
    }

    override fun next(): Pair<Coordinate, T> {
        val coord = Coordinate.of(this.x, this.y)
        val value = this.getter(coord)
        this.x++

        if (this.x >= this.width) {
            this.x = 0
            this.y++
        }

        return coord to value
    }
}

fun <T> Array<Array<T>>.withCoordinate(): Iterable<Pair<Coordinate, T>> = Iterable { GridIterator(this.size, this[0].size, ::get) }

// region Array<Array>#withCoordinate for primitives
fun Array<IntArray>.withCoordinate(): Iterable<Pair<Coordinate, Int>> = Iterable { GridIterator(this.size, this[0].size, ::get) }
fun Array<LongArray>.withCoordinate(): Iterable<Pair<Coordinate, Long>> = Iterable { GridIterator(this.size, this[0].size, ::get) }
fun Array<CharArray>.withCoordinate(): Iterable<Pair<Coordinate, Char>> = Iterable { GridIterator(this.size, this[0].size, ::get) }
fun Array<ByteArray>.withCoordinate(): Iterable<Pair<Coordinate, Byte>> = Iterable { GridIterator(this.size, this[0].size, ::get) }
fun Array<DoubleArray>.withCoordinate(): Iterable<Pair<Coordinate, Double>> = Iterable { GridIterator(this.size, this[0].size, ::get) }
fun Array<FloatArray>.withCoordinate(): Iterable<Pair<Coordinate, Float>> = Iterable { GridIterator(this.size, this[0].size, ::get) }
fun Array<BooleanArray>.withCoordinate(): Iterable<Pair<Coordinate, Boolean>> = Iterable { GridIterator(this.size, this[0].size, ::get) }
// endregion

inline fun <T> Array<Array<T>>.forEach2D(action: (Coordinate, T) -> Unit) {
    for ((y, row) in this.withIndex()) {
        for ((x, t) in row.withIndex()) {
            action(Coordinate.of(x, y), t)
        }
    }
}

inline fun <T> Array<Array<T>>.forEach2D(action: (x: Int, y: Int, T) -> Unit) {
    for ((y, row) in this.withIndex()) {
        for ((x, t) in row.withIndex()) {
            action(x, y, t)
        }
    }
}

// region Array<Array>#forEach2D for primitives
inline fun Array<IntArray>.forEach2D(action: (Coordinate, Int) -> Unit) {
    for ((y, row) in this.withIndex()) {
        for ((x, t) in row.withIndex()) {
            action(Coordinate.of(x, y), t)
        }
    }
}

inline fun Array<IntArray>.forEach2D(action: (x: Int, y: Int, Int) -> Unit) {
    for ((y, row) in this.withIndex()) {
        for ((x, t) in row.withIndex()) {
            action(x, y, t)
        }
    }
}

inline fun Array<LongArray>.forEach2D(action: (Coordinate, Long) -> Unit) {
    for ((y, row) in this.withIndex()) {
        for ((x, t) in row.withIndex()) {
            action(Coordinate.of(x, y), t)
        }
    }
}

inline fun Array<LongArray>.forEach2D(action: (x: Int, y: Int, Long) -> Unit) {
    for ((y, row) in this.withIndex()) {
        for ((x, t) in row.withIndex()) {
            action(x, y, t)
        }
    }
}

inline fun Array<CharArray>.forEach2D(action: (Coordinate, Char) -> Unit) {
    for ((y, row) in this.withIndex()) {
        for ((x, t) in row.withIndex()) {
            action(Coordinate.of(x, y), t)
        }
    }
}

inline fun Array<CharArray>.forEach2D(action: (x: Int, y: Int, Char) -> Unit) {
    for ((y, row) in this.withIndex()) {
        for ((x, t) in row.withIndex()) {
            action(x, y, t)
        }
    }
}

inline fun Array<ByteArray>.forEach2D(action: (Coordinate, Byte) -> Unit) {
    for ((y, row) in this.withIndex()) {
        for ((x, t) in row.withIndex()) {
            action(Coordinate.of(x, y), t)
        }
    }
}

inline fun Array<ByteArray>.forEach2D(action: (x: Int, y: Int, Byte) -> Unit) {
    for ((y, row) in this.withIndex()) {
        for ((x, t) in row.withIndex()) {
            action(x, y, t)
        }
    }
}

inline fun Array<DoubleArray>.forEach2D(action: (Coordinate, Double) -> Unit) {
    for ((y, row) in this.withIndex()) {
        for ((x, t) in row.withIndex()) {
            action(Coordinate.of(x, y), t)
        }
    }
}

inline fun Array<DoubleArray>.forEach2D(action: (x: Int, y: Int, Double) -> Unit) {
    for ((y, row) in this.withIndex()) {
        for ((x, t) in row.withIndex()) {
            action(x, y, t)
        }
    }
}

inline fun Array<FloatArray>.forEach2D(action: (Coordinate, Float) -> Unit) {
    for ((y, row) in this.withIndex()) {
        for ((x, t) in row.withIndex()) {
            action(Coordinate.of(x, y), t)
        }
    }
}

inline fun Array<FloatArray>.forEach2D(action: (x: Int, y: Int, Float) -> Unit) {
    for ((y, row) in this.withIndex()) {
        for ((x, t) in row.withIndex()) {
            action(x, y, t)
        }
    }
}

inline fun Array<BooleanArray>.forEach2D(action: (Coordinate, Boolean) -> Unit) {
    for ((y, row) in this.withIndex()) {
        for ((x, t) in row.withIndex()) {
            action(Coordinate.of(x, y), t)
        }
    }
}

inline fun Array<BooleanArray>.forEach2D(action: (x: Int, y: Int, Boolean) -> Unit) {
    for ((y, row) in this.withIndex()) {
        for ((x, t) in row.withIndex()) {
            action(x, y, t)
        }
    }
}
// endregion

inline fun <T, R> Array<Array<T>>.map2D(transform: (Coordinate, T) -> R): List<R> {
    val list = mutableListOf<R>()

    this.forEach2D { coord, t ->
        list.add(transform(coord, t))
    }

    return list
}

// region Array<Array>#map2D for primitives
inline fun <R> Array<IntArray>.map2D(transform: (Coordinate, Int) -> R): List<R> {
    val list = mutableListOf<R>()

    this.forEach2D { coord, t ->
        list.add(transform(coord, t))
    }

    return list
}

inline fun <R> Array<IntArray>.map2D(transform: (x: Int, y: Int, Int) -> R): List<R> {
    val list = mutableListOf<R>()

    this.forEach2D { x, y, t ->
        list.add(transform(x, y, t))
    }

    return list
}

inline fun <R> Array<LongArray>.map2D(transform: (Coordinate, Long) -> R): List<R> {
    val list = mutableListOf<R>()

    this.forEach2D { coord, t ->
        list.add(transform(coord, t))
    }

    return list
}

inline fun <R> Array<LongArray>.map2D(transform: (x: Int, y: Int, Long) -> R): List<R> {
    val list = mutableListOf<R>()

    this.forEach2D { x, y, t ->
        list.add(transform(x, y, t))
    }

    return list
}

inline fun <R> Array<CharArray>.map2D(transform: (Coordinate, Char) -> R): List<R> {
    val list = mutableListOf<R>()

    this.forEach2D { coord, t ->
        list.add(transform(coord, t))
    }

    return list
}

inline fun <R> Array<CharArray>.map2D(transform: (x: Int, y: Int, Char) -> R): List<R> {
    val list = mutableListOf<R>()

    this.forEach2D { x, y, t ->
        list.add(transform(x, y, t))
    }

    return list
}

inline fun <R> Array<ByteArray>.map2D(transform: (Coordinate, Byte) -> R): List<R> {
    val list = mutableListOf<R>()

    this.forEach2D { coord, t ->
        list.add(transform(coord, t))
    }

    return list
}

inline fun <R> Array<ByteArray>.map2D(transform: (x: Int, y: Int, Byte) -> R): List<R> {
    val list = mutableListOf<R>()

    this.forEach2D { x, y, t ->
        list.add(transform(x, y, t))
    }

    return list
}

inline fun <R> Array<DoubleArray>.map2D(transform: (Coordinate, Double) -> R): List<R> {
    val list = mutableListOf<R>()

    this.forEach2D { coord, t ->
        list.add(transform(coord, t))
    }

    return list
}

inline fun <R> Array<DoubleArray>.map2D(transform: (x: Int, y: Int, Double) -> R): List<R> {
    val list = mutableListOf<R>()

    this.forEach2D { x, y, t ->
        list.add(transform(x, y, t))
    }

    return list
}

inline fun <R> Array<FloatArray>.map2D(transform: (Coordinate, Float) -> R): List<R> {
    val list = mutableListOf<R>()

    this.forEach2D { coord, t ->
        list.add(transform(coord, t))
    }

    return list
}

inline fun <R> Array<FloatArray>.map2D(transform: (x: Int, y: Int, Float) -> R): List<R> {
    val list = mutableListOf<R>()

    this.forEach2D { x, y, t ->
        list.add(transform(x, y, t))
    }

    return list
}

inline fun <R> Array<BooleanArray>.map2D(transform: (Coordinate, Boolean) -> R): List<R> {
    val list = mutableListOf<R>()

    this.forEach2D { coord, t ->
        list.add(transform(coord, t))
    }

    return list
}

inline fun <R> Array<BooleanArray>.map2D(transform: (x: Int, y: Int, Boolean) -> R): List<R> {
    val list = mutableListOf<R>()

    this.forEach2D { x, y, t ->
        list.add(transform(x, y, t))
    }

    return list
}
// endregion

inline fun <T> Array<Array<T>>.filter2D(predicate: (Coordinate, T) -> Boolean): List<Pair<Coordinate, T>> {
    val list = mutableListOf<Pair<Coordinate, T>>()

    this.forEach2D { coord, t ->
        if (predicate(coord, t))
            list.add(coord to t)
    }

    return list
}

inline fun <T> Array<Array<T>>.filter2D(predicate: (x: Int, y: Int, T) -> Boolean): List<Pair<Coordinate, T>> {
    val list = mutableListOf<Pair<Coordinate, T>>()

    this.forEach2D { x, y, t ->
        if (predicate(x, y, t))
            list.add(Coordinate.of(x, y) to t)
    }

    return list
}

// region Array<Array>#filter2D for primitives
inline fun Array<IntArray>.filter2D(predicate: (Coordinate, Int) -> Boolean): List<Pair<Coordinate, Int>> {
    val list = mutableListOf<Pair<Coordinate, Int>>()

    this.forEach2D { coord, t ->
        if (predicate(coord, t))
            list.add(coord to t)
    }

    return list
}

inline fun Array<IntArray>.filter2D(predicate: (x: Int, y: Int, Int) -> Boolean): List<Pair<Coordinate, Int>> {
    val list = mutableListOf<Pair<Coordinate, Int>>()

    this.forEach2D { x, y, t ->
        if (predicate(x, y, t))
            list.add(Coordinate.of(x, y) to t)
    }

    return list
}

inline fun Array<LongArray>.filter2D(predicate: (Coordinate, Long) -> Boolean): List<Pair<Coordinate, Long>> {
    val list = mutableListOf<Pair<Coordinate, Long>>()

    this.forEach2D { coord, t ->
        if (predicate(coord, t))
            list.add(coord to t)
    }

    return list
}

inline fun Array<LongArray>.filter2D(predicate: (x: Int, y: Int, Long) -> Boolean): List<Pair<Coordinate, Long>> {
    val list = mutableListOf<Pair<Coordinate, Long>>()

    this.forEach2D { x, y, t ->
        if (predicate(x, y, t))
            list.add(Coordinate.of(x, y) to t)
    }

    return list
}

inline fun Array<CharArray>.filter2D(predicate: (Coordinate, Char) -> Boolean): List<Pair<Coordinate, Char>> {
    val list = mutableListOf<Pair<Coordinate, Char>>()

    this.forEach2D { coord, t ->
        if (predicate(coord, t))
            list.add(coord to t)
    }

    return list
}

inline fun Array<CharArray>.filter2D(predicate: (x: Int, y: Int, Char) -> Boolean): List<Pair<Coordinate, Char>> {
    val list = mutableListOf<Pair<Coordinate, Char>>()

    this.forEach2D { x, y, t ->
        if (predicate(x, y, t))
            list.add(Coordinate.of(x, y) to t)
    }

    return list
}

inline fun Array<ByteArray>.filter2D(predicate: (Coordinate, Byte) -> Boolean): List<Pair<Coordinate, Byte>> {
    val list = mutableListOf<Pair<Coordinate, Byte>>()

    this.forEach2D { coord, t ->
        if (predicate(coord, t))
            list.add(coord to t)
    }

    return list
}

inline fun Array<ByteArray>.filter2D(predicate: (x: Int, y: Int, Byte) -> Boolean): List<Pair<Coordinate, Byte>> {
    val list = mutableListOf<Pair<Coordinate, Byte>>()

    this.forEach2D { x, y, t ->
        if (predicate(x, y, t))
            list.add(Coordinate.of(x, y) to t)
    }

    return list
}

inline fun Array<DoubleArray>.filter2D(predicate: (Coordinate, Double) -> Boolean): List<Pair<Coordinate, Double>> {
    val list = mutableListOf<Pair<Coordinate, Double>>()

    this.forEach2D { coord, t ->
        if (predicate(coord, t))
            list.add(coord to t)
    }

    return list
}

inline fun Array<DoubleArray>.filter2D(predicate: (x: Int, y: Int, Double) -> Boolean): List<Pair<Coordinate, Double>> {
    val list = mutableListOf<Pair<Coordinate, Double>>()

    this.forEach2D { x, y, t ->
        if (predicate(x, y, t))
            list.add(Coordinate.of(x, y) to t)
    }

    return list
}

inline fun Array<FloatArray>.filter2D(predicate: (Coordinate, Float) -> Boolean): List<Pair<Coordinate, Float>> {
    val list = mutableListOf<Pair<Coordinate, Float>>()

    this.forEach2D { coord, t ->
        if (predicate(coord, t))
            list.add(coord to t)
    }

    return list
}

inline fun Array<FloatArray>.filter2D(predicate: (x: Int, y: Int, Float) -> Boolean): List<Pair<Coordinate, Float>> {
    val list = mutableListOf<Pair<Coordinate, Float>>()

    this.forEach2D { x, y, t ->
        if (predicate(x, y, t))
            list.add(Coordinate.of(x, y) to t)
    }

    return list
}

inline fun Array<BooleanArray>.filter2D(predicate: (Coordinate, Boolean) -> Boolean): List<Pair<Coordinate, Boolean>> {
    val list = mutableListOf<Pair<Coordinate, Boolean>>()

    this.forEach2D { coord, t ->
        if (predicate(coord, t))
            list.add(coord to t)
    }

    return list
}

inline fun Array<BooleanArray>.filter2D(predicate: (x: Int, y: Int, Boolean) -> Boolean): List<Pair<Coordinate, Boolean>> {
    val list = mutableListOf<Pair<Coordinate, Boolean>>()

    this.forEach2D { x, y, t ->
        if (predicate(x, y, t))
            list.add(Coordinate.of(x, y) to t)
    }

    return list
}
// endregion

inline fun <T> Array<Array<T>>.sumOf2D(selector: (Coordinate, T) -> Int): Int {
    var sum = 0

    this.forEach2D { coord, t ->
        sum += selector(coord, t)
    }

    return sum
}

inline fun <T> Array<Array<T>>.sumOf2D(selector: (x: Int, y: Int, T) -> Int): Int {
    var sum = 0

    this.forEach2D { x, y, t ->
        sum += selector(x, y, t)
    }

    return sum
}

@OptIn(kotlin.experimental.ExperimentalTypeInference::class)
@OverloadResolutionByLambdaReturnType
inline fun <T> Array<Array<T>>.sumOf2D(selector: (Coordinate, T) -> Long): Long {
    var sum = 0L

    this.forEach2D { coord, t ->
        sum += selector(coord, t)
    }

    return sum
}

@OptIn(kotlin.experimental.ExperimentalTypeInference::class)
@OverloadResolutionByLambdaReturnType
inline fun <T> Array<Array<T>>.sumOf2D(selector: (x: Int, y: Int, T) -> Long): Long {
    var sum = 0L

    this.forEach2D { x, y, t ->
        sum += selector(x, y, t)
    }

    return sum
}

// region Array<Array>#sumOf2D for primitives
inline fun Array<IntArray>.sumOf2D(selector: (Coordinate, Int) -> Int): Int {
    var sum = 0

    this.forEach2D { coord, t ->
        sum += selector(coord, t)
    }

    return sum
}

inline fun Array<IntArray>.sumOf2D(selector: (x: Int, y: Int, Int) -> Int): Int {
    var sum = 0

    this.forEach2D { x, y, t ->
        sum += selector(x, y, t)
    }

    return sum
}

@OptIn(kotlin.experimental.ExperimentalTypeInference::class)
@OverloadResolutionByLambdaReturnType
inline fun Array<IntArray>.sumOf2D(selector: (Coordinate, Int) -> Long): Long {
    var sum = 0L

    this.forEach2D { coord, t ->
        sum += selector(coord, t)
    }

    return sum
}

@OptIn(kotlin.experimental.ExperimentalTypeInference::class)
@OverloadResolutionByLambdaReturnType
inline fun Array<IntArray>.sumOf2D(selector: (x: Int, y: Int, Int) -> Long): Long {
    var sum = 0L

    this.forEach2D { x, y, t ->
        sum += selector(x, y, t)
    }

    return sum
}

@OptIn(kotlin.experimental.ExperimentalTypeInference::class)
@OverloadResolutionByLambdaReturnType
inline fun Array<LongArray>.sumOf2D(selector: (Coordinate, Long) -> Long): Long {
    var sum = 0L

    this.forEach2D { coord, t ->
        sum += selector(coord, t)
    }

    return sum
}

@OptIn(kotlin.experimental.ExperimentalTypeInference::class)
@OverloadResolutionByLambdaReturnType
inline fun Array<LongArray>.sumOf2D(selector: (x: Int, y: Int, Long) -> Long): Long {
    var sum = 0L

    this.forEach2D { x, y, t ->
        sum += selector(x, y, t)
    }

    return sum
}

inline fun Array<CharArray>.sumOf2D(selector: (Coordinate, Char) -> Int): Int {
    var sum = 0

    this.forEach2D { coord, t ->
        sum += selector(coord, t)
    }

    return sum
}

inline fun Array<CharArray>.sumOf2D(selector: (x: Int, y: Int, Char) -> Int): Int {
    var sum = 0

    this.forEach2D { x, y, t ->
        sum += selector(x, y, t)
    }

    return sum
}

@OptIn(kotlin.experimental.ExperimentalTypeInference::class)
@OverloadResolutionByLambdaReturnType
inline fun Array<CharArray>.sumOf2D(selector: (Coordinate, Char) -> Long): Long {
    var sum = 0L

    this.forEach2D { coord, t ->
        sum += selector(coord, t)
    }

    return sum
}

@OptIn(kotlin.experimental.ExperimentalTypeInference::class)
@OverloadResolutionByLambdaReturnType
inline fun Array<CharArray>.sumOf2D(selector: (x: Int, y: Int, Char) -> Long): Long {
    var sum = 0L

    this.forEach2D { x, y, t ->
        sum += selector(x, y, t)
    }

    return sum
}

inline fun Array<ByteArray>.sumOf2D(selector: (Coordinate, Byte) -> Int): Int {
    var sum = 0

    this.forEach2D { coord, t ->
        sum += selector(coord, t)
    }

    return sum
}

inline fun Array<ByteArray>.sumOf2D(selector: (x: Int, y: Int, Byte) -> Int): Int {
    var sum = 0

    this.forEach2D { x, y, t ->
        sum += selector(x, y, t)
    }

    return sum
}

@OptIn(kotlin.experimental.ExperimentalTypeInference::class)
@OverloadResolutionByLambdaReturnType
inline fun Array<ByteArray>.sumOf2D(selector: (Coordinate, Byte) -> Long): Long {
    var sum = 0L

    this.forEach2D { coord, t ->
        sum += selector(coord, t)
    }

    return sum
}

@OptIn(kotlin.experimental.ExperimentalTypeInference::class)
@OverloadResolutionByLambdaReturnType
inline fun Array<ByteArray>.sumOf2D(selector: (x: Int, y: Int, Byte) -> Long): Long {
    var sum = 0L

    this.forEach2D { x, y, t ->
        sum += selector(x, y, t)
    }

    return sum
}

inline fun Array<DoubleArray>.sumOf2D(selector: (Coordinate, Double) -> Double): Double {
    var sum = 0.0

    this.forEach2D { coord, t ->
        sum += selector(coord, t)
    }

    return sum
}

inline fun Array<DoubleArray>.sumOf2D(selector: (x: Int, y: Int, Double) -> Double): Double {
    var sum = 0.0

    this.forEach2D { x, y, t ->
        sum += selector(x, y, t)
    }

    return sum
}

inline fun Array<FloatArray>.sumOf2D(selector: (Coordinate, Float) -> Float): Float {
    var sum = 0.0F

    this.forEach2D { coord, t ->
        sum += selector(coord, t)
    }

    return sum
}

inline fun Array<FloatArray>.sumOf2D(selector: (x: Int, y: Int, Float) -> Float): Float {
    var sum = 0.0F

    this.forEach2D { x, y, t ->
        sum += selector(x, y, t)
    }

    return sum
}

inline fun Array<BooleanArray>.sumOf2D(selector: (Coordinate, Boolean) -> Int): Int {
    var sum = 0

    this.forEach2D { coord, t ->
        sum += selector(coord, t)
    }

    return sum
}

inline fun Array<BooleanArray>.sumOf2D(selector: (x: Int, y: Int, Boolean) -> Int): Int {
    var sum = 0

    this.forEach2D { x, y, t ->
        sum += selector(x, y, t)
    }

    return sum
}

@OptIn(kotlin.experimental.ExperimentalTypeInference::class)
@OverloadResolutionByLambdaReturnType
inline fun Array<BooleanArray>.sumOf2D(selector: (Coordinate, Boolean) -> Long): Long {
    var sum = 0L

    this.forEach2D { coord, t ->
        sum += selector(coord, t)
    }

    return sum
}

@OptIn(kotlin.experimental.ExperimentalTypeInference::class)
@OverloadResolutionByLambdaReturnType
inline fun Array<BooleanArray>.sumOf2D(selector: (x: Int, y: Int, Boolean) -> Long): Long {
    var sum = 0L

    this.forEach2D { x, y, t ->
        sum += selector(x, y, t)
    }

    return sum
}
// endregion

// region Array<Array>#count2D for primitives
inline fun Array<IntArray>.count2D(selector: (Coordinate, Int) -> Boolean): Int {
    var sum = 0

    this.forEach2D { coord, t ->
        if (selector(coord, t))
            sum++
    }

    return sum
}

inline fun Array<IntArray>.count2D(selector: (x: Int, y: Int, Int) -> Boolean): Int {
    var sum = 0

    this.forEach2D { x, y, t ->
        if (selector(x, y, t))
            sum++
    }

    return sum
}

inline fun Array<LongArray>.count2D(selector: (Coordinate, Long) -> Boolean): Int {
    var sum = 0

    this.forEach2D { coord, t ->
        if (selector(coord, t))
            sum++
    }

    return sum
}

inline fun Array<LongArray>.count2D(selector: (x: Int, y: Int, Long) -> Boolean): Int {
    var sum = 0

    this.forEach2D { x, y, t ->
        if (selector(x, y, t))
            sum++
    }

    return sum
}

inline fun Array<CharArray>.count2D(selector: (Coordinate, Char) -> Boolean): Int {
    var sum = 0

    this.forEach2D { coord, t ->
        if (selector(coord, t))
            sum++
    }

    return sum
}

inline fun Array<CharArray>.count2D(selector: (x: Int, y: Int, Char) -> Boolean): Int {
    var sum = 0

    this.forEach2D { x, y, t ->
        if (selector(x, y, t))
            sum++
    }

    return sum
}

inline fun Array<ByteArray>.count2D(selector: (Coordinate, Byte) -> Boolean): Int {
    var sum = 0

    this.forEach2D { coord, t ->
        if (selector(coord, t))
            sum++
    }

    return sum
}

inline fun Array<ByteArray>.count2D(selector: (x: Int, y: Int, Byte) -> Boolean): Int {
    var sum = 0

    this.forEach2D { x, y, t ->
        if (selector(x, y, t))
            sum++
    }

    return sum
}

inline fun Array<DoubleArray>.count2D(selector: (Coordinate, Double) -> Boolean): Int {
    var sum = 0

    this.forEach2D { coord, t ->
        if (selector(coord, t))
            sum++
    }

    return sum
}

inline fun Array<DoubleArray>.count2D(selector: (x: Int, y: Int, Double) -> Boolean): Int {
    var sum = 0

    this.forEach2D { x, y, t ->
        if (selector(x, y, t))
            sum++
    }

    return sum
}

inline fun Array<FloatArray>.count2D(selector: (Coordinate, Float) -> Boolean): Int {
    var sum = 0

    this.forEach2D { coord, t ->
        if (selector(coord, t))
            sum++
    }

    return sum
}

inline fun Array<FloatArray>.count2D(selector: (x: Int, y: Int, Float) -> Boolean): Int {
    var sum = 0

    this.forEach2D { x, y, t ->
        if (selector(x, y, t))
            sum++
    }

    return sum
}

inline fun Array<BooleanArray>.count2D(selector: (Coordinate, Boolean) -> Boolean): Int {
    var sum = 0

    this.forEach2D { coord, t ->
        if (selector(coord, t))
            sum++
    }

    return sum
}

inline fun Array<BooleanArray>.count2D(selector: (x: Int, y: Int, Boolean) -> Boolean): Int {
    var sum = 0

    this.forEach2D { x, y, t ->
        if (selector(x, y, t))
            sum++
    }

    return sum
}
// endregion