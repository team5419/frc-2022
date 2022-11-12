package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.math.kinematics.SwerveDriveOdometry;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.math.kinematics.ChassisSpeeds
import edu.wpi.first.math.geometry.Rotation2d;
import com.ctre.phoenix.sensors.PigeonIMU
import com.ctre.phoenix.sensors.BasePigeonSimCollection
import com.ctre.phoenix.motorcontrol.can.TalonFX
import com.ctre.phoenix.motorcontrol.can.TalonSRX
import com.ctre.phoenix.motorcontrol.*
import kotlin.math.*
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout
import edu.wpi.first.wpilibj.motorcontrol.Talon
import edu.wpi.first.math.geometry.Pose2d
import edu.wpi.first.math.geometry.Translation2d
import edu.wpi.first.wpilibj.smartdashboard.Field2d
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard


import edu.wpi.first.math.kinematics.DifferentialDriveOdometry;

import frc.robot.DriveConstants


class Drivetrain() : SubsystemBase() {
    private val leftLeader: TalonFX;
    private val leftFollower: TalonFX;
    private val rightLeader: TalonFX;
    private val rightFollower: TalonFX;

    public var mod: Double;

    
    public val gyro: PigeonIMU = PigeonIMU(DriveConstants.Ports.gyroPort)
    // configure the motors and add to shuffleboard
    
    init {
        leftLeader = TalonFX(4)
        rightLeader = TalonFX(5)
        leftFollower = TalonFX(2)
        rightFollower = TalonFX(3)
        val motors: Array<TalonFX> = arrayOf(leftLeader, leftFollower, rightLeader, rightFollower);
        val inverted: Array<Boolean> = arrayOf(false, false, true, true);
        mod = 1.0;
        for(i in 0..3) {
            motors[i].apply {
                //configSupplyCurrentLimit(SupplyCurrentLimitConfiguration(true, 40.0, 0.0, 0.0), 100)

                setSensorPhase(false)
                setInverted(inverted[i])

                config_kP( 0, 0.0, 100 )
                config_kI( 0, 0.0, 100 )
                config_kD( 0, 0.0, 100 )
                config_kF( 0, 0.0 , 100 )

                setSelectedSensorPosition(0.0, 0, 100)

                configVoltageCompSaturation(12.0, 100)
                enableVoltageCompensation(true)
                setStatusFramePeriod(StatusFrame.Status_2_Feedback0, 10, 100)

                setNeutralMode(NeutralMode.Coast)

                configClosedLoopPeakOutput(0, 1.0, 100)
            }
        }
    }
    fun drive(throttle: Double, turn : Double) {
        //println(throttle);
        leftLeader.set(ControlMode.PercentOutput, (throttle - (turn * 0.5)) * mod);
        rightLeader.set(ControlMode.PercentOutput, (throttle + (turn * 0.5)) * mod);
        leftFollower.set(ControlMode.PercentOutput, (throttle - (turn * 0.5)) * mod);
        rightFollower.set(ControlMode.PercentOutput, (throttle + (turn * 0.5)) * mod);
    }



        // unit conversion functions
    fun nativeUnitsToMeters(units: Double): Double =
        (DriveConstants.wheelCircumference * units.toDouble() / DriveConstants.ticksPerRotation)
    fun nativeUnitsToMetersPerSecond(units: Double) =
        units * 10.0 / DriveConstants.ticksPerRotation * DriveConstants.wheelCircumference
    fun metersPerSecondToNativeUnits(units: Double)
        = (units / DriveConstants.wheelCircumference * DriveConstants.ticksPerRotation / 10)

        // get angle from gyro
    val angle: Double
        get() = -gyro.getFusedHeading()
     // constructs object with angle from gyro (assuming starting position is (0,0))
    var odometry = DifferentialDriveOdometry(Rotation2d(angle))
    // returns the x and y position of the robot
    val pose: Pose2d
        get() = odometry.getPoseMeters()

    // set the velocity of the drivetrain motors
    fun setVelocity(leftVelocity: Double, rightVelocity: Double, leftFF: Double, rightFF: Double) {
        println("setting left velocity ${leftVelocity}, ff ${leftFF}");
        leftLeader.set(
            ControlMode.Velocity, metersPerSecondToNativeUnits(leftVelocity),
            DemandType.ArbitraryFeedForward, leftFF / 12.0
        )
        rightLeader.set(
            ControlMode.Velocity, metersPerSecondToNativeUnits(rightVelocity),
            DemandType.ArbitraryFeedForward, rightFF / 12.0
        )
    }


    override fun periodic() {
    }

    override fun simulationPeriodic() {
    }
}