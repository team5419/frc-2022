package org.team5419.frc2022.fault.hardware

import org.team5419.frc2022.fault.math.units.SIKey
import org.team5419.frc2022.fault.math.units.SIUnit
import org.team5419.frc2022.fault.math.units.derived.Velocity
import org.team5419.frc2022.fault.math.units.native.NativeUnit
import org.team5419.frc2022.fault.math.units.native.NativeUnitVelocity

interface BerkeliumEncoder<T : SIKey> {

    /**
     * The velocity of the encoder in [T]/s
     */
    val velocity: SIUnit<Velocity<T>>
    /**
     * The position of the encoder in [T]
     */
    val position: SIUnit<T>

    /**
     * The velocity of the encoder in NativeUnits/s
     */
    val rawVelocity: SIUnit<NativeUnitVelocity>
    /**
     * The position of the encoder in NativeUnits
     */
    val rawPosition: SIUnit<NativeUnit>

    fun resetPosition(newPosition: SIUnit<T> = SIUnit(0.0))

    fun resetPositionRaw(newPosition: SIUnit<NativeUnit> = SIUnit(0.0))
}
