package org.team5419.frc2022.fault.hardware.ctre

import com.ctre.phoenix.motorcontrol.can.VictorSPX
import org.team5419.frc2022.fault.math.units.Meter
import org.team5419.frc2022.fault.math.units.SIKey
import org.team5419.frc2022.fault.math.units.derived.Radian
import org.team5419.frc2022.fault.math.units.native.NativeUnitModel

typealias LinearBerkeleiumSPX = BerkeliumSPX<Meter>
typealias AngularBerkeleiumSPX = BerkeliumSPX<Radian>

class BerkeliumSPX<T : SIKey>(
    val victorSPX: VictorSPX,
    model: NativeUnitModel<T>
) : CTREBerkeliumMotor<T>(victorSPX, model) {

    init {
        victorSPX.configFactoryDefault()
    }

    constructor(id: Int, model: NativeUnitModel<T>): this(VictorSPX(id), model)
}
