@file:SuppressWarnings("TopLevelPropertyNaming")
package org.team5419.frc2022.fault.math.units

object Kilogram : SIKey

const val kCentiOffsetKilo = 1e-5
const val kBaseOffsetKilo = 1e-3
const val kMilliOffsetKilo = 1e-6
const val kLbOffsetKilo = 0.453592

val Double.lbs get() = pounds
val Double.pounds get() = SIUnit<Kilogram>(times(kLbOffsetKilo))

val Number.lbs get() = toDouble().lbs
val Number.pounds get() = toDouble().pounds

fun SIUnit<Kilogram>.inKilograms() = value
fun SIUnit<Kilogram>.inGrams() = value.div(kBaseOffsetKilo)
fun SIUnit<Kilogram>.inCentigrams() = value.div(kCentiOffsetKilo)
fun SIUnit<Kilogram>.inMilligrams() = value.div(kMilliOffsetKilo)

fun SIUnit<Kilogram>.inLbs() = inPounds()
fun SIUnit<Kilogram>.inPounds() = value.div(kLbOffsetKilo)
