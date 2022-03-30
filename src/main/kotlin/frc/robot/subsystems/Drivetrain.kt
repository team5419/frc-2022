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
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout
import edu.wpi.first.math.geometry.Pose2d

class Drivetrain(tab: ShuffleboardTab) : SubsystemBase() {

    // declare motors and ports
    public val leftLeader: TalonFX = TalonFX(DriveConstants.Ports.leftLeader)
    public val leftFollower: TalonFX = TalonFX(DriveConstants.Ports.leftFollower)
    public val rightLeader: TalonFX = TalonFX(DriveConstants.Ports.rightLeader)
    public val rightFollower: TalonFX = TalonFX(DriveConstants.Ports.rightFollower)
    public val gyro: PigeonIMU = PigeonIMU(DriveConstants.Ports.gyroPort)
    public var inverted: Int = 1
    private val layout: ShuffleboardLayout = tab.getLayout("Drivetrain", BuiltInLayouts.kList).withPosition(2, 0).withSize(1, 2);
    private var previousThrottle: Double = -2.0;
    private var previousTurn: Double = -2.0;
    // configure the motors and add to shuffleboard
    init {
        leftLeader.apply {
            configFactoryDefault(100)
            configSupplyCurrentLimit(SupplyCurrentLimitConfiguration(true, 15.0, 0.0, 0.0), 100)

            setSensorPhase(false)
            setInverted(false)

            config_kP( 0, DriveConstants.PID.P , 100 )
            config_kI( 0, DriveConstants.PID.I , 100 )
            config_kD( 0, DriveConstants.PID.D , 100 )
            config_kF( 0, 0.0 , 100 )

            setSelectedSensorPosition(0.0, 0, 100)

            configVoltageCompSaturation(12.0, 100)
            enableVoltageCompensation(true)
            setStatusFramePeriod(StatusFrame.Status_2_Feedback0, 50, 100)
            setControlFramePeriod(ControlFrame.Control_3_General, 50)

            setNeutralMode(NeutralMode.Coast)

            configClosedLoopPeakOutput(0, 0.1, 100)
        }

        rightLeader.apply {
            configFactoryDefault(100)
            configSupplyCurrentLimit(SupplyCurrentLimitConfiguration(true, 20.0, 0.0, 0.0), 100)

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

        leftFollower.apply {
            configFactoryDefault(100)
            configSupplyCurrentLimit(SupplyCurrentLimitConfiguration(true, 20.0, 0.0, 0.0), 100)

            // follow the master
            follow(leftLeader)
            setInverted(InvertType.FollowMaster)

            configVoltageCompSaturation(12.0, 100)
            enableVoltageCompensation(true)
            
            configClosedLoopPeakOutput(0, 0.1, 100)
        }

        rightFollower.apply {
            configFactoryDefault(100)
            configSupplyCurrentLimit(SupplyCurrentLimitConfiguration(true, 20.0, 0.0, 0.0), 100)

            // follow the master
            follow(rightLeader)
            setInverted(InvertType.FollowMaster)

            configVoltageCompSaturation(12.0, 100)
            enableVoltageCompensation(true)

            configClosedLoopPeakOutput(0, 0.1, 100)
        }

         gyro.apply {
            configFactoryDefault(100)
            setFusedHeading(0.0, 100)
        }
        //layout.addNumber("left velocity", { leftLeader.getSelectedSensorVelocity(0) + 0.0 })
        //layout.addNumber("right velocity", { rightLeader.getSelectedSensorVelocity(0) + 0.0 })
        //layout.addBoolean("brake mode", { brakeMode})
    }

    fun getAllVelocities() : List<Double> {
        return listOf(
            leftLeader.getSelectedSensorVelocity(0),
            leftFollower.getSelectedSensorVelocity(0),
            rightLeader.getSelectedSensorVelocity(0),
            rightFollower.getSelectedSensorVelocity(0)
        );
    }

    // get angle from gyro
    val angle: Double
        get() = -gyro.getFusedHeading()

    // constructs object with angle from gyro (assuming starting position is (0,0))
    var odometry = DifferentialDriveOdometry(Rotation2d(angle))

    // returns the x and y position of the robot
    val pose: Pose2d
        get() = odometry.getPoseMeters()

    // unit conversion functions
    fun nativeUnitsToMeters(units: Double): Double =
        (DriveConstants.wheelCircumference * units.toDouble() / DriveConstants.ticksPerRotation)
    fun nativeUnitsToMetersPerSecond(units: Double) =
        units * 10.0 / DriveConstants.ticksPerRotation * DriveConstants.wheelCircumference
    fun metersPerSecondToNativeUnits(units: Double)
        = (units / DriveConstants.wheelCircumference * DriveConstants.ticksPerRotation / 10)

    // get distances and velocities of both sides of the robot from the encoders
    val leftDistance: Double
        get() = nativeUnitsToMeters(leftLeader.getSelectedSensorPosition(0))
    val rightDistance: Double
        get() = nativeUnitsToMeters(rightLeader.getSelectedSensorPosition(0))
    val leftVelocity: Double // meters per second
        get() = nativeUnitsToMetersPerSecond(leftLeader.getSelectedSensorVelocity(0))
    val rightVelocity: Double
        get() = nativeUnitsToMetersPerSecond(rightLeader.getSelectedSensorVelocity(0))
    val averageSpeed: Double // meters per second
        get() = ((Math.abs(leftVelocity) + Math.abs(rightVelocity))/2)

    // set the percent output of the drivetrain motors
    fun setPercent(left: Double, right: Double){
        
        leftLeader.set(ControlMode.PercentOutput, left)
        rightLeader.set(ControlMode.PercentOutput, right)
    }

    // set the velocity of the drivetrain motors
    fun setVelocity(leftVelocity: Double, rightVelocity: Double, leftFF: Double, rightFF: Double) {
        leftLeader.set(
            ControlMode.Velocity, metersPerSecondToNativeUnits(leftVelocity),
            DemandType.ArbitraryFeedForward, leftFF / 12.0
        )
        rightLeader.set(
            ControlMode.Velocity, metersPerSecondToNativeUnits(rightVelocity),
            DemandType.ArbitraryFeedForward, rightFF / 12.0
        )
    }

    // maintains a deadband
    fun withDeadband(movement: Double, deadband: Double): Double {
        if(abs(movement) <= deadband) return 0.0;
        if(abs(movement) > 1.0) return sign(movement);
        return movement;
    }

    public fun drive(throttle: Double, turn: Double, isSlow: Boolean) {
        if(throttle == previousThrottle && turn == previousTurn) {
            return;
        }
        previousThrottle = throttle;
        previousTurn = turn;
        // set slow multiplier
        var slow: Double = 1.0
        if(isSlow) slow = DriveConstants.slowMultiplier
        // set percent outputs of drivetrain motors
        //println("throttle output ${throttle - turn - howFarOver}, ${throttle + turn - howFarOver}")
        leftLeader.set(ControlMode.PercentOutput, withDeadband((inverted * throttle - turn) * slow, 0.001))
        rightLeader.set(ControlMode.PercentOutput, withDeadband((inverted * throttle + turn) * slow, 0.001))
    }

    public var brakeMode = false
        set(value: Boolean) {
            if(value) {
                leftLeader.setNeutralMode(NeutralMode.Brake)
                rightLeader.setNeutralMode(NeutralMode.Brake)
            } else {
                leftLeader.setNeutralMode(NeutralMode.Coast)
                rightLeader.setNeutralMode(NeutralMode.Coast)
            }
        }

    override fun periodic() {
        // update the odometry of the field with new gyro and encoder values
        odometry.update(Rotation2d.fromDegrees(angle), leftDistance, rightDistance)
    }

    override fun simulationPeriodic() {
    }
}
