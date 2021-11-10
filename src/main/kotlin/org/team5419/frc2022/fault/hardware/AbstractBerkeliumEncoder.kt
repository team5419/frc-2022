package org.team5419.frc2022.fault.hardware

import org.team5419.frc2022.fault.math.units.SIKey
import org.team5419.frc2022.fault.math.units.SIUnit
import org.team5419.frc2022.fault.math.units.derived.Velocity
import org.team5419.frc2022.fault.math.units.native.NativeUnitModel

abstract class AbstractBerkeliumEncoder<T : SIKey>(
    val model: NativeUnitModel<T>
) : BerkeliumEncoder<T> {
    override val position: SIUnit<T> get() = model.fromNativeUnitPosition(rawPosition)
    override val velocity: SIUnit<Velocity<T>> get() = model.fromNativeUnitVelocity(rawVelocity)
}
