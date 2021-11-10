package org.team5419.frc2022.fault.math.units.derived

import org.team5419.frc2022.fault.math.units.Frac
import org.team5419.frc2022.fault.math.units.Meter
import org.team5419.frc2022.fault.math.units.Unitless

typealias Inverse<T> = Frac<Unitless, T>
typealias Curvature = Inverse<Meter>
