package org.team5419.frc2022.fault.math.geometry

import org.team5419.frc2022.fault.math.epsilonEquals
import org.team5419.frc2022.fault.math.kEpsilon
import org.team5419.frc2022.fault.math.units.SIUnit
import org.team5419.frc2022.fault.math.units.derived.Radian
import org.team5419.frc2022.fault.math.units.derived.cos
import org.team5419.frc2022.fault.math.units.derived.sin
import org.team5419.frc2022.fault.math.units.derived.radians
import org.team5419.frc2022.fault.math.units.derived.inDegrees
// import org.team5419.frc2022.fault.math.units.derived.radians

@Suppress("EqualsWithHashCodeExist")
class Rotation2d {

    val value: SIUnit<Radian>
    val cos: Double
    val sin: Double

    constructor() : this(0.0.radians)

    constructor(heading: SIUnit<Radian>) : this(heading.cos, heading.sin, true)

    constructor(x: Double, y: Double, normalize: Boolean) {
        if (normalize) {
            val magnitude = Math.hypot(x, y)
            if (magnitude > kEpsilon) {
                sin = y / magnitude
                cos = x / magnitude
            } else {
                sin = 0.0
                cos = 1.0
            }
        } else {
            cos = x
            sin = y
        }
        value = Math.atan2(sin, cos).radians
    }

    val radian get() = value
    val degree get() = value.inDegrees()

    fun isParallel(rotation: Rotation2d) = (this - rotation).radian epsilonEquals 0.0.radians

    operator fun minus(other: Rotation2d) = plus(-other)
    operator fun unaryMinus() = Rotation2d(-value)

    operator fun times(other: Double) = Rotation2d(value * other)

    operator fun plus(other: Rotation2d): Rotation2d {
        return Rotation2d(
                cos * other.cos - sin * other.sin,
                cos * other.sin + sin * other.cos,
                true
        )
    }

    override fun equals(other: Any?): Boolean {
        return other is Rotation2d && other.sin epsilonEquals this.sin && other.cos epsilonEquals this.cos
    }
}
