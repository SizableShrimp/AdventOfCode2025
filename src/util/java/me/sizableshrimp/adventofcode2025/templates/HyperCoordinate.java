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
 * A 4-dimensional coordinate object that holds an x, a y, a z, and a w value.
 */
public record HyperCoordinate(int x, int y, int z, int w) {
    public static final HyperCoordinate ORIGIN = new HyperCoordinate(0, 0, 0, 0);

    public static HyperCoordinate of(int x, int y, int z, int w) {
        return new HyperCoordinate(x, y, z, w);
    }

    public static HyperCoordinate of(double x, double y, double z, double w) {
        return new HyperCoordinate((int) x, (int) y, (int) z, (int) w);
    }

    /**
     * Parses a coordinate in the format "x,y,z,w".
     *
     * @param coord The input string of which to parse a coordinate.
     * @return A new {@link HyperCoordinate} object.
     */
    public static HyperCoordinate parse(String coord) {
        int commaIdx = coord.indexOf(',');
        int x = Integer.parseInt(coord.substring(0, commaIdx));
        int commaIdx2 = coord.indexOf(',', commaIdx + 1);
        int y = Integer.parseInt(coord.substring(commaIdx + 1, commaIdx2));
        int commaIdx3 = coord.indexOf(',', commaIdx2 + 1);
        int z = Integer.parseInt(coord.substring(commaIdx2 + 1, commaIdx3));
        int w = Integer.parseInt(coord.substring(commaIdx3 + 1));
        return new HyperCoordinate(x, y, z, w);
    }

    public HyperCoordinate resolve(HyperCoordinate other) {
        return resolve(other.x, other.y, other.z, other.w);
    }

    /**
     * Creates a new {@link HyperCoordinate} based on the offset of x, y, z, and w given in parameters added to the current
     * coordinate.
     *
     * @param x The offset-x to add to the coordinate x.
     * @param y The offset-y to add to the coordinate y.
     * @param z The offset-z to add to the coordinate z.
     * @param w The offset-w to add to the coordinate w.
     * @return A new {@link HyperCoordinate} created from the sum of the current coordinates and offset values provided.
     */
    public HyperCoordinate resolve(int x, int y, int z, int w) {
        return new HyperCoordinate(this.x + x, this.y + y, this.z + z, this.w + w);
    }

    /**
     * Finds the Manhattan distance between this coordinate and the origin at (0, 0, 0, 0).
     *
     * @return The Manhattan distance between this coordinate and the origin at (0, 0, 0, 0).
     */
    public int distanceToOrigin() {
        return distanceManhattan(0, 0, 0, 0);
    }

    /**
     * Finds the Manhattan distance between this coordinate and (x2, y2, z2, w2).
     *
     * @param x2 The x of the other coordinate.
     * @param y2 The y of the other coordinate.
     * @param z2 The z of the other coordinate.
     * @param w2 The w of the other coordinate.
     * @return The Manhattan distance between this coordinate and (x2, y2, z2, w2).
     */
    public int distanceManhattan(int x2, int y2, int z2, int w2) {
        return Math.abs(this.x - x2) + Math.abs(this.y - y2) + Math.abs(this.z - z2) + Math.abs(this.w - w2);
    }

    /**
     * Finds the Manhattan distance between this coordinate and the other specified coordinate.
     *
     * @param other The coordinate object to compare with.
     * @return The Manhattan distance between this coordinate and the other specified coordinate.
     */
    public int distanceManhattan(HyperCoordinate other) {
        return distanceManhattan(other.x, other.y, other.z, other.w);
    }

    /**
     * Finds the square of the Euclidean distance between this coordinate and (x2, y2, z2, w2).
     *
     * @param x2 The x of the other coordinate.
     * @param y2 The y of the other coordinate.
     * @param z2 The z of the other coordinate.
     * @return The square of the Euclidean distance between this coordinate and (x2, y2, z2, w2).
     */
    public long distanceSquared(int x2, int y2, int z2, int w2) {
        long xDiff = this.x - x2;
        long yDiff = this.y - y2;
        long zDiff = this.z - z2;
        long wDiff = this.w - w2;

        return xDiff * xDiff + yDiff * yDiff + zDiff * zDiff + wDiff * wDiff;
    }

    /**
     * Finds the square of the Euclidean distance between this coordinate and the other specified coordinate.
     *
     * @param other The coordinate object to compare with.
     * @return The square of the Euclidean distance between this coordinate and the other specified coordinate.
     */
    public double distanceSquared(HyperCoordinate other) {
        return this.distanceSquared(other.x, other.y, other.z, other.w);
    }

    /**
     * Finds the Euclidean distance between this coordinate and (x2, y2, z2, w2).
     *
     * @param x2 The x of the other coordinate.
     * @param y2 The y of the other coordinate.
     * @param z2 The z of the other coordinate.
     * @return The Euclidean distance between this coordinate and (x2, y2, z2, w2).
     */
    public double distanceSqrt(int x2, int y2, int z2, int w2) {
        return Math.sqrt(this.distanceSquared(x2, y2, z2, w2));
    }

    /**
     * Finds the Euclidean distance between this coordinate and the other specified coordinate.
     *
     * @param other The coordinate object to compare with.
     * @return The Euclidean distance between this coordinate and the other specified coordinate.
     */
    public double distanceSqrt(HyperCoordinate other) {
        return this.distanceSqrt(other.x, other.y, other.z, other.w);
    }

    public int component1() {
        return this.x;
    }

    public int component2() {
        return this.y;
    }

    public int component3() {
        return this.z;
    }

    public int component4() {
        return this.w;
    }

    @Override
    public String toString() {
        return String.format("(%d,%d,%d,%d)", this.x, this.y, this.z, this.w);
    }
}
