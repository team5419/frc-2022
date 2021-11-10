package org.team5419.frc2022.fault.math.units.derived

import org.team5419.frc2022.fault.math.units.Frac
import org.team5419.frc2022.fault.math.units.kMinuteToSecond
import org.team5419.frc2022.fault.math.units.kFeetToMeter
import org.team5419.frc2022.fault.math.units.kInchToMeter
import org.team5419.frc2022.fault.math.units.Meter
import org.team5419.frc2022.fault.math.units.Second
import org.team5419.frc2022.fault.math.units.SIKey
import org.team5419.frc2022.fault.math.units.SIUnit

typealias Velocity<T> = Frac<T, Second>
typealias LinearVelocity = Velocity<Meter>
typealias AngularVelocity = Velocity<Radian>

val <K : SIKey> SIUnit<K>.velocity get() = SIUnit<Velocity<K>>(value)
fun SIUnit<LinearVelocity>.inFeetPerSecond() = value.div(kFeetToMeter)
fun SIUnit<LinearVelocity>.inFeetPerMinute() = inFeetPerSecond().times(kMinuteToSecond)
fun SIUnit<LinearVelocity>.inInchesPerSecond() = value.div(kInchToMeter)
