package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.math.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.math.kinematics.SwerveDriveOdometry;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.math.kinematics.ChassisSpeeds
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
import edu.wpi.first.wpilibj.motorcontrol.Talon
import edu.wpi.first.math.geometry.Pose2d
import edu.wpi.first.math.geometry.Translation2d
import edu.wpi.first.wpilibj.smartdashboard.Field2d
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard

class Drivetrain(tab: ShuffleboardTab) : SubsystemBase() {

    // declare motors and ports
    public val turners: MutableList<TalonFX> = mutableListOf()
    public val drivers: MutableList<TalonFX> = mutableListOf()
    public var states: Array<SwerveModuleState> = arrayOf()
    public val gyro: PigeonIMU = PigeonIMU(/*DriveConstants.Ports.gyroPort*/0)
    public var inverted: Int = 1
    private val layout: ShuffleboardLayout = tab.getLayout("Drivetrain", BuiltInLayouts.kList).withPosition(2, 0).withSize(1, 2);
    private var previousThrottle: Double = -2.0;
    private var previousTurn: Double = -2.0;
    private val field: Field2d = Field2d();
    // configure the motors and add to shuffleboard
    init {
        for(i in 0..3) {
            turners.add(TalonFX(DriveConstants.turnerPorts[i]))
            turners[i].apply {
                configFactoryDefault(100)
                configSupplyCurrentLimit(SupplyCurrentLimitConfiguration(true, 40.0, 0.0, 0.0), 100)

                setSensorPhase(false)
                setInverted(false)

                // config_kP( 0, DriveConstants.PID.P , 100 )
                // config_kI( 0, DriveConstants.PID.I , 100 )
                // config_kD( 0, DriveConstants.PID.D , 100 )
                // config_kF( 0, 0.0 , 100 )

                setSelectedSensorPosition(0.0, 0, 100)

                configVoltageCompSaturation(12.0, 100)
                enableVoltageCompensation(true)
                setStatusFramePeriod(StatusFrame.Status_2_Feedback0, 10, 100)
                setControlFramePeriod(ControlFrame.Control_3_General, 10)

                setNeutralMode(NeutralMode.Coast)

                configClosedLoopPeakOutput(0, 0.1, 100)
            }
            drivers.add(TalonFX(DriveConstants.driverPorts[i]))
            drivers[i].apply {
                configFactoryDefault(100)
                configSupplyCurrentLimit(SupplyCurrentLimitConfiguration(true, 40.0, 0.0, 0.0), 100)

                setSensorPhase(false)
                setInverted(false)

                // config_kP( 0, DriveConstants.PID.P , 100 )
                // config_kI( 0, DriveConstants.PID.I , 100 )
                // config_kD( 0, DriveConstants.PID.D , 100 )
                // config_kF( 0, 0.0 , 100 )

                setSelectedSensorPosition(0.0, 0, 100)

                configVoltageCompSaturation(12.0, 100)
                enableVoltageCompensation(true)
                setStatusFramePeriod(StatusFrame.Status_2_Feedback0, 10, 100)
                setControlFramePeriod(ControlFrame.Control_3_General, 10)

                setNeutralMode(NeutralMode.Coast)

                configClosedLoopPeakOutput(0, 0.1, 100)
            }
        }

        gyro.apply {
            configFactoryDefault(100)
            setFusedHeading(0.0, 100)
        }
        SmartDashboard.putData("Field", field);
    }

    // Locations for the swerve drive modules relative to the robot center
    val m_frontLeftLocation: Translation2d = Translation2d(0.381, 0.381);
    val m_frontRightLocation: Translation2d = Translation2d(0.381, -0.381);
    val m_backLeftLocation: Translation2d = Translation2d(-0.381, 0.381);
    val m_backRightLocation: Translation2d = Translation2d(-0.381, -0.381);

    // Creating my kinematics object using the module locations
    val m_kinematics: SwerveDriveKinematics = SwerveDriveKinematics(
        m_frontLeftLocation, m_frontRightLocation, m_backLeftLocation, m_backRightLocation
    );

    // get angle from gyro
    val angle: Double
        get() = -gyro.getFusedHeading()

    var originalAngle : Double = 0.0

    // constructs object with angle from gyro (assuming starting position is (0,0))
    var odometry = SwerveDriveOdometry(m_kinematics, Rotation2d(angle))

    // returns the x and y position of the robot
    fun pose(): Pose2d {
        return odometry.getPoseMeters()
    }

    fun resetOdometry(pose: Pose2d) {
        odometry.resetPosition(pose, Rotation2d(angle));
    }

    // unit conversion functions
    fun nativeUnitsToMeters(units: Double): Double =
        (DriveConstants.wheelCircumference * units.toDouble() / DriveConstants.ticksPerRotation)
    fun nativeUnitsToMetersPerSecond(units: Double) =
        units * 10.0 / DriveConstants.ticksPerRotation * DriveConstants.wheelCircumference
    fun metersPerSecondToNativeUnits(units: Double)
        = (units / DriveConstants.wheelCircumference * DriveConstants.ticksPerRotation / 10)
    fun radiansToNativeUnits(units: Double): Double {
        return (units / (2 * Math.PI)) * (DriveConstants.ticksPerRotation / 10)
    }

    // set the percent output of the drivetrain motors
    fun drive(forward: Double, left: Double, rotation: Double) {
        val speeds: ChassisSpeeds = ChassisSpeeds(forward, left, rotation)
        states = m_kinematics.toSwerveModuleStates(speeds)
        updateMotors(states);
    }
    fun updateMotors(myStates: Array<SwerveModuleState>) {
        for(i in 0..3) {
            drivers[i].set(ControlMode.Velocity, metersPerSecondToNativeUnits(myStates[i].speedMetersPerSecond))
            turners[i].set(ControlMode.Velocity, radiansToNativeUnits(myStates[i].angle.getRadians()))
        }
    }
    fun withDeadband(movement: Double, deadband: Double): Double {
        if(abs(movement) <= deadband) return 0.0;
        if(abs(movement) > 1.0) return sign(movement);
        return movement;
    }

    public var brakeMode = false
        set(value: Boolean) {
            for(i in 0..3) {
                if( value) {
                    turners[i].setNeutralMode(NeutralMode.Brake)
                    drivers[i].setNeutralMode(NeutralMode.Brake)
                } else {
                    turners[i].setNeutralMode(NeutralMode.Coast)
                    drivers[i].setNeutralMode(NeutralMode.Coast)
                }
            }
        }

    override fun periodic() {
        odometry.update(Rotation2d(angle), states[0], states[1], states[2], states[3]);
    }

    override fun simulationPeriodic() {
    }
}
