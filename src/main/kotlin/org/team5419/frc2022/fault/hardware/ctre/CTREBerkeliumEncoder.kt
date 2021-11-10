package org.team5419.frc2022.fault.hardware.ctre

import com.ctre.phoenix.motorcontrol.IMotorController
import org.team5419.frc2022.fault.hardware.AbstractBerkeliumEncoder
import org.team5419.frc2022.fault.math.units.SIKey
import org.team5419.frc2022.fault.math.units.SIUnit
import org.team5419.frc2022.fault.math.units.native.NativeUnit
import org.team5419.frc2022.fault.math.units.native.NativeUnitModel
import org.team5419.frc2022.fault.math.units.native.nativeUnits
import org.team5419.frc2022.fault.math.units.native.nativeUnitsPer100ms
import kotlin.properties.Delegates

class CTREBerkeliumEncoder<T : SIKey>(
    val motorController: IMotorController,
    val pidIdx: Int = 0,
    model: NativeUnitModel<T>
) : AbstractBerkeliumEncoder<T>(model) {
    override val rawPosition get() =
        motorController.getSelectedSensorPosition(pidIdx).toDouble().nativeUnits
    override val rawVelocity get() =
        motorController.getSelectedSensorVelocity(pidIdx).toDouble().nativeUnitsPer100ms // check this

    var encoderPhase by Delegates.observable(false) {
        _, _, newValue -> motorController.setSensorPhase(newValue)
    }

    /**
     * sets position of encoder [newPosition] where [newPosition] is defined by the units of the[model]
     */
    override fun resetPosition(newPosition: SIUnit<T>) {
        // TODO(make sure this works)
        motorController.setSelectedSensorPosition(model.toNativeUnitPosition(newPosition).value.toInt(), pidIdx, 0)
    }

    override fun resetPositionRaw(newPosition: SIUnit<NativeUnit>) {
        motorController.setSelectedSensorPosition(newPosition.value.toInt(), pidIdx, 0)
    }
}
