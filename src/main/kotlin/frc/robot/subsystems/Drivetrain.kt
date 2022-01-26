// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.math.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.math.geometry.Rotation2d;
import com.ctre.phoenix.sensors.PigeonIMU
import com.ctre.phoenix.motorcontrol.can.TalonFX
import com.ctre.phoenix.motorcontrol.can.TalonSRX
import com.ctre.phoenix.motorcontrol.*
import kotlin.math.*

import frc.robot.DriveConstants
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab

class Drivetrain(tab: ShuffleboardTab) : SubsystemBase() {

    val leftLeader: TalonFX = TalonFX(DriveConstants.Ports.leftLeader)

    private val leftFollower: TalonFX = TalonFX(DriveConstants.Ports.leftFollower)

    val rightLeader: TalonFX = TalonFX(DriveConstants.Ports.rightLeader)

    private val rightFollower: TalonFX = TalonFX(DriveConstants.Ports.rightFollower)

    public val gyro: PigeonIMU = PigeonIMU(DriveConstants.Ports.gyroPort)


    init {
        leftFollower.apply {
            configFactoryDefault(100)
            configSupplyCurrentLimit(SupplyCurrentLimitConfiguration(true, 40.0, 0.0, 0.0), 100)

            // follow the master
            follow(leftLeader)
            setInverted(InvertType.FollowMaster)

            configVoltageCompSaturation(12.0, 100)
            enableVoltageCompensation(true)
            
            configClosedLoopPeakOutput(0, 0.1, 100)
        }

        rightFollower.apply {
            configFactoryDefault(100)
            configSupplyCurrentLimit(SupplyCurrentLimitConfiguration(true, 40.0, 0.0, 0.0), 100)

            // follow the master
            follow(rightLeader)
            setInverted(InvertType.FollowMaster)

            configVoltageCompSaturation(12.0, 100)
            enableVoltageCompensation(true)

            configClosedLoopPeakOutput(0, 0.1, 100)
        }

        leftLeader.apply {
            configFactoryDefault(100)
            configSupplyCurrentLimit(SupplyCurrentLimitConfiguration(true, 40.0, 0.0, 0.0), 100)

            setSensorPhase(false)
            setInverted(false)

            config_kP( 0, DriveConstants.PID.P , 100 )
            config_kI( 0, DriveConstants.PID.I , 100 )
            config_kD( 0, DriveConstants.PID.D , 100 )
            config_kF( 0, 0.0 , 100 )

            setSelectedSensorPosition(0.0, 0, 100)

            configVoltageCompSaturation(12.0, 100)
            enableVoltageCompensation(true)
            setStatusFramePeriod(StatusFrame.Status_2_Feedback0, 10, 100)

            setNeutralMode(NeutralMode.Coast)

            configClosedLoopPeakOutput(0, 0.1, 100)
        }

        rightLeader.apply {
            configFactoryDefault(100)
            configSupplyCurrentLimit(SupplyCurrentLimitConfiguration(true, 40.0, 0.0, 0.0), 100)

            setSensorPhase(true)
            setInverted(true)

            config_kP( 0, DriveConstants.PID.P , 100 )
            config_kI( 0, DriveConstants.PID.I , 100 )
            config_kD( 0, DriveConstants.PID.D , 100 )
            config_kF( 0, 0.0 , 100 )

            setSelectedSensorPosition(0.0, 0, 100)

            configVoltageCompSaturation(12.0, 100)
            enableVoltageCompensation(true)
            setStatusFramePeriod(StatusFrame.Status_2_Feedback0, 10, 100)

            setNeutralMode(NeutralMode.Coast)
        }

         gyro.apply {
            configFactoryDefault(100)
            setFusedHeading(0.0, 100)
        }

        tab.addNumber("left vel", { leftLeader.getSelectedSensorVelocity(0) + 0.0 })
        tab.addNumber("right vel", { rightLeader.getSelectedSensorVelocity(0) + 0.0 })

    }

    val angle: Double
            get() = -gyro.getFusedHeading()

    var odometry = DifferentialDriveOdometry(Rotation2d(angle))

    val pose
        get() = odometry.getPoseMeters()

    fun nativeUnitsToMeters(units: Double): Double =
        (DriveConstants.wheelCircumference * units.toDouble() / DriveConstants.ticksPerRotation)

    fun nativeUnitsToMetersPerSecond(units: Double) =
        units * 10.0 / DriveConstants.ticksPerRotation * DriveConstants.wheelCircumference

    val leftDistance: Double
        get() = nativeUnitsToMeters(leftLeader.getSelectedSensorPosition(0))

    val rightDistance: Double
        get() = nativeUnitsToMeters(rightLeader.getSelectedSensorPosition(0))

    val leftVelocity: Double // meters per second
        get() = nativeUnitsToMetersPerSecond(leftLeader.getSelectedSensorVelocity(0))

    val rightVelocity: Double
        get() = nativeUnitsToMetersPerSecond(rightLeader.getSelectedSensorVelocity(0))

    val averageSpeed: Double // meters per second
        get() = ((Math.abs(leftVelocity) + Math.abs(leftVelocity))/2)

    fun setPercent(left: Double, right: Double){
        leftLeader.set(ControlMode.PercentOutput, left)
        rightLeader.set(ControlMode.PercentOutput, right)
    }


    fun setVelocity(
        leftVelocity: Double,
        rightVelocity: Double,
        leftFF: Double,
        rightFF: Double
    ) {

        leftLeader.set(
            ControlMode.Velocity, metersPerSecondToNativeUnits(leftVelocity),
            DemandType.ArbitraryFeedForward, leftFF / 12.0
        )
        rightLeader.set(
            ControlMode.Velocity, metersPerSecondToNativeUnits(rightVelocity),
            DemandType.ArbitraryFeedForward, rightFF / 12.0
        )
    }

    fun metersPerSecondToNativeUnits(units: Double)
        = (units / DriveConstants.wheelCircumference * DriveConstants.ticksPerRotation / 10)

    fun withDeadband(movement: Double, deadband: Double): Double {
        if(abs(movement) <= deadband) {
            return 0.0;
        }
        return movement;
    }

    public fun drive(throttle: Double, turn: Double, isSlow: Boolean) {
        //println("drive run with throttle ${throttle}, turn ${turn}")
        val howFarOver = max(0.0, throttle + turn - 1)
        var slow: Double = 1.0
        if(isSlow) {
            slow = DriveConstants.slowMultiplier
        }
        //println(leftLeader)
        leftLeader.set(ControlMode.PercentOutput, withDeadband((throttle - turn - howFarOver) * slow * 0.6, 0.001))
        rightLeader.set(ControlMode.PercentOutput, withDeadband((throttle + turn - howFarOver) * slow * 0.6, 0.001))
        //leftLeader.set(ControlMode.PercentOutput, 1.0)
    }

    public var brakeMode = false
        set(value: Boolean) {
            if(value == field) return
            if(value) {
                leftLeader.setNeutralMode(NeutralMode.Brake)
                rightLeader.setNeutralMode(NeutralMode.Brake)
            } else {
                leftLeader.setNeutralMode(NeutralMode.Coast)
                rightLeader.setNeutralMode(NeutralMode.Coast)
            }
        }

    override fun periodic() {
        odometry.update(
            Rotation2d.fromDegrees(angle),
            leftDistance,
            rightDistance
        )
        //println("Left distance " + leftDistance + " Right distance " + rightDistance + " angle " + angle)
        //println("Pose " + pose)
    // This method will be called once per scheduler run
    }

    override fun simulationPeriodic() {
    // This method will be called once per scheduler run during simulation
    }
}
