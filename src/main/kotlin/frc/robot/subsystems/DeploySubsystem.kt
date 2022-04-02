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
import com.revrobotics.CANSparkMaxLowLevel.PeriodicFrame

class DeploySubsystem(tab: ShuffleboardTab) : SubsystemBase() {

    // declare motors and ports
    var deployMotor: CANSparkMax = CANSparkMax(IntakeConstants.Ports.deployMotor, MotorType.kBrushless)
    var controller = deployMotor.getPIDController()
    public var encoder = deployMotor.getEncoder()    
    
    private var deploySetpoint: Double = -2.0;
    public var setpointTicks: Double = 0.0;
    // configure the motors and add to shuffleboard
    init {

        deployMotor.apply {
            restoreFactoryDefaults()
            setIdleMode(IdleMode.kBrake)
            setInverted(false)
            //setSensorPhase(false)
            setSmartCurrentLimit(40)
            setClosedLoopRampRate(1.0)
            setControlFramePeriodMs(50)
            setPeriodicFramePeriod(PeriodicFrame.kStatus2, 50)
        }

        
        controller.apply {
            setP(0.05, 1)
            setI(0.0, 1)
            setD(0.0, 1)
            setFF(10.0, 1)
        }

        encoder.apply {
            setPosition(0.0)
        }

        //layout.addNumber("Velocity", { motor.getSelectedSensorVelocity() })
        
    }

    public fun deployStop() {
        deployMotor.set(0.0)
    }


    public fun positionDeploy(position: Double) {
        println("setting intake pos ${position}");
        controller.setReference(position, CANSparkMax.ControlType.kPosition);
        println("deploying intake !!!")

    }

    public fun changeSetpoint(setpoint: Double) {
        setpointTicks = setpoint
    }

    public fun runDeploy(percent: Double = 1.0) {
        if(percent == deploySetpoint) {
            return;
        }
        deploySetpoint = percent;
        deployMotor.set(percent)
    }

    override fun periodic() {
        //println(encoder.getPosition());
    }

    override fun simulationPeriodic() {
    }
}