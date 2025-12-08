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

package me.sizableshrimp.adventofcode2025.templates;

/**
 * A 3-dimensional coordinate object that holds an x, a y, and a z value.
 */
public record ZLongCoordinate(long x, long y, long z) {
    public static final ZLongCoordinate ORIGIN = new ZLongCoordinate(0, 0, 0);

    public static ZLongCoordinate of(long x, long y, long z) {
        return new ZLongCoordinate(x, y, z);
    }

    public static ZLongCoordinate of(double x, double y, double z) {
        return new ZLongCoordinate((long) x, (long) y, (long) z);
    }

    /**
     * Parses a coordinate in the format "x,y,z".
     *
     * @param coord The input string of which to parse a coordinate.
     * @return A new {@link ZLongCoordinate} object.
     */
    public static ZLongCoordinate parse(String coord) {
        int commaIdx = coord.indexOf(',');
        long x = Long.parseLong(coord.substring(0, commaIdx));
        int commaIdx2 = coord.indexOf(',', commaIdx + 1);
        long y = Long.parseLong(coord.substring(commaIdx + 1, commaIdx2));
        long z = Long.parseLong(coord.substring(commaIdx2 + 1));
        return new ZLongCoordinate(x, y, z);
    }

    public ZLongCoordinate resolve(ZLongCoordinate other) {
        return resolve(other.x, other.y, other.z);
    }

    public ZLongCoordinate resolve(ZDirection direction) {
        return resolve(direction.x, direction.y, direction.z);
    }

    /**
     * Creates a new {@link ZLongCoordinate} based on the offset of x, y, and z given in parameters added to the current
     * coordinate.
     *
     * @param x The offset-x to add to the coordinate x.
     * @param y The offset-y to add to the coordinate y.
     * @param z The offset-z to add to the coordinate z.
     * @return A new {@link ZLongCoordinate} created from the sum of the current coordinates and offset values provided.
     */
    public ZLongCoordinate resolve(long x, long y, long z) {
        return new ZLongCoordinate(this.x + x, this.y + y, this.z + z);
    }

    /**
     * Finds the Manhattan distance between this coordinate and the origin at (0, 0, 0).
     *
     * @return The Manhattan distance between this coordinate and the origin at (0, 0, 0).
     */
    public long distanceToOrigin() {
        return distanceManhattan(0, 0, 0);
    }

    /**
     * Finds the Manhattan distance between this coordinate and (x2, y2, z2).
     *
     * @param x2 The x of the other coordinate.
     * @param y2 The y of the other coordinate.
     * @param z2 The z of the other coordinate.
     * @return The Manhattan distance between this coordinate and (x2, y2, z2).
     */
    public long distanceManhattan(long x2, long y2, long z2) {
        return Math.abs(this.x - x2) + Math.abs(this.y - y2) + Math.abs(this.z - z2);
    }

    /**
     * Finds the Manhattan distance between this coordinate and the other specified coordinate.
     *
     * @param other The coordinate object to compare with.
     * @return The Manhattan distance between this coordinate and the other specified coordinate.
     */
    public long distanceManhattan(ZLongCoordinate other) {
        return distanceManhattan(other.x, other.y, other.z);
    }

    /**
     * Finds the square of the Euclidean distance between this coordinate and (x2, y2, z2).
     *
     * @param x2 The x of the other coordinate.
     * @param y2 The y of the other coordinate.
     * @param z2 The z of the other coordinate.
     * @return The square of the Euclidean distance between this coordinate and (x2, y2, z2).
     */
    public long distanceSquared(long x2, long y2, long z2) {
        long xDiff = this.x - x2;
        long yDiff = this.y - y2;
        long zDiff = this.z - z2;

        return xDiff * xDiff + yDiff * yDiff + zDiff * zDiff;
    }

    /**
     * Finds the square of the Euclidean distance between this coordinate and the other specified coordinate.
     *
     * @param other The coordinate object to compare with.
     * @return The square of the Euclidean distance between this coordinate and the other specified coordinate.
     */
    public double distanceSquared(ZLongCoordinate other) {
        return this.distanceSquared(other.x, other.y, other.z);
    }

    /**
     * Finds the Euclidean distance between this coordinate and (x2, y2, z2).
     *
     * @param x2 The x of the other coordinate.
     * @param y2 The y of the other coordinate.
     * @param z2 The z of the other coordinate.
     * @return The Euclidean distance between this coordinate and (x2, y2, z2).
     */
    public double distanceSqrt(long x2, long y2, long z2) {
        return Math.sqrt(this.distanceSquared(x2, y2, z2));
    }

    /**
     * Finds the Euclidean distance between this coordinate and the other specified coordinate.
     *
     * @param other The coordinate object to compare with.
     * @return The Euclidean distance between this coordinate and the other specified coordinate.
     */
    public double distanceSqrt(ZLongCoordinate other) {
        return this.distanceSqrt(other.x, other.y, other.z);
    }

    public ZLongCoordinate subtract(ZLongCoordinate other) {
        return ZLongCoordinate.of(this.x - other.x, this.y - other.y, this.z - other.z);
    }

    /**
     * Returns the value on the z direction axis.
     */
    public long getAxis(ZDirection.Axis axis) {
        return switch (axis) {
            case X -> this.x;
            case Y -> this.y;
            case Z -> this.z;
        };
    }

    public long component1() {
        return this.x;
    }

    public long component2() {
        return this.y;
    }

    public long component3() {
        return this.z;
    }

    @Override
    public String toString() {
        return String.format("(%d,%d,%d)", this.x, this.y, this.z);
    }
}
