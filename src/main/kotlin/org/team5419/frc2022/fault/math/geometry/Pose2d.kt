package org.team5419.frc2022.fault.math.geometry

import org.team5419.frc2022.fault.math.epsilonEquals
import org.team5419.frc2022.fault.math.kEpsilon
import org.team5419.frc2022.fault.math.units.Meter
import org.team5419.frc2022.fault.math.units.SIUnit
import org.team5419.frc2022.fault.math.units.derived.Radian
import org.team5419.frc2022.fault.math.units.derived.toRotation2d
import kotlin.math.absoluteValue

data class Pose2d(
    val translation: Vector2<Meter> = Vector2(),
    val rotation: Rotation2d = Rotation2d()
) : State<Pose2d> {

    constructor(
        x: SIUnit<Meter>,
        y: SIUnit<Meter>,
        rotation: Rotation2d = Rotation2d()
    ) : this(Vector2(x, y), rotation)

    constructor(
        translation: Vector2<Meter> = Vector2(),
        rotation: SIUnit<Radian>
    ) : this(translation, rotation.toRotation2d())

    constructor(
        x: SIUnit<Meter>,
        y: SIUnit<Meter>,
        rotation: SIUnit<Radian>
    ) : this(Vector2(x, y), rotation)

    val twist: Twist2d
        get() {
            val dtheta = rotation.radian.value
            val halfDTheta = dtheta / 2.0
            val cosMinusOne = rotation.cos - 1.0

            val halfThetaByTanOfHalfDTheta = if (cosMinusOne.absoluteValue < kEpsilon) {
                1.0 - 1.0 / 12.0 * dtheta * dtheta
            } else {
                -(halfDTheta * rotation.sin) / cosMinusOne
            }
            val translationPart = translation *
                    Rotation2d(halfThetaByTanOfHalfDTheta, -halfDTheta, false)
            return Twist2d(translationPart.x, translationPart.y, rotation.radian)
        }

    val mirror get() = Pose2d(Vector2(translation.x, -translation.y), -rotation)

    infix fun inFrameOfReferenceOf(fieldRelativeOrigin: Pose2d) = (-fieldRelativeOrigin) + this

    operator fun plus(other: Pose2d) = transformBy(other)

    operator fun minus(other: Pose2d) = this + -other

    fun transformBy(other: Pose2d) =
            Pose2d(
                    translation + (other.translation * rotation),
                    rotation + other.rotation
            )

    operator fun unaryMinus(): Pose2d {
        val invertedRotation = -rotation
        return Pose2d((-translation) * invertedRotation, invertedRotation)
    }

    fun isCollinear(other: Pose2d): Boolean {
        if (!rotation.isParallel(other.rotation)) return false
        val twist = (-this + other).twist
        return twist.dy.value epsilonEquals 0.0 && twist.dTheta.value epsilonEquals 0.0
    }

    @Suppress("ReturnCount")
    override fun interpolate(endValue: Pose2d, t: Double): Pose2d {
        if (t <= 0) {
            return Pose2d(this.translation, this.rotation)
        } else if (t >= 1) {
            return Pose2d(endValue.translation, endValue.rotation)
        }
        val twist = (-this + endValue).twist
        return this + (twist * t).asPose
    }

    override fun toString() = toCSV()

    override fun toCSV() = "${translation.toCSV()}, ${rotation.degree}"

    override fun distance(other: Pose2d) = (-this + other).twist.norm.value
}
