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
import frc.robot.Ports;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.CANSparkMax.IdleMode;

import frc.robot.FeederConstants
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout
import com.revrobotics.CANSparkMaxLowLevel.PeriodicFrame

class Feeder(tab: ShuffleboardTab) : SubsystemBase() {

    // declare motors and ports
    val motor = CANSparkMax(Ports.feeder, MotorType.kBrushless)
    public val encoder = motor.getEncoder() 
    private var previousVel : Double = -2.0
    
    private val layout: ShuffleboardLayout = tab.getLayout("Feeder", BuiltInLayouts.kList).withPosition(2, 2).withSize(1, 2)
    // configure the motors and add to shuffleboard
    init {
        motor.apply {
            restoreFactoryDefaults()
            setIdleMode(IdleMode.kCoast)
            setInverted(true)
            //setSensorPhase(false)
            setSmartCurrentLimit(40)
            setClosedLoopRampRate(1.0)
            setControlFramePeriodMs(50)
            setPeriodicFramePeriod(PeriodicFrame.kStatus2, 50)
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
        //layout.addNumber("Attempted", { currentVel })
        //layout.addNumber("Velocity", { encoder.getVelocity() })
    }

    public fun stop() {
        motor.set(0.0)
    }

    // public fun feed() {
    //     motor.set(currentVel)
    // }

    public var currentVel: Double = 0.0
    set(value: Double) {
            println("trying to set")
            if(value == previousVel) {
                return;
            }
            println("setting to ${value}");
            previousVel = value;
            motor.set(value);
    }

    override fun periodic() {
    }

    override fun simulationPeriodic() {
    }
}