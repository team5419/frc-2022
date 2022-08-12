package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.math.kinematics.DifferentialDriveOdometry;
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
import frc.robot.modules.SwerveModule;
import frc.robot.modules.ISwerveModule;
import frc.robot.modules.SimulatedSwerveModule;
import frc.robot.modules.Module;
import frc.robot.Util;

class Drivetrain(simulated: Boolean = false) : SubsystemBase() {

    // declare motors and ports
    public val drivers: Array<ISwerveModule> = arrayOf(
        Module.create(DriveConstants.driverPorts[0], DriveConstants.turnerPorts[0], DriveConstants.cancoderPorts[0], simulated),
        Module.create(DriveConstants.driverPorts[1], DriveConstants.turnerPorts[1], DriveConstants.cancoderPorts[1], simulated),
        Module.create(DriveConstants.driverPorts[2], DriveConstants.turnerPorts[2], DriveConstants.cancoderPorts[2], simulated),
        Module.create(DriveConstants.driverPorts[3], DriveConstants.turnerPorts[3], DriveConstants.cancoderPorts[3], simulated)
    );
    public val gyro: PigeonIMU = PigeonIMU(DriveConstants.gyroPort);
    private val gyroSim: BasePigeonSimCollection = BasePigeonSimCollection(gyro, false);
    private var yaw: Double = 0.0;
    public var inverted: Int = 1;
    private val tab: ShuffleboardTab = Shuffleboard.getTab("Drivetrain");
    private val field: Field2d = Field2d();
    private var previousMove: ChassisSpeeds = ChassisSpeeds();
    // configure the motors and add to shuffleboard
    init {
        gyro.apply {
            configFactoryDefault(100)
            setFusedHeading(0.0, 100)
        }
        SmartDashboard.putData("Field", field);
        tab.add("angle (rad)", { angle });
        tab.add("x m", { pose().getX() });
        tab.add("y m", { pose().getY() });
        tab.add("brake", { brakeMode });
        tab.add("forward m/s", { previousMove.vxMetersPerSecond });
        tab.add("sideways m/s", { previousMove.vyMetersPerSecond });
        tab.add("turning (rad)", { previousMove.omegaRadiansPerSecond });
    }

    // get angle from gyro
    val angle: Double
        get() = -gyro.getFusedHeading()

    var originalAngle : Double = 0.0

    // constructs object with angle from gyro (assuming starting position is (0,0))
    var odometry = SwerveDriveOdometry(DriveConstants.kinematics, Rotation2d(angle))

    // returns the x and y position of the robot
    fun pose(): Pose2d {
        return odometry.getPoseMeters()
    }

    fun resetOdometry(pose: Pose2d) {
        odometry.resetPosition(pose, Rotation2d(angle));
    }

    // set the percent output of the drivetrain motors
    fun drive(forward: Double, left: Double, rotation: Double) {
        val speeds: ChassisSpeeds = ChassisSpeeds(forward, left, rotation);
        this.previousMove = speeds;
        val states: Array<SwerveModuleState> = DriveConstants.kinematics.toSwerveModuleStates(speeds);
        SwerveDriveKinematics.desaturateWheelSpeeds(states, DriveConstants.Ramsete.maxVelocity);
        updateMotors(states);
    }
    fun updateMotors(myStates: Array<SwerveModuleState>): Int {
        for(i in 0..drivers.size - 1) {
            drivers[i].setDesiredState(myStates[i]);
        }
        return 0;
    }
    fun withDeadband(movement: Double, deadband: Double): Double {
        if(abs(movement) <= deadband) return 0.0;
        if(abs(movement) > 1.0) return sign(movement);
        return movement;
    }

    public var brakeMode = false
        set(value: Boolean) {
            for(i in 0..drivers.size - 1) {
                drivers[i].setBrakeMode(value);
            }
        }
    
    fun getStates(): Array<SwerveModuleState> {
        return Array<SwerveModuleState>(drivers.size, { i: Int -> drivers[i].getState() });
    }
    override fun periodic() {
        val res: Array<SwerveModuleState> = getStates();
        val cangle: Rotation2d = Rotation2d(angle);
        val cpose: Pose2d = pose();
        odometry.update(cangle, res[0], res[1], res[2], res[3]);
        for(i in 0..drivers.size - 1) {
            val modulePositionFromChassis: Translation2d = DriveConstants.modulePositions[i]
                    .rotateBy(cangle)
                    .plus(cpose.getTranslation());
            val pose: Pose2d = Pose2d(modulePositionFromChassis, drivers[i].getTurn().plus(cpose.getRotation()));
            field.getObject("Swerve Module ${i}").setPose(pose);
          }
          field.setRobotPose(cpose);
    }

    override fun simulationPeriodic() {
        for(i in 0..drivers.size - 1) {
            drivers[i].simulationPeriodic(DriveConstants.simUpdateTime);
        }
        val modStates: Array<SwerveModuleState> = getStates();

        val chassisSpeed: ChassisSpeeds = DriveConstants.kinematics.toChassisSpeeds(modStates[0], modStates[1], modStates[2], modStates[3]);
        val chassisRotationSpeed: Double = chassisSpeed.omegaRadiansPerSecond;

        yaw += chassisRotationSpeed * DriveConstants.simUpdateTime;
        gyroSim.setRawHeading(-Util.radiansToDegrees(yaw));
    }
}
