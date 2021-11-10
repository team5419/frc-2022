package org.team5419.frc2022.fault.math.units

object Ampere : SIKey

val Double.amps get() = SIUnit<Ampere>(this)

val Number.amps get() = toDouble().amps

fun SIUnit<Ampere>.inMilliamps() = value.div(kMilli)
fun SIUnit<Ampere>.inMicroamps() = value.div(kMicro)
fun SIUnit<Ampere>.inAmps() = value
