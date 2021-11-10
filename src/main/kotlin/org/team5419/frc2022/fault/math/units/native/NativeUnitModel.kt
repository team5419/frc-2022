package org.team5419.frc2022.fault.math.units.native

import org.team5419.frc2022.fault.math.units.Frac
import org.team5419.frc2022.fault.math.units.Meter
import org.team5419.frc2022.fault.math.units.SIKey
import org.team5419.frc2022.fault.math.units.SIUnit
import org.team5419.frc2022.fault.math.units.derived.Acceleration
import org.team5419.frc2022.fault.math.units.derived.Radian
import org.team5419.frc2022.fault.math.units.derived.Velocity
import org.team5419.frc2022.fault.math.units.operations.times
import org.team5419.frc2022.fault.math.units.operations.div

abstract class NativeUnitModel<K : SIKey> {

    abstract fun fromNativeUnitPosition(nativeUnits: SIUnit<NativeUnit>): SIUnit<K>
    abstract fun toNativeUnitPosition(modelledUnit: SIUnit<K>): SIUnit<NativeUnit>

    open fun toNativeUnitError(modelledUnit: SIUnit<K>): SIUnit<NativeUnit> =
            toNativeUnitPosition(modelledUnit) - toNativeUnitPosition(SIUnit(0.0))

    open fun fromNativeUnitVelocity(nativeUnitVelocity: SIUnit<NativeUnitVelocity>): SIUnit<Velocity<K>> =
            SIUnit(fromNativeUnitPosition(SIUnit(nativeUnitVelocity.value)).value)

    open fun toNativeUnitVelocity(modelledUnitVelocity: SIUnit<Velocity<K>>): SIUnit<NativeUnitVelocity> =
            SIUnit(toNativeUnitPosition(SIUnit(modelledUnitVelocity.value)).value)

    open fun fromNativeUnitAcceleration(nativeUnitAcceleration: SIUnit<NativeUnitAcceleration>):
            SIUnit<Acceleration<K>> = SIUnit(fromNativeUnitVelocity(SIUnit(nativeUnitAcceleration.value)).value)

    open fun toNativeUnitAcceleration(modelledUnitAcceleration: SIUnit<Acceleration<K>>):
            SIUnit<NativeUnitAcceleration> = SIUnit(toNativeUnitVelocity(SIUnit(modelledUnitAcceleration.value)).value)
}

object DefaultNativeUnitModel : NativeUnitModel<NativeUnit>() {
    override fun fromNativeUnitPosition(nativeUnits: SIUnit<NativeUnit>): SIUnit<NativeUnit> = nativeUnits
    override fun toNativeUnitPosition(modelledUnit: SIUnit<NativeUnit>): SIUnit<NativeUnit> = modelledUnit
}

class NativeUnitLengthModel(
    val nativeUnitsPerRotation: SIUnit<NativeUnit>,
    val wheelRadius: SIUnit<Meter>
) : NativeUnitModel<Meter>() {
    override fun fromNativeUnitPosition(nativeUnits: SIUnit<NativeUnit>): SIUnit<Meter> =
            wheelRadius * ((nativeUnits / nativeUnitsPerRotation) * (2.0 * Math.PI))

    override fun toNativeUnitPosition(modelledUnit: SIUnit<Meter>): SIUnit<NativeUnit> =
            nativeUnitsPerRotation * (modelledUnit / (wheelRadius * (2.0 * Math.PI)))
}

class NativeUnitRotationModel(
    val nativeUnitsPerRotation: SIUnit<NativeUnit>
) : NativeUnitModel<Radian>() {
    override fun toNativeUnitPosition(modelledUnit: SIUnit<Radian>): SIUnit<NativeUnit> =
            (modelledUnit / (2.0 * Math.PI)) * nativeUnitsPerRotation

    override fun fromNativeUnitPosition(nativeUnits: SIUnit<NativeUnit>): SIUnit<Radian> =
            2.0 * Math.PI * (nativeUnits / nativeUnitsPerRotation)
}

class SlopeNativeUnitModel<K : SIKey>(
    val modelledSample: SIUnit<K>,
    val nativeUnitSample: SIUnit<NativeUnit>
) : NativeUnitModel<K>() {
    private val slope: SIUnit<Frac<K, NativeUnit>> = modelledSample / nativeUnitSample

    override fun fromNativeUnitPosition(nativeUnits: SIUnit<NativeUnit>): SIUnit<K> =
            nativeUnits * slope

    override fun toNativeUnitPosition(modelledUnit: SIUnit<K>): SIUnit<NativeUnit> =
            modelledUnit / slope
}

fun SlopeNativeUnitModel<Meter>.wheelRadius(sensorUnitsPerRotation: SIUnit<NativeUnit>): SIUnit<Meter> =
        modelledSample / (nativeUnitSample / sensorUnitsPerRotation) / 2.0 / Math.PI
