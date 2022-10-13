package frc.robot;
import kotlin.math.PI
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.controller.PIDController;
import frc.robot.classes.ModuleInfo;
import edu.wpi.first.math.system.plant.LinearSystemId;
import edu.wpi.first.math.numbers.N2;
import edu.wpi.first.math.numbers.N1;
import edu.wpi.first.math.system.LinearSystem;
import edu.wpi.first.math.controller.LinearPlantInversionFeedforward;

// 169.254.207.121:5801

object DriveConstants {
    val driveMotorGearRatio: Double = 6.75;
    val turnMotorGearRatio: Double = 12.8;
    val driveCPR: Double = 2048.0;
    val turnCPR: Double = 4096.0;
    val autoCheckVelocities = arrayOf(1000.0, 1000.0, 1000.0, 1000.0)
    val ticksPerRotation: Double = (driveCPR * driveMotorGearRatio);
    const val wheelRadius = 0.0508; // m
    const val wheelDiameter = wheelRadius * 2.0; // m
    const val wheelCircumference = wheelDiameter * PI; // m
    const val driverPort: Int = 0

    val info: Array<ModuleInfo> = arrayOf(
        ModuleInfo(2, 24, false, true, 8, 3.46219),
        ModuleInfo(5, 1, true, true, 10, 1.28547),
        ModuleInfo(4, 6, false, true, 9, 5.67418),
        ModuleInfo(3, 7, true, true, 11, 1.98190)
    )

    // ------------------- Swerve Constants -------------------
    val simUpdateTime: Double = 0.02;
    const val controllerDeadband: Double = 0.1;

    // Locations for the swerve drive modules relative to the robot center
    val frontLeftLocation: Translation2d = Translation2d(0.2794, 0.2794);
    val frontRightLocation: Translation2d = Translation2d(0.2794, -0.2794);
    val backLeftLocation: Translation2d = Translation2d(-0.2794, 0.2794);
    val backRightLocation: Translation2d = Translation2d(-0.2794, -0.2794);

    val modulePositions: Array<Translation2d> = arrayOf(frontLeftLocation, frontRightLocation, backLeftLocation, backRightLocation);

    // Creating my kinematics object using the module locations
    val kinematics: SwerveDriveKinematics = SwerveDriveKinematics(
        frontLeftLocation, frontRightLocation, backLeftLocation, backRightLocation
    );
    
    val driveMotorGearbox: DCMotor = DCMotor.getFalcon500(1);
    val turnMotorGearbox: DCMotor = DCMotor.getNeo550(1);
    
    val kvRadians: Double = 0.16;
    val kaRadians: Double = 0.0348;
    object SwerveRamsete { //Swerve
        const val kpx: Double = 0.0;
        const val kpy: Double = 0.0;
        const val kptheta: Double = 0.0;
        const val kMaxAngularSpeedRadiansPerSecond: Double = 10.0 * Math.PI;
        val kMaxAngularSpeedRadiansPerSecondSquared: Double = 10.0 * Math.PI;
        public val kThetaControllerConstraints: TrapezoidProfile.Constraints =
                TrapezoidProfile.Constraints(kMaxAngularSpeedRadiansPerSecond, kMaxAngularSpeedRadiansPerSecondSquared);
        const val kv: Double = (2.1737 / 12)
        const val ka: Double = (0.29281 / 12)
        const val ks: Double = (0.63566 / 12)
        const val maxVelocity: Double = 4.0 //8.0 all in m/?
        const val maxAcceleration: Double = 1.5 // 5.0
    }
    const val speedMultiplier: Double = 4.0//8.0;
    const val turnMultiplier: Double =2.5// 8.0;
    const val slowMultiplier: Double = 0.2;
    val feedForward: SimpleMotorFeedforward = SimpleMotorFeedforward(SwerveRamsete.ks, SwerveRamsete.kv, SwerveRamsete.ka);
    //val turnSystem: LinearSystem<N2, N1, N1> = LinearSystemId.identifyPositionSystem(kvRadians, kaRadians);
    //val turnFeedForward: LinearPlantInversionFeedforward<N2, N1, N1> = LinearPlantInversionFeedforward<N2, N1, N1>(turnSystem, simUpdateTime);
    object Modules {
        object DrivePID {
            const val P: Double = 0.1
            const val I: Double = 0.0
            const val D: Double = 0.0
        }
        object TurnPID {
            const val P: Double = 5.0
            const val I: Double = 0.0
            const val D: Double = 0.0
        }
        public val kMaxModuleAngularSpeedRadiansPerSecond: Double = 2 * Math.PI;
        public val kDriveEncoderDistancePerPulse: Double =
                (wheelDiameter * Math.PI) / ticksPerRotation;
        public val kTurningEncoderDistancePerPulse: Double =
                (2.0 * Math.PI) / (turnCPR * turnMotorGearRatio);
        public val driveController: PIDController = PIDController(DrivePID.P, DrivePID.I, DrivePID.D);
        public val turnController: PIDController = PIDController(TurnPID.P, TurnPID.I, TurnPID.D);
    }
}

object Ports {
    val shooterMain: Int = 19;
    val shooterKicker: Int = 20;
    val indexer: Int = 22;
    val feeder: Int = 18; 
    val intake: Int = 17;
    val intakeDeploy: Int = 23;
    val climberLeft1: Int = 12;
    val climberLeft2: Int = 14;
    // val climberRight1: Int = 13;
    // val climberRight2: Int = 15;
    val lights: Int = 21;
    val gyro: Int = 16;
}

object ShooterConstants {
    val mainVelocity = 15000.0
    val kickerVelocity = 15000.0
}

object IndexerConstants {
    object Sensors {
        val sensor1 = 2;
        val sensor2 = 1;
        val sensor3 = 0;
    }
    val outputPercent: Double = 1.0; // x / 1
    val ticksPerIndex: Double = 8.0;
    val ticksDefault: Double = 5.0;
    val autoSpeed: Double = 1000.0;
}

object FeederConstants {
    val idlePercent = 0.2;
    val activePercent = 0.9;
    val reversePercent: Double = -0.9;
    val autoSpeed: Double = 1000.0;
}

object IntakeConstants {
    val outputPercent = 1.0;
    val reversePercent = -outputPercent;
    val autoSpeed: Double = 1000.0;
}

object VisionConstants {

    public val cameraAngle = 17.0 // degrees
    public val cameraHeight = 0.254 // meters
    public val targetHeight = 1.5 // meters
    public val turnTolerance = 0.8 // 3.0 // arbitrary
    public val throttleTolerance = 3.0 // arbitrary
    public val maxAutoAlignSpeed = 0.15
    public val targetOffset = 0.0
    public const val maxOffsetFor2XZoom = 10.0

    object TurnPID {
        public const val P = 0.1 // .01
        public const val I = 0.006 // .006
        public const val D = 0.0005 // .0005
    }

    object ThrottlePID {
        public const val P = 0.1 // 0.5
        public const val I = 0.006
        public const val D = 0.0010
    }
}

object ClimberConstants {
    
    val autoCheckVelocities = arrayOf(1000.0, 1000.0, 1000.0, 1000.0)
    object Pair0 {
        object Left {
            val min: Double = 0.0 // the hard stop for how low the climber can go (should be zero as long as the climbers are down when you deploy the code)
            val max: Double = 10.0 // the hard stop for how high the climber can go
        }
        object Right { // you can adjust these values individually for each climber
            val min: Double = 0.0
            val max: Double = 10.0
        }
    }
    object Pair1 {
        object Left {
            val min: Double = 0.0
            val max: Double = 10.0
        }
        object Right {
            val min: Double = 0.0
            val max: Double = 10.0
        }
    }
    // adjust these when using the setPairSlightAdjust function
    val adjustmentDeadband: Double = 1.0 // increasing this may prevent climbers from jerking back and forth
    // after you find min and max values, i would suggest setting this to roughly (max - min) / 50
    val adjustment: Double = -2000.0 // this represents how much the sensors will affect the climbers when adjusting
    // i'm pretty sure the climbers go up when the value is negative, so this value is also negative

    object PID {
        val P: Double = 0.5
        val I: Double = 0.0
        val D: Double = 0.0
        val F: Double = 0.06
    }
}