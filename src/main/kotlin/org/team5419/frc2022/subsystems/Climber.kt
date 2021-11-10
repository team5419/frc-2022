package org.team5419.frc2022.subsystems

import org.team5419.frc2022.tab
import org.team5419.frc2022.ClimberConstants
import org.team5419.frc2022.fault.subsystems.Subsystem
import org.team5419.frc2022.fault.math.units.native.NativeUnitRotationModel
import org.team5419.frc2022.fault.math.units.native.*
import org.team5419.frc2022.fault.math.units.derived.*
import org.team5419.frc2022.fault.math.units.*
import org.team5419.frc2022.fault.hardware.ctre.BerkeliumSRX
import kotlin.math.PI
import edu.wpi.first.wpilibj.shuffleboard.*
import edu.wpi.first.wpilibj.Timer
import edu.wpi.first.networktables.EntryListenerFlags
import com.ctre.phoenix.motorcontrol.can.TalonSRX
import com.ctre.phoenix.motorcontrol.NeutralMode
import com.ctre.phoenix.motorcontrol.FeedbackDevice
import com.ctre.phoenix.motorcontrol.ControlMode

object Climber : Subsystem("Climber") {
    // motor

    private val deployMotor = TalonSRX(ClimberConstants.DeployPort)
        .apply {
            configFactoryDefault(100)

            setInverted(true)

            configPeakOutputForward(1.0)
            configPeakOutputReverse(-1.0)

        }

    private val winchMotor = TalonSRX(ClimberConstants.WinchPort)
        .apply{
            configFactoryDefault(100)

            setNeutralMode(NeutralMode.Brake)

            configPeakOutputForward(1.0)
            configPeakOutputReverse(-1.0)
        }

    // deploy

    fun deploy(percent: Double = 0.5) {
        deployMotor.set(ControlMode.PercentOutput, percent)
    }

    fun retract(percent: Double = -0.7) {
        deployMotor.set(ControlMode.PercentOutput, percent)
    }

    fun stopDeploy() {
        deployMotor.set(ControlMode.PercentOutput, 0.0)
    }

    // winch

    fun winch(percent: Double = 1.0) {
        winchMotor.set(ControlMode.PercentOutput, percent)
    }

    fun retractWinch(percent: Double = -1.0) {
        winchMotor.set(ControlMode.PercentOutput, percent)
    }

    fun stopWinch() {
        winchMotor.set(ControlMode.PercentOutput, 0.0)

    }

    // are forces combined

    fun stop() {
        stopDeploy()
        stopWinch()
    }

    // subsystem functions

    override fun autoReset() = stop()
    override fun teleopReset() = stop()
}
