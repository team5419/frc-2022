package org.team5419.frc2022.fault.math.geometry

import org.team5419.frc2022.fault.math.kEpsilon
import org.team5419.frc2022.fault.math.units.Meter
import org.team5419.frc2022.fault.math.units.SIUnit
import org.team5419.frc2022.fault.math.units.derived.Radian
import org.team5419.frc2022.fault.math.units.meters
import kotlin.math.hypot
import kotlin.math.sin
import kotlin.math.cos
import kotlin.math.abs

class Twist2d constructor(
    val dx: SIUnit<Meter>,
    val dy: SIUnit<Meter>,
    val dTheta: SIUnit<Radian>
) {

    val norm get() = if (dy.value == 0.0) dx.absoluteValue else hypot(dx.value, dy.value).meters

    val asPose: Pose2d
        get() {
            val dTheta = this.dTheta.value
            val sinTheta = sin(dTheta)
            val cosTheta = cos(dTheta)

            val (s, c) = if (abs(dTheta) < kEpsilon) {
                1.0 - 1.0 / 6.0 * dTheta * dTheta to .5 * dTheta
            } else {
                sinTheta / dTheta to (1.0 - cosTheta) / dTheta
            }
            return Pose2d(
                    Vector2(dx * s - dy * c, dx * c + dy * s),
                    Rotation2d(cosTheta, sinTheta, false)
            )
        }

    operator fun times(scale: Double) =
            Twist2d(dx * scale, dy * scale, dTheta * scale)
}
