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

        return new Fraction(this.numerator * (lcm / this.denominator) + other.numerator * (lcm / other.denominator), lcm);
    }

    /**
     * Subtracts the given fraction from this fraction.
     * The returned fraction will be simplified.
     */
    public Fraction minus(Fraction other) {
        int lcm = MathUtil.lcm(this.denominator, other.denominator);

        return new Fraction(this.numerator * (lcm / this.denominator) - other.numerator * (lcm / other.denominator), lcm);
    }

    public Fraction times(Fraction other) {
        int newNum = this.numerator * other.numerator;
        int newDenom = this.denominator * other.denominator;
        int gcd = MathUtil.gcd(newNum, newDenom);

        return new Fraction(newNum / gcd, newDenom / gcd);
    }

    /**
     * Takes this fraction as the dividend and the other fraction as the divisor.
     * The returned fraction will be simplified.
     */
    public Fraction divide(Fraction other) {
        int newNum = this.numerator * other.denominator;
        int newDenom = this.denominator * other.numerator;
        int gcd = MathUtil.gcd(newNum, newDenom);

        return new Fraction(newNum / gcd, newDenom / gcd);
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
