package org.team5419.frc2022.fault.math.units.operations

import org.team5419.frc2022.fault.math.units.SIKey
import org.team5419.frc2022.fault.math.units.SIUnit
import org.team5419.frc2022.fault.math.units.Unitless

operator fun <A : SIKey> SIUnit<A>.div(other: SIUnit<A>) = SIUnit<Unitless>(value.div(other.value))
