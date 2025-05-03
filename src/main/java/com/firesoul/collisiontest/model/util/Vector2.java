package com.firesoul.collisiontest.model.util;

import java.util.function.BiFunction;

public record Vector2(double x, double y) {
    
    /**
     * @return a vector with all zeros as coordinates
     */
    public static Vector2 zero() {
        return new Vector2(0.0, 0.0);
    }
    
    /**
     * @return a vector with all ones as coordinates
     */
    public static Vector2 one() {
        return new Vector2(1.0, 1.0);
    }
    
    /**
     * @return a vector directed up
     */
    public static Vector2 up() {
        return new Vector2(0.0, -1.0);
    }
    
    /**
     * @return a vector directed down
     */
    public static Vector2 down() {
        return new Vector2(0.0, 1.0);
    }
    
    /**
     * @return a vector directed left
     */
    public static Vector2 left() {
        return new Vector2(-1.0, 0.0);
    }
    
    /**
     * @return a vector directed right
     */
    public static Vector2 right() {
        return new Vector2(1.0, 0.0);
    }

    /**
     * @param a scalar
     * @return the sum of this vector and a scalar a
     */
    public Vector2 add(final double a) {
        return new Vector2(this.x + a, this.y + a);
    }

    /**
     * @param a scalar
     * @return the difference of this vector and a scalar a
     */
    public Vector2 subtract(final double a) {
        return this.add(-a);
    }

    /**
     * @param a scalar
     * @return the product of this vector and a scalar a
     */
    public Vector2 multiply(final double a) {
        return new Vector2(this.x*a, this.y*a);
    }

    /**
     * @param a scalar
     * @return the division of this vector and a scalar a
     */
    public Vector2 divide(final double a) {
        return this.multiply(1/a);
    }

    /**
     * @return the vector with the sign of both coordinates inverted
     */
    public Vector2 invert() {
        return new Vector2(-this.x, -this.y);
    }

    /**
     * @param v vector
     * @return the sum of this vector and the vector v
     */
    public Vector2 add(final Vector2 v) {
        return new Vector2(this.x + v.x, this.y + v.y);
    }

    /**
     * @param v vector
     * @return the difference of this vector and the vector v
     */
    public Vector2 subtract(final Vector2 v) {
        return this.add(v.invert());
    }

    /**
     * @param v vector
     * @return the product of this vector and the vector v
     */
    public Vector2 multiply(final Vector2 v) {
        return new Vector2(this.x*v.x, this.y*v.y);
    }

    /**
     * @param v vector
     * @return the division of this vector and the vector v
     */
    public Vector2 divide(final Vector2 v) {
        return this.multiply(new Vector2(1/v.x, 1/v.y));
    }

    public double dot(final Vector2 v) {
        return this.x*v.x + this.y*v.y;
    }

    public double norm() {
        return Math.sqrt(this.dot(this));
    }

    public Vector2 normalize() {
        if (this.norm() != 0.0) {
            return this.divide(this.norm());
        } else {
            return Vector2.zero();
        }
    }

    private static final BiFunction<Vector2, Double, Vector2> r = (v, a) -> new Vector2(v.x*Math.cos(a) - v.y*Math.sin(a), v.x*Math.sin(a) + v.y*Math.cos(a));

    public Vector2 rotate(final double a) {
        return r.apply(this, a);
    }
}
