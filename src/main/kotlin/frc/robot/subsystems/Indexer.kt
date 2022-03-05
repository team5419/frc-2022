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

import frc.robot.IndexerConstants
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout
import edu.wpi.first.wpilibj.AnalogInput

class Indexer(tab: ShuffleboardTab) : SubsystemBase() {

    // declare motors and ports
    val motor = CANSparkMax(IndexerConstants.Ports.motor, MotorType.kBrushless)
    public val encoder = motor.getEncoder()    
    public val sensor1 = AnalogInput(IndexerConstants.Ports.sensor1)
    public val sensor2 = AnalogInput(IndexerConstants.Ports.sensor2)
    public val sensor3 = AnalogInput(IndexerConstants.Ports.sensor3)

    private val layout: ShuffleboardLayout = tab.getLayout("Indexer", BuiltInLayouts.kList).withPosition(5, 0).withSize(1, 4);
    // configure the motors and add to shuffleboard
    init {
        motor.apply {
            restoreFactoryDefaults()
            setIdleMode(IdleMode.kCoast)
            setInverted(false)
            //setSensorPhase(false)
            setSmartCurrentLimit(40)
            setClosedLoopRampRate(1.0)
            setControlFramePeriodMs(1)
        }

        val controller = motor.getPIDController()
        controller.apply {
            setP(1.0, 1)
            setI(0.0, 1)
            setD(0.0, 1)
        }

        encoder.apply {
            setPosition(0.0)
        }

        layout.addNumber("Velocity", { encoder.getVelocity() })
        layout.addNumber("Sensor 1", { sensor1.getValue().toDouble() })
        layout.addNumber("Sensor 2", { sensor2.getValue().toDouble() })
        layout.addNumber("Sensor 3", { sensor3.getValue().toDouble() })
    }

    public fun stop() {
        motor.set(0.0)
    }

    public fun atPositionOne() : Boolean{
        return sensor1.getValue().toDouble() > 1000.0
    }

    public fun atPositionTwo() : Boolean {
        return sensor2.getValue().toDouble() > 1000.0
    }

    public fun atPositionThree() : Boolean {
        return sensor3.getValue().toDouble() > 1000.0
    }

    public fun index(percent: Double = 1.0) {
        motor.set(IndexerConstants.outputPercent * percent)
    }

    override fun periodic() {
    }

    override fun simulationPeriodic() {
    }
}