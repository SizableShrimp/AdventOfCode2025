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

fun <S, ID> searchBest(
    start: S, target: ID, getId: (S) -> ID,
    comparator: Comparator<S>, bfs: Boolean = true,
    run: (state: S, addNext: (S) -> Unit) -> Unit
): Pair<Map<ID, S>, S?> {
    val queue = ArrayDeque<S>()
    val seen = mutableMapOf<ID, S>()
    var min: S? = null
    queue.add(start)
    seen[getId(start)] = start
    val addNext = { s: S ->
        val id = getId(s)
        if (id == target) {
            if (min == null || comparator.compare(s, min) < 0) {
                min = s
            }
        } else if (isBetterState(seen, comparator, id, s)) {
            seen[id] = s
            queue.add(s)
        }
    }

    while (queue.isNotEmpty()) {
        val state = if (bfs) queue.removeFirst() else queue.removeLast()
        if (min != null && comparator.compare(min, state) <= 0) continue
        if (isWorseState(seen, comparator, getId(state), state)) continue

        run(state, addNext)
    }

    return seen to min
}

fun <S : Comparable<S>, ID> searchBest(
    start: S, target: ID, getId: (S) -> ID,
    bfs: Boolean = true, comparator: Comparator<S> = Comparator.naturalOrder(),
    run: (state: S, addNext: (S) -> Unit) -> Unit
) = searchBest(start, target, getId, comparator, bfs, run)

fun <ID> searchBestSimple(
    start: ID, target: ID,
    bfs: Boolean = true, comparator: Comparator<Pair<ID, Int>> = Comparator.comparingInt { it.second },
    run: (state: ID, addNext: (ID) -> Unit) -> Unit
) = searchBest(start to 0, target, { it.first }, comparator, bfs) { state, addNext ->
    run(state.first) { next -> addNext(next to (state.second + 1)) }
}

fun <S, ID> searchAll(
    start: S, getId: (S) -> ID,
    comparator: Comparator<S>, bfs: Boolean = true,
    run: (state: S, addNext: (S) -> Unit) -> Unit
): Map<ID, S> {
    val queue = ArrayDeque<S>()
    val seen = mutableMapOf<ID, S>()
    queue.add(start)
    seen[getId(start)] = start
    val addNext = { s: S ->
        val id = getId(s)
        if (isBetterState(seen, comparator, id, s)) {
            seen[id] = s
            queue.add(s)
        }
    }

    while (queue.isNotEmpty()) {
        val state = if (bfs) queue.removeFirst() else queue.removeLast()
        if (isWorseState(seen, comparator, getId(state), state)) continue

        run(state, addNext)
    }

    return seen
}

fun <S : Comparable<S>, ID> searchAll(
    start: S, getId: (S) -> ID,
    bfs: Boolean = true, comparator: Comparator<S> = Comparator.naturalOrder(),
    run: (state: S, addNext: (S) -> Unit) -> Unit
) = searchAll(start, getId, comparator, bfs, run)

fun <ID> searchAllSimple(
    start: ID,
    bfs: Boolean = true, comparator: Comparator<Pair<ID, Int>> = Comparator.comparingInt { it.second },
    run: (state: ID, addNext: (ID) -> Unit) -> Unit
) = searchAll(start to 0, { it.first }, comparator, bfs) { state, addNext ->
    run(state.first) { next -> addNext(next to (state.second + 1)) }
}

fun <S, ID> searchNoRepeats(
    start: S, getId: (S) -> ID, bfs: Boolean = true,
    run: (state: S, addNext: (S) -> Boolean) -> Unit
): Set<S> {
    val queue = ArrayDeque<S>()
    val seen = mutableMapOf<ID, S>()
    queue.add(start)
    seen[getId(start)] = start
    val addNext = { s: S ->
        val id = getId(s)
        if (!seen.contains(id)) {
            seen[id] = s
            queue.add(s)
            true
        } else false
    }

    while (queue.isNotEmpty()) {
        val state = if (bfs) queue.removeFirst() else queue.removeLast()

        run(state, addNext)
    }

    return seen.values.toSet()
}

fun <S> searchNoRepeats(
    start: S, bfs: Boolean = true,
    run: (state: S, addNext: (S) -> Boolean) -> Unit
): Set<S> {
    val queue = ArrayDeque<S>()
    val seen = mutableSetOf<S>()
    queue.add(start)
    seen.add(start)
    val addNext = { s: S ->
        if (seen.add(s)) {
            queue.add(s)
            true
        } else false
    }

    while (queue.isNotEmpty()) {
        val state = if (bfs) queue.removeFirst() else queue.removeLast()

        run(state, addNext)
    }

    return seen
}

private fun <S, ID> isEquivalentState(seen: Map<ID, S>, comparator: Comparator<S>, id: ID, state: S) =
    seen[id]?.let { comparator.compare(state, it) == 0 } ?: false

private fun <S, ID> isBetterState(seen: Map<ID, S>, comparator: Comparator<S>, id: ID, state: S) =
    seen[id]?.let { comparator.compare(state, it) < 0 } ?: true

private fun <S, ID> isWorseState(seen: Map<ID, S>, comparator: Comparator<S>, id: ID, state: S) =
    seen[id]?.let { comparator.compare(it, state) < 0 } ?: false

fun <S, ID, O> searchMemoizing(
    getId: (S) -> ID,
    run: (state: S, next: (S) -> O) -> O
): (S) -> O {
    val seen = mutableMapOf<ID, O>()

    fun recurse(s: S): O = getId(s).let { id ->
        seen[id] ?: run(s, ::recurse).also { seen[id] = it }
    }

    return { start -> run(start, ::recurse) }
}

fun <S, ID, O> searchMemoizing(
    start: S, getId: (S) -> ID,
    run: (state: S, next: (S) -> O) -> O
): O {
    val seen = mutableMapOf<ID, O>()

    fun recurse(s: S): O = getId(s).let { id ->
        seen[id] ?: run(s, ::recurse).also { seen[id] = it }
    }

    return run(start, ::recurse)
}

fun <S, O> searchMemoizing(
    run: (state: S, next: (S) -> O) -> O
): (S) -> O {
    val seen = mutableMapOf<S, O>()

    fun recurse(s: S): O = seen[s] ?: run(s, ::recurse).also { seen[s] = it }

    return { start -> run(start, ::recurse) }
}

fun <S, O> searchMemoizing(
    start: S,
    run: (state: S, next: (S) -> O) -> O
): O {
    val seen = mutableMapOf<S, O>()

    fun recurse(s: S): O = seen[s] ?: run(s, ::recurse).also { seen[s] = it }

    return run(start, ::recurse)
}