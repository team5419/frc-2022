package org.team5419.frc2022.fault.hardware.ctre

import com.ctre.phoenix.motorcontrol.IMotorController
import com.ctre.phoenix.motorcontrol.ControlMode
import com.ctre.phoenix.motorcontrol.DemandType
import com.ctre.phoenix.motorcontrol.NeutralMode

import org.team5419.frc2022.fault.hardware.AbstractBerkeliumMotor
import org.team5419.frc2022.fault.hardware.BerkeliumMotor
import org.team5419.frc2022.fault.math.units.SIKey
import org.team5419.frc2022.fault.math.units.SIUnit
import org.team5419.frc2022.fault.math.units.derived.Acceleration
import org.team5419.frc2022.fault.math.units.derived.Velocity
import org.team5419.frc2022.fault.math.units.derived.Volt
import org.team5419.frc2022.fault.math.units.derived.volts
import org.team5419.frc2022.fault.math.units.native.NativeUnitModel
import org.team5419.frc2022.fault.math.units.native.inNativeUnitsPer100msPerSecond
import org.team5419.frc2022.fault.math.units.native.nativeUnitsPer100ms
import org.team5419.frc2022.fault.math.units.native.toNativeUnitVelocity
import org.team5419.frc2022.fault.math.units.operations.times
import org.team5419.frc2022.fault.math.units.operations.div
import org.team5419.frc2022.fault.math.units.unitlessValue
import kotlin.math.roundToInt

import kotlin.properties.Delegates

abstract class CTREBerkeliumMotor<T : SIKey> internal constructor(
    val motorController: IMotorController,
    val model: NativeUnitModel<T>
) : AbstractBerkeliumMotor<T>() {

    private var mLastDemand = Demand(ControlMode.Disabled, 0.0, DemandType.Neutral, 0.0)

    override val encoder = CTREBerkeliumEncoder(motorController, 0, model)

    override val voltageOutput: SIUnit<Volt>
        get() = motorController.motorOutputVoltage.volts

    override var outputInverted: Boolean by Delegates.observable(false) { _, _, newValue ->
        motorController.inverted = newValue
    }

    override var brakeMode: Boolean by Delegates.observable(false) { _, _, newValue ->
        motorController.setNeutralMode(if (newValue) NeutralMode.Brake else NeutralMode.Coast)
    }

    override var voltageCompSaturation: SIUnit<Volt> by Delegates.observable(12.0.volts) { _, _, newValue ->
            motorController.configVoltageCompSaturation(newValue.value, 0)
            motorController.enableVoltageCompensation(true)
    }

    override var motionProfileCruiseVelocity: SIUnit<Velocity<T>>
            by Delegates.observable(SIUnit(0.0)) { _, _, newValue ->
        motorController.configMotionCruiseVelocity(
                model.toNativeUnitVelocity(newValue).nativeUnitsPer100ms.roundToInt(), 0
        )
    }

    override var motionProfileAcceleration: SIUnit<Acceleration<T>>
            by Delegates.observable(SIUnit(0.0)) { _, _, newValue ->
        motorController.configMotionAcceleration(
                model.toNativeUnitAcceleration(newValue).inNativeUnitsPer100msPerSecond().roundToInt(), 0
        )
    }

    init {
        motorController.configVoltageCompSaturation(kCompVoltage.value, 0)
        motorController.enableVoltageCompensation(true)
    }

    override fun setVoltage(voltage: SIUnit<Volt>, arbitraryFeedForward: SIUnit<Volt>) = sendDemand(
            Demand(
                    ControlMode.PercentOutput, (voltage / kCompVoltage).unitlessValue,
                    DemandType.ArbitraryFeedForward, (arbitraryFeedForward / kCompVoltage).unitlessValue
            )
    )

    override fun setPercent(percent: Double, arbitraryFeedForward: SIUnit<Volt>) = sendDemand(
            Demand(
                    ControlMode.PercentOutput, percent,
                    DemandType.ArbitraryFeedForward, (arbitraryFeedForward / kCompVoltage).unitlessValue
            )
    )

    override fun setVelocity(velocity: SIUnit<Velocity<T>>, arbitraryFeedForward: SIUnit<Volt>) = sendDemand(
            Demand(
                    ControlMode.Velocity, model.toNativeUnitVelocity(velocity).nativeUnitsPer100ms,
                    DemandType.ArbitraryFeedForward, (arbitraryFeedForward / kCompVoltage).unitlessValue
            )
    )

    override fun setPosition(position: SIUnit<T>, arbitraryFeedForward: SIUnit<Volt>) = sendDemand(
            Demand(
                    if (useMotionProfileForPosition) ControlMode.MotionMagic else ControlMode.Position,
                    model.toNativeUnitPosition(position).value,
                    DemandType.ArbitraryFeedForward, (arbitraryFeedForward / kCompVoltage).unitlessValue
            )
    )

    override fun setNeutral() = sendDemand(
            Demand(
                    ControlMode.Disabled,
                    0.0,
                    DemandType.Neutral,
                    0.0
            )
    )

    fun sendDemand(demand: Demand) {
        if (demand != mLastDemand) {
            motorController.set(demand.mode, demand.demand0, demand.demand1Type, demand.demand1)
            mLastDemand = demand
        }
    }

    override fun follow(motor: BerkeliumMotor<*>): Boolean =
            if (motor is CTREBerkeliumMotor<*>) {
                motorController.follow(motor.motorController)
                true
            } else {
                super.follow(motor)
            }

    data class Demand(
        val mode: ControlMode,
        val demand0: Double,
        val demand1Type: DemandType = DemandType.Neutral,
        val demand1: Double = 0.0
    )

    companion object {
        private val kCompVoltage = SIUnit<Volt>(12.0)
    }
}
