@file:SuppressWarnings("TopLevelPropertyNaming", "TooManyFunctions")
package org.team5419.frc2022.fault.math.units

import org.team5419.frc2022.fault.math.epsilonEquals
import org.team5419.frc2022.fault.math.lerp
import org.team5419.frc2022.fault.math.units.derived.Ohm
import org.team5419.frc2022.fault.math.units.derived.Volt
import org.team5419.frc2022.fault.math.units.derived.Watt
import kotlin.math.absoluteValue

// SI Constants
const val kKilo = 1e3
const val kHecto = 1e2
const val kDeca = 1e1
const val kDeci = 1e-1
const val kCenti = 1e-2
const val kMilli = 1e-3
const val kMicro = 1e-6
const val kNano = 1e-9

// SI Unit
inline class SIUnit<T : SIKey>(val value: Double) : Comparable<SIUnit<T>> {

    val absoluteValue get() = SIUnit<T>(value.absoluteValue)

    operator fun unaryMinus() = SIUnit<T>(-value)
    operator fun plus(other: SIUnit<T>) = SIUnit<T>(value.plus(other.value))
    operator fun minus(other: SIUnit<T>) = SIUnit<T>(value.minus(other.value))
    operator fun rem(other: SIUnit<T>) = SIUnit<T>(value.rem(other.value))
    operator fun times(other: Double) = SIUnit<T>(value.times(other))
    operator fun div(other: Double) = SIUnit<T>(value.div(other))
    operator fun times(other: Number) = times(other.toDouble())
    operator fun div(other: Number) = div(other.toDouble())

    override operator fun compareTo(other: SIUnit<T>) = value.compareTo(other.value)
    operator fun compareTo(other: Double) = value.compareTo(other)
    operator fun compareTo(other: Number) = compareTo(other.toDouble())

    fun lerp(endValue: SIUnit<T>, t: Double) = SIUnit<T>(value.lerp(endValue.value, t))
    infix fun epsilonEquals(other: SIUnit<T>) = value.epsilonEquals(other.value)
}

val Double.kilo get() = SIUnitBuilder(times(kKilo))
val Double.deca get() = SIUnitBuilder(times(kDeca))
val Double.base get() = SIUnitBuilder(this)
val Double.deci get() = SIUnitBuilder(times(kDeci))
val Double.centi get() = SIUnitBuilder(times(kCenti))
val Double.milli get() = SIUnitBuilder(times(kMilli))
val Double.micro get() = SIUnitBuilder(times(kMicro))

val Number.kilo get() = toDouble().kilo
val Number.deca get() = toDouble().deca
val Number.base get() = toDouble().base
val Number.deci get() = toDouble().deci
val Number.centi get() = toDouble().centi
val Number.milli get() = toDouble().milli
val Number.micro get() = toDouble().micro

inline class SIUnitBuilder(private val value: Double) {
    val seconds get() = SIUnit<Second>(value)
    val meters get() = SIUnit<Meter>(value)
    val grams get() = SIUnit<Kilogram>(value.times(kBaseOffsetKilo))
    val amps get() = SIUnit<Ampere>(value)
    val ohms get() = SIUnit<Ohm>(value)
    val volts get() = SIUnit<Volt>(value)
    val watts get() = SIUnit<Watt>(value)
}

interface SIKey

class Mult<T : SIKey, S : SIKey> : SIKey
class Frac<N : SIKey, D : SIKey> : SIKey

object Unitless : SIKey

val SIUnit<Unitless>.unitlessValue get() = value
