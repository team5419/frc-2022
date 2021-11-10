package org.team5419.frc2022.fault.math.units.derived

import org.team5419.frc2022.fault.math.units.Frac
import org.team5419.frc2022.fault.math.units.Mult
import org.team5419.frc2022.fault.math.units.Kilogram
import org.team5419.frc2022.fault.math.units.Meter
import org.team5419.frc2022.fault.math.units.Second
import org.team5419.frc2022.fault.math.units.Ampere
import org.team5419.frc2022.fault.math.units.SIUnit

typealias Volt = Frac<Mult<Kilogram, Mult<Meter, Meter>>,
        Mult<Ampere, Mult<Second, Mult<Second, Second>>>>

val Double.volts get() = SIUnit<Volt>(this)

val Number.volts get() = toDouble().volts
