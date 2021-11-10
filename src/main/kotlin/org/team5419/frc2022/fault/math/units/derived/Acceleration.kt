package org.team5419.frc2022.fault.math.units.derived

import org.team5419.frc2022.fault.math.units.Frac
import org.team5419.frc2022.fault.math.units.Meter
import org.team5419.frc2022.fault.math.units.Second
import org.team5419.frc2022.fault.math.units.SIKey
import org.team5419.frc2022.fault.math.units.SIUnit

typealias Acceleration<T> = Frac<Frac<T, Second>, Second>
typealias LinearAcceleration = Acceleration<Meter>
typealias AngularAcceleration = Acceleration<Radian>

val <K : SIKey> SIUnit<K>.acceleration get() = SIUnit<Acceleration<K>>(value)
