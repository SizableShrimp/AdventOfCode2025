package me.sizableshrimp.adventofcode2025.helper;

import me.sizableshrimp.adventofcode2025.templates.Fraction;

public class LinearAlgebra {
    // TODO: Docs
    /**
     * CHANGES THE INPUT MATRIX.
     * Must be in the form [A | b]!
     * The last column will be the output column!!
     *
     * @param matrix
     * @return {@code true} if possible, then solved is in the input matrix, {@code false} if not
     */
    public static boolean solve(Fraction[][] matrix) {
        if (!isConsistent(matrix))
            return false;

        if (!solve(matrix, 0, 0))
            return false;

        reduce(matrix);

        return true;
    }

    private static boolean isConsistent(Fraction[][] matrix) {
        if (matrix[0].length == 1)
            return true;

        for (int y = 0; y < matrix.length; y++) {
            boolean allZeros = true;
            for (int x = 0; x < matrix[0].length - 1; x++) {
                if (!matrix[y][x].isZero()) {
                    allZeros = false;
                    break;
                }
            }

            if (allZeros && !matrix[y][matrix[0].length - 1].isZero())
                return false;
        }

        return true;
    }

    private static boolean solve(Fraction[][] matrix, int startY, int startX) {
        if (startY == matrix.length || startX == matrix[0].length - 1)
            return true;

        int nonZeroIdx = -1;

        for (int y = startY; y < matrix.length; y++) {
            Fraction value = matrix[y][startX];
            if (!value.isZero()) {
                if (nonZeroIdx == -1)
                    nonZeroIdx = y;
                if (Fraction.ONE.equals(value)) {
                    nonZeroIdx = y;
                    break;
                }
            }
        }

        if (nonZeroIdx == -1) {
            return solve(matrix, startY, startX + 1);
        }

        if (nonZeroIdx != startY) {
            // Swap row to top
            Fraction[] temp = matrix[startY];
            matrix[startY] = matrix[nonZeroIdx];
            matrix[nonZeroIdx] = temp;
        }

        Fraction[] row = matrix[startY];
        Fraction scale = row[startX];
        row[startX] = Fraction.ONE;

        for (int x = startX + 1; x < matrix[0].length; x++) {
            row[x] = row[x].divide(scale);
        }

        for (int y = startY + 1; y < matrix.length; y++) {
            Fraction subScale = matrix[y][startX];
            if (subScale.isZero())
                continue;
            matrix[y][startX] = Fraction.ZERO;

            boolean allZeros = true;
            for (int x = startX + 1; x < matrix[0].length; x++) {
                Fraction next = matrix[y][x].minus(subScale.times(row[x]));
                matrix[y][x] = next;
                if (x < matrix[0].length - 1 && !next.isZero())
                    allZeros = false;
            }

            if (allZeros && !matrix[y][matrix[0].length - 1].isZero())
                return false; // Impossible to solve
        }

        return solve(matrix, startY + 1, startX + 1);
    }

    private static void reduce(Fraction[][] matrix) {
        int leadingX = 0;
        for (int y = 0; y < matrix.length; y++) {
            if (leadingX >= matrix[0].length - 1)
                return;

            Fraction[] row = matrix[y];
            while (row[leadingX].isZero()) {
                if (leadingX >= matrix[0].length - 2)
                    return;
                leadingX++;
            }

            for (int subY = 0; subY < y; subY++) {
                Fraction scale = matrix[subY][leadingX];
                if (scale.isZero())
                    continue;

                matrix[subY][leadingX] = Fraction.ZERO;

                for (int x = leadingX + 1; x < matrix[0].length; x++) {
                    matrix[subY][x] = matrix[subY][x].minus(scale.times(row[x]));
                }
            }

            leadingX++;
        }
    }

    public static boolean solve(int[][] matrix) {
        Fraction[][] fracMatrix = new Fraction[matrix.length][matrix[0].length];

        for (int y = 0; y < matrix.length; y++) {
            for (int x = 0; x < matrix[0].length; x++) {
                fracMatrix[y][x] = new Fraction(matrix[y][x], 1);
            }
        }

        if (!solve(fracMatrix))
            return false;

        for (int y = 0; y < matrix.length; y++) {
            int lcm = fracMatrix[y][0].denominator();

            for (int x = 1; x < matrix[0].length; x++) {
                lcm = MathUtil.lcm(lcm, fracMatrix[y][x].denominator());
            }

            for (int x = 0; x < matrix[0].length; x++) {
                matrix[y][x] = fracMatrix[y][x].numerator() * (lcm / fracMatrix[y][x].denominator());
            }
        }

        return true;
    }
}
