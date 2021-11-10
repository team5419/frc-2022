package org.team5419.frc2022.fault.math

import kotlin.math.PI

fun Double.lerp(endValue: Double, t: Double) = this + (endValue - this) * t.coerceIn(0.0, 1.0)

infix fun Double.cos(other: Double) = times(Math.cos(other))
infix fun Double.sin(other: Double) = times(Math.sin(other))

fun limit(value: Double, limit: Double): Double {
    return limit(value, -limit, limit)
}

/**
 * method that limits input to a certain range.
 * @param value value to be limited
 * @param min lower bound of the value
 * @param max upper bound of the value
 * @return bounded value
 */
fun limit(value: Double, min: Double, max: Double): Double {
    return Math.min(max, Math.max(min, value))
}

/**
 * method to interpolate between 2 doubles
 * @param a first value
 * @param b second value
 * @param x value between the 2 values
 * @return interpolated value
 */
fun interpolate(a: Double, b: Double, x: Double): Double {
    val newX = limit(x, 0.0, 1.0)
    return a + (b - a) * newX
}

fun Double.boundRadians(): Double {
    var x = this
    while (x >= PI) x -= (2 * PI)
    while (x < -PI) x += (2 * PI)
    return x
}
