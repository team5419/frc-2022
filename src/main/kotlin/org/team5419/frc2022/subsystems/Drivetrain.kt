package org.team5419.frc2022.subsystems
import com.ctre.phoenix.sensors.PigeonIMU
import com.ctre.phoenix.motorcontrol.can.TalonFX
import com.ctre.phoenix.motorcontrol.can.TalonSRX
import com.ctre.phoenix.motorcontrol.*
import org.team5419.frc2022.DriveConstants
import kotlin.math.*

object Drivetrain: Subsystem("Drivetrain") {
    val leftLeader: TalonFX = TalonFX(DriveConstants.Left.leaderPort)

    private val leftFollower: TalonFX = TalonFX(DriveConstants.Left.followerPort)

    val rightLeader: TalonFX = TalonFX(DriveConstants.Right.leaderPort)

    private val rightFollower: TalonFX = TalonFX(DriveConstants.Right.followerPort)

    init {
        leftFollower.apply {
            configFactoryDefault(100)
            configSupplyCurrentLimit(SupplyCurrentLimitConfiguration(true, 40.0, 0.0, 0.0), 100)

            // follow the master
            follow(leftLeader)
            setInverted(InvertType.FollowMaster)

            configVoltageCompSaturation(12.0, 100)
            enableVoltageCompensation(true)
        }

        rightFollower.apply {
            configFactoryDefault(100)
            configSupplyCurrentLimit(SupplyCurrentLimitConfiguration(true, 40.0, 0.0, 0.0), 100)

            // follow the master
            follow(rightLeader)
            setInverted(InvertType.FollowMaster)

            configVoltageCompSaturation(12.0, 100)
            enableVoltageCompensation(true)
        }

        leftLeader.apply {
            configFactoryDefault(100)
            configSupplyCurrentLimit(SupplyCurrentLimitConfiguration(true, 40.0, 0.0, 0.0), 100)

            setSensorPhase(false)
            setInverted(true)

            config_kP( 0, DriveConstants.PID.P , 100 )
            config_kI( 0, DriveConstants.PID.I , 100 )
            config_kD( 0, DriveConstants.PID.D , 100 )
            config_kF( 0, 0.0 , 100 )

            setSelectedSensorPosition(0, 0, 100)

            configVoltageCompSaturation(12.0, 100)
            enableVoltageCompensation(true)
            setStatusFramePeriod(StatusFrame.Status_2_Feedback0, 10, 100)

            setNeutralMode(NeutralMode.Coast)
        }

        rightLeader.apply {
            configFactoryDefault(100)
            configSupplyCurrentLimit(SupplyCurrentLimitConfiguration(true, 40.0, 0.0, 0.0), 100)

            setSensorPhase(false)
            setInverted(false)

            config_kP( 0, DriveConstants.PID.P , 100 )
            config_kI( 0, DriveConstants.PID.I , 100 )
            config_kD( 0, DriveConstants.PID.D , 100 )
            config_kF( 0, 0.0 , 100 )

            setSelectedSensorPosition(0, 0, 100)

            configVoltageCompSaturation(12.0, 100)
            enableVoltageCompensation(true)
            setStatusFramePeriod(StatusFrame.Status_2_Feedback0, 10, 100)

            setNeutralMode(NeutralMode.Coast)
        }
    }

    fun withDeadband(movement: Double, deadband: Double): Double {
        if(abs(movement) <= deadband) {
            return 0.0;
        }
        return movement;
    }

    public fun drive(throttle: Double, turn: Double, isSlow: Boolean) {
        val howFarOver = max(0.0, throttle + turn - 1)
        var slow: Double = 1.0
        if(isSlow) {
            slow = DriveConstants.slowMultiplier
        }
        leftLeader.set(ControlMode.PercentOutput, withDeadband((throttle - turn - howFarOver) * slow * 0.1, 0.001))
        rightLeader.set(ControlMode.PercentOutput, withDeadband((throttle + turn - howFarOver) * slow * 0.1, 0.001))
    }

    override public fun reset() {
        leftLeader.set(ControlMode.PercentOutput, 0.0)
        rightLeader.set(ControlMode.PercentOutput, 0.0)
    }
}
