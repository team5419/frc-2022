package org.team5419.frc2022.fault.util

interface Interpolable<T> {
    public fun interpolate(other: T, x: Double): T
}
