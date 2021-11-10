package org.team5419.frc2022.fault.math.units.derived

import org.team5419.frc2022.fault.math.geometry.Rotation2d
import org.team5419.frc2022.fault.math.units.SIUnit
import org.team5419.frc2022.fault.math.units.Unitless

typealias Radian = Unitless

val Double.radians get() = SIUnit<Radian>(this)
val Double.degrees get() = SIUnit<Radian>(Math.toRadians(this))

val Number.radians get() = toDouble().radians
val Number.degrees get() = toDouble().degrees

fun SIUnit<Radian>.inRadians() = value
fun SIUnit<Radian>.inDegrees() = Math.toDegrees(value)

fun SIUnit<Radian>.toRotation2d() = Rotation2d(this)

val SIUnit<Radian>.cos get() = Math.cos(value)
val SIUnit<Radian>.sin get() = Math.sin(value)
val SIUnit<Radian>.tan get() = Math.tan(value)
