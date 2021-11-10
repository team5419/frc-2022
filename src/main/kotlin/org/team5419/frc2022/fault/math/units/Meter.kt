@file:SuppressWarnings("TopLevelPropertyNaming", "TooManyFunctions")
package org.team5419.frc2022.fault.math.units

object Meter : SIKey

const val kInchToMeter = 0.0254
const val kFeetToMeter = kInchToMeter * 12.0
const val kThouToMeter = kInchToMeter * 0.001
const val kYardToMeter = kFeetToMeter * 3.0

val Double.meters get() = SIUnit<Meter>(this)
val Double.thou get() = SIUnit<Meter>(times(kThouToMeter))
val Double.inches get() = SIUnit<Meter>(times(kInchToMeter))
val Double.feet get() = SIUnit<Meter>(times(kFeetToMeter))
val Double.yards get() = SIUnit<Meter>(times(kYardToMeter))

val Number.meters get() = toDouble().meters
val Number.thou get() = toDouble().thou
val Number.inches get() = toDouble().inches
val Number.feet get() = toDouble().feet

fun SIUnit<Meter>.inThou() = value.div(kThouToMeter)
fun SIUnit<Meter>.inInches() = value.div(kInchToMeter)
fun SIUnit<Meter>.inFeet() = value.div(kFeetToMeter)
fun SIUnit<Meter>.inYards() = value.div(kYardToMeter)

fun SIUnit<Meter>.inKilometers() = value.div(kKilo)
fun SIUnit<Meter>.inHectometers() = value.div(kHecto)
fun SIUnit<Meter>.inDecameters() = value.div(kDeca)
fun SIUnit<Meter>.inMeters() = value
fun SIUnit<Meter>.inDecimeters() = value.div(kDeci)
fun SIUnit<Meter>.inCentimeters() = value.div(kCenti)
fun SIUnit<Meter>.inMillimeters() = value.div(kMilli)
fun SIUnit<Meter>.inMicrometers() = value.div(kMicro)
