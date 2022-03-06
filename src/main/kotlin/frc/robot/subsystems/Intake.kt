package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.ctre.phoenix.motorcontrol.can.TalonFX
import com.ctre.phoenix.motorcontrol.can.TalonSRX
import edu.wpi.first.networktables.NetworkTableEntry
import edu.wpi.first.networktables.EntryListenerFlags
import edu.wpi.first.networktables.EntryNotification
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets
import com.ctre.phoenix.motorcontrol.*
import kotlin.math.*

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.CANSparkMax.IdleMode;

import frc.robot.IntakeConstants
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout

class Intake(tab: ShuffleboardTab) : SubsystemBase() {

    // declare motors and ports
    val motor = TalonFX(IntakeConstants.Ports.motor)
    private val layout: ShuffleboardLayout = tab.getLayout("Intake", BuiltInLayouts.kList).withPosition(8, 0).withSize(2, 1);
    // configure the motors and add to shuffleboard
    init {

        motor.apply {
            configFactoryDefault(100)
            configSupplyCurrentLimit(SupplyCurrentLimitConfiguration(true, 20.0, 0.0, 0.0), 100)

            setSensorPhase(false)
            setInverted(true)

            config_kP( 0, 1.0, 100 )
            config_kI( 0, 0.0, 100 )
            config_kD( 0, 0.0, 100 )

            setSelectedSensorPosition(0.0, 0, 100)

            configVoltageCompSaturation(12.0, 100)
            enableVoltageCompensation(true)
            setStatusFramePeriod(StatusFrame.Status_2_Feedback0, 10, 100)

            setNeutralMode(NeutralMode.Coast)

            configClosedLoopPeakOutput(0, 0.1, 100)
        }

        layout.addNumber("Velocity", { motor.getSelectedSensorVelocity() })
    }

    public fun stop() {
        motor.set(ControlMode.PercentOutput, 0.0)
    }

    public fun intake() {
        motor.set(ControlMode.PercentOutput, IntakeConstants.outputPercent)
    }

    public fun reverse() {
        motor.set(ControlMode.PercentOutput, IntakeConstants.reversePercent)
    }

    override fun periodic() {
    }

    override fun simulationPeriodic() {
    }
}