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
class Intake(tab: ShuffleboardTab) : SubsystemBase() {

    // declare motors and ports
    val motor = TalonFX(IntakeConstants.Ports.motor)
    var deployMotor: CANSparkMax = CANSparkMax(IntakeConstants.Ports.deployMotor, MotorType.kBrushless)
    var controller = deployMotor.getPIDController()
    public var encoder = deployMotor.getEncoder()    
    private val layout: ShuffleboardLayout = tab.getLayout("Intake", BuiltInLayouts.kList).withPosition(8, 0).withSize(2, 1);
    private var deploySetpoint: Double = -2.0;
    public var setpointTicks: Double = 0.0;
    // configure the motors and add to shuffleboard
    init {

        motor.apply {
            configFactoryDefault(100)
            configSupplyCurrentLimit(SupplyCurrentLimitConfiguration(true, 20.0, 0.0, 0.0), 100)

            setSensorPhase(false)
            setInverted(false)

            config_kP( 0, 1.0, 100 )
            config_kI( 0, 0.0, 100 )
            config_kD( 0, 0.0, 100 )

            setSelectedSensorPosition(0.0, 0, 100)

            configVoltageCompSaturation(12.0, 100)
            enableVoltageCompensation(true)
            setStatusFramePeriod(StatusFrame.Status_2_Feedback0, 50, 100)
            setControlFramePeriod(ControlFrame.Control_3_General, 50)
            setNeutralMode(NeutralMode.Coast)

            configClosedLoopPeakOutput(0, 0.1, 100)
        }

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
        layout.addNumber("Deploy pos", { encoder.getPosition() })
        
    }

    public fun stop() {
        motor.set(ControlMode.PercentOutput, 0.0)
    }

    public fun deployStop() {
        deployMotor.set(0.0)
    }

    public fun intake(velocity: Double = 1.0) {
        motor.set(ControlMode.PercentOutput, IntakeConstants.outputPercent * velocity)
    }

    public fun positionDeploy(position: Double) {
        println("setting intake pos ${position}");
        controller.setReference(position, CANSparkMax.ControlType.kPosition);
        println("deploying intake !!!")

    }

    public fun reverse() {
        motor.set(ControlMode.PercentOutput, IntakeConstants.reversePercent)
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