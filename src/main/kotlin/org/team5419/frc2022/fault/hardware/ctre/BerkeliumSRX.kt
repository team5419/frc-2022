package org.team5419.frc2022.fault.hardware.ctre

import com.ctre.phoenix.motorcontrol.can.TalonSRX
import org.team5419.frc2022.fault.math.units.SIUnit
import org.team5419.frc2022.fault.math.units.SIKey
import org.team5419.frc2022.fault.math.units.Meter
import org.team5419.frc2022.fault.math.units.Second
import org.team5419.frc2022.fault.math.units.inMilliseconds
import org.team5419.frc2022.fault.math.units.Ampere
import org.team5419.frc2022.fault.math.units.inAmps
import org.team5419.frc2022.fault.math.units.derived.Radian
import org.team5419.frc2022.fault.math.units.native.NativeUnitModel

typealias LinearBerkeleiumSRX = BerkeliumSRX<Meter>
typealias AngularBerkeleiumSRX = BerkeliumSRX<Radian>

class BerkeliumSRX<T : SIKey>(
    val talonSRX: TalonSRX,
    model: NativeUnitModel<T>
) : CTREBerkeliumMotor<T>(talonSRX, model) {

    constructor(id: Int, model: NativeUnitModel<T>): this(TalonSRX(id), model)

    init {
        talonSRX.configFactoryDefault(0)
    }

    fun configCurrentLimit(enabled: Boolean, config: CurrentLimitConfig? = null) {
        talonSRX.enableCurrentLimit(enabled)
        if (enabled && config != null) {
            talonSRX.configContinuousCurrentLimit(config.continuousCurrentLimit.inAmps().toInt())
            talonSRX.configPeakCurrentLimit(config.peakCurrentLimit.inAmps().toInt())
            talonSRX.configPeakCurrentDuration(config.peakCurrentLimitDuration.inMilliseconds().toInt())
        }
    }

    data class CurrentLimitConfig(
        val peakCurrentLimit: SIUnit<Ampere>,
        val peakCurrentLimitDuration: SIUnit<Second>,
        val continuousCurrentLimit: SIUnit<Ampere>
    )
}
