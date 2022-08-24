package frc.robot;
import kotlin.math.PI
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.controller.PIDController;

object DriveConstants {
    val simUpdateTime: Double = 0.02;
    val turnerPorts: Array<Int> = arrayOf(0, 1, 2, 3)
    val driverPorts: Array<Int> = arrayOf(4, 5, 6, 7)
    val cancoderPorts: Array<Int> = arrayOf(8, 9, 10, 11)
    val gyroPort: Int = 16;
    val autoCheckVelocities: Array<Double> = arrayOf(1000.0, 1000.0, 1000.0, 1000.0)
    val gearRatio: Double = (10.3333 / 1.0)
    val ticksPerRotation: Double = (2048.0 * gearRatio)
    const val wheelRadius: Double = 0.0508
    const val wheelDiameter: Double = wheelRadius * 2.0
    const val wheelCircumference: Double = wheelDiameter * PI
    const val controllerDeadband: Double = 0.05;

    // Locations for the swerve drive modules relative to the robot center
    val frontLeftLocation: Translation2d = Translation2d(0.381, 0.381);
    val frontRightLocation: Translation2d = Translation2d(0.381, -0.381);
    val backLeftLocation: Translation2d = Translation2d(-0.381, 0.381);
    val backRightLocation: Translation2d = Translation2d(-0.381, -0.381);

    val modulePositions: Array<Translation2d> = arrayOf(frontLeftLocation, frontRightLocation, backLeftLocation, backRightLocation);

    // Creating my kinematics object using the module locations
    val kinematics: SwerveDriveKinematics = SwerveDriveKinematics(
        frontLeftLocation, frontRightLocation, backLeftLocation, backRightLocation
    );
    val driveMotorGearRatio: Double = 6.75;
    val turnMotorGearRatio: Double = 12.8;
    val driveMotorGearbox: DCMotor = DCMotor.getFalcon500(1);
    val turnMotorGearbox: DCMotor = DCMotor.getNeo550(1);
    
    val kvRadians: Double = 0.16;
    val kaRadians: Double = 0.0348;
    object Ramsete {
        const val kpx: Double = 0.0;
        const val kpy: Double = 0.0;
        const val kptheta: Double = 0.0;
        const val kMaxAngularSpeedRadiansPerSecond: Double = 10.0 * Math.PI ;
        val kMaxAngularSpeedRadiansPerSecondSquared: Double = 10.0 * Math.PI;
        public val kThetaControllerConstraints: TrapezoidProfile.Constraints =
                TrapezoidProfile.Constraints(kMaxAngularSpeedRadiansPerSecond, kMaxAngularSpeedRadiansPerSecondSquared);
        const val kv: Double = 2.3 // arbitrary
        const val ka: Double = 0.463
        const val ks: Double = 0.191
        const val maxVelocity: Double = 3.0 // all in m/?
        const val maxAcceleration: Double = 1.5
    }
    const val speedMultiplier: Double = 3.0;
    val feedForward: SimpleMotorFeedforward = SimpleMotorFeedforward(Ramsete.ks, Ramsete.kv, Ramsete.ka);
    const val driverPort: Int = 0
    const val slowMultiplier: Double = 0.25
    object Modules {
        object DrivePID {
            const val P: Double = 2.3
            const val I: Double = 0.0
            const val D: Double = 0.0
        }
        object TurnPID {
            const val P: Double = 2.4
            const val I: Double = 0.0
            const val D: Double = 0.0
        }
        public val kMaxModuleAngularSpeedRadiansPerSecond: Double = 2 * Math.PI;
        public val kMaxModuleAngularAccelerationRadiansPerSecondSquared: Double = 2 * Math.PI;
        public val encoderCPR: Double = 2048.0;
        public val wheelDiameter: Double = 10.16; //cm 
        public val kDriveEncoderDistancePerPulse: Double =
                (wheelRadius * Math.PI) / (encoderCPR * driveMotorGearRatio);

        public val kTurningEncoderDistancePerPulse: Double =
                (2.0 * Math.PI) / (encoderCPR * turnMotorGearRatio);
        public val driveController: PIDController = PIDController(DrivePID.P, DrivePID.I, DrivePID.D);
        public val turnController: ProfiledPIDController = ProfiledPIDController(TurnPID.P, TurnPID.I, TurnPID.D, TrapezoidProfile.Constraints(
            kMaxModuleAngularSpeedRadiansPerSecond, kMaxModuleAngularAccelerationRadiansPerSecondSquared
        ));
    }
}

object ShooterConstants {
    object Ports {
        val main = 8;
        val kicker = 7;
    }
    val mainVelocity = 15000.0
    val kickerVelocity = 15000.0
}

object IndexerConstants {
    object Ports {
        val motor = 22;
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
    object Ports {
        val motor = 16;
    }
    val idlePercent = 0.2;
    val activePercent = 0.9;
    val reversePercent: Double = -0.9;
    val autoSpeed: Double = 1000.0;
}

object IntakeConstants {
    object Ports {
        val motor = 6;
        val deployMotor = 15;
    }
    val outputPercent = 1.0;
    val reversePercent = -outputPercent;
    val autoSpeed: Double = 1000.0;
}

object PrototypeMotorConstants {
    object Ports {
        val motor = 20;
    }
}

object VisionConstants {

    public val cameraAngle = 17.0 // degrees
    public val cameraHeight = 0.254 // meters
    public val targetHeight = 1.5 // meters
    public val turnTolerance = 3.0 // 3.0 // arbitrary
    public val throttleTolerance = 3.0 // arbitrary
    public val maxAutoAlignSpeed = 0.15
    public val targetOffset = 0.0
    public const val maxOffsetFor2XZoom = 10.0

    object TurnPID {
        public const val P = 0.01 // .02
        public const val I = 0.006 // .006
        public const val D = 0.0005 // .0005
    }

    object ThrottlePID {
        public const val P = 0.5
        public const val I = 0.006
        public const val D = 0.0010
    }
}

object ClimberConstants {
    object Ports {
        val left1 = 9
        val right1 = 10
        val left2 = 11
        val right2 = 12
        val lsensor0 = 0
        val lsensor1 = 1
        val rsensor0 = 2
        val rsensor1 = 3
        //val left3 = 13
        //val right3 = 14
    }
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

object LightsConstants {
    object Ports {
        val lights1 = 25;
    }
}
