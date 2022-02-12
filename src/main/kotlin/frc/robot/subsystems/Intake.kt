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

class Intake(tab: ShuffleboardTab) : SubsystemBase() {

    // declare motors and ports
    val motor = CANSparkMax(IntakeConstants.Ports.motor, MotorType.kBrushless)
    public val encoder = motor.getEncoder()

    // configure the motors and add to shuffleboard
    init {
        motor.apply {
            restoreFactoryDefaults()
            setIdleMode(IdleMode.kCoast)
            setInverted(true)
            //setSensorPhase(false)
            setSmartCurrentLimit(40)
            setClosedLoopRampRate(1.0)
            setControlFramePeriodMs(1)
        }

        val controller = motor.getPIDController()
        controller.apply {
            setP(0.5, 1)
            setI(0.0, 1)
            setD(0.0, 1)
        }

        encoder.apply {
            setPosition(0.0)
        }

        tab.addNumber("Intake Real Velocity", { encoder.getVelocity() })
    }

    public fun stop() {
        motor.set(0.0)
    }

    public fun intake() {
        motor.set(IntakeConstants.outputPercent)
    }

    override fun periodic() {
    }

    override fun simulationPeriodic() {
    }
}