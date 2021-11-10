package org.team5419.frc2022.subsystems

import org.team5419.frc2022.tab
import org.team5419.frc2022.subsystems.Shooger
import org.team5419.frc2022.StorageConstants
import org.team5419.frc2022.ShoogerConstants
import org.team5419.frc2022.fault.subsystems.Subsystem
import edu.wpi.first.wpilibj.shuffleboard.*
import edu.wpi.first.wpilibj.Timer
import edu.wpi.first.wpilibj.AnalogInput
import com.ctre.phoenix.motorcontrol.can.TalonSRX
import com.ctre.phoenix.motorcontrol.NeutralMode
import com.ctre.phoenix.motorcontrol.FeedbackDevice
import com.ctre.phoenix.motorcontrol.ControlMode

object Storage : Subsystem("Storage") {
    // storage mode

    enum class StorageMode() { LOAD, PASSIVE, REVERSE, OFF }

    public var mode = StorageMode.OFF
        set(value: StorageMode) {
            if (value == field) return

            when (value) {
                StorageMode.LOAD -> {
                    hopper.set( ControlMode.PercentOutput, hopperPercent )
                    feeder.set( ControlMode.PercentOutput, feederPercent )
                }

                StorageMode.PASSIVE -> {
                    hopper.set( ControlMode.PercentOutput, hopperLazyPercent )
                    feeder.set( ControlMode.PercentOutput, 0.0 )
                }

                StorageMode.OFF -> {
                    hopper.set( ControlMode.PercentOutput, 0.0 )
                    feeder.set( ControlMode.PercentOutput, 0.0 )
                }

                StorageMode.REVERSE -> {
                    hopper.set( ControlMode.PercentOutput, -0.5 )
                    feeder.set( ControlMode.PercentOutput, 0.0 )
                }

                /*StorageMode.UNJAM -> {
                    hopper.set( ControlMode.PercentOutput, -0.5 )
                    feeder.set( ControlMode.PercentOutput, -0.5 )
                }*/
            }

            lastMode = field

            field = value
        }

    var lastMode = mode

    // motors

    public val feeder = TalonSRX(StorageConstants.FeederPort)
        .apply {
            configFactoryDefault(100)

            configPeakOutputForward(1.0, 100)
            configPeakOutputReverse(-1.0, 100)

            setInverted(true)
            setSensorPhase(false)
            setNeutralMode(NeutralMode.Brake)
            configSelectedFeedbackSensor(FeedbackDevice.Analog)
        }

    public val hopper = TalonSRX(StorageConstants.HopperPort)
        .apply {
            configFactoryDefault(100)

            configPeakOutputForward(1.0, 100)
            configPeakOutputReverse(-1.0, 100)

            setInverted(false)
        }

    // reverse

    public fun reverse() {
        mode = StorageMode.REVERSE
    }

    public fun resetReverse() {
        if (mode == StorageMode.REVERSE) {
            mode = lastMode
        }
    }

    // default settings

    private var hopperPercent = StorageConstants.HopperPercent
    private var feederPercent = StorageConstants.FeederPercent

    private var feederLazyPercent = StorageConstants.FeederLazyPercent
    private var hopperLazyPercent = 0.0 // set in reset function

    // distance sensor to find balls

    public val sensorPosition: Int
        get() = -feeder.getSelectedSensorPosition(0)

    public val isLoadedBall: Boolean
        get() = sensorPosition >= StorageConstants.SensorThreshold

    init {
        tab.addNumber("feeder amperage", { feeder.getStatorCurrent() })
        tab.addNumber("IR pos", { sensorPosition.toDouble() })
        tab.addBoolean("IR Sensor", { isLoadedBall })
    }

    // subsystem functions

    override public fun periodic() {
        // do we need to partally load?
        if (mode == StorageMode.PASSIVE) {
            feeder.set(
                ControlMode.PercentOutput,
                if ( !isLoadedBall ) feederLazyPercent else 0.0
            )
        }
    }

    fun reset() {
        mode = StorageMode.OFF
    }

    override fun autoReset() {
        reset()

        hopperLazyPercent = StorageConstants.AutoHopperLazyPercent
    }

    override fun teleopReset() {
        reset()

        hopperLazyPercent = StorageConstants.HopperLazyPercent
    }
}
