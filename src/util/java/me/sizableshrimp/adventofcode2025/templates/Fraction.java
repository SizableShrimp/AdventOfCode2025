package me.sizableshrimp.adventofcode2025.templates;

import me.sizableshrimp.adventofcode2025.helper.MathUtil;
import org.jspecify.annotations.NonNull;

public record Fraction(int numerator, int denominator) {
    public static final Fraction ZERO = new Fraction(0, 1);
    public static final Fraction ONE = new Fraction(1, 1);

    public Fraction reciprocal() {
        return new Fraction(this.denominator, this.numerator);
    }

    public boolean isZero() {
        return this.numerator == 0;
    }

    /**
     * Computes the product of the given fraction with this fraction.
     * The returned fraction will be simplified.
     */
    public Fraction plus(Fraction other) {
        int lcm = MathUtil.lcm(this.denominator, other.denominator);

        return simplify(this.numerator * (lcm / this.denominator) + other.numerator * (lcm / other.denominator), lcm);
    }

    /**
     * Subtracts the given fraction from this fraction.
     * The returned fraction will be simplified.
     */
    public Fraction minus(Fraction other) {
        int lcm = MathUtil.lcm(this.denominator, other.denominator);

        return simplify(this.numerator * (lcm / this.denominator) - other.numerator * (lcm / other.denominator), lcm);
    }

    public Fraction times(Fraction other) {
        return simplify(this.numerator * other.numerator, this.denominator * other.denominator);
    }

    /**
     * Takes this fraction as the dividend and the other fraction as the divisor.
     * The returned fraction will be simplified.
     */
    public Fraction divide(Fraction other) {
        return simplify(this.numerator * other.denominator, this.denominator * other.numerator);
    }

    private static Fraction simplify(int numerator, int denominator) {
        if (denominator == -1) {
            numerator = -numerator;
            denominator = -denominator;
        }
        int gcd = MathUtil.gcd(numerator, denominator);

        return new Fraction(numerator / gcd, denominator / gcd);
    }

    public double toDouble() {
        return ((double) this.numerator) / this.denominator;
    }

    public int component1() {
        return this.numerator;
    }

    public int component2() {
        return this.denominator;
    }

    @Override
    public @NonNull String toString() {
        return this.numerator + "/" + this.denominator;
    }
}
