package frc.robot;
import kotlin.math.PI

object DriveConstants {
    object Ports {
        val leftLeader = 3; //2
        val leftFollower = 4; //3
        val rightLeader = 1; //4
        val rightFollower = 2; // 1
        val gyroPort = 20;
    }

    val autoCheckVelocities = arrayOf(1000.0, 1000.0, 1000.0, 1000.0)
    val gearRatio: Double = (10.3333 / 1.0)
    val ticksPerRotation: Double = (2048.0 * gearRatio)
    const val wheelRadius = 0.0762
    const val wheelDiameter = wheelRadius * 2.0
    const val wheelCircumference = wheelRadius * PI

    object Ramsete {
        const val kv: Double = 2.3 // arbitrary
        const val ka: Double = 0.463
        const val ks: Double = 0.191
        const val maxVelocity: Double = 3.0 // all in m/?
        const val maxAcceleration: Double = 1.5
        const val maxCentripetalAcceleration: Double = 3.0
        const val maxVoltage: Double = 12.0 // volts
        const val beta: Double = 1.8 // m^-2
        const val zeta: Double = 0.7 // unitless
        const val trackWidth: Double = 1.781 // meters
    }

    const val driverPort: Int = 0
    const val slowMultiplier: Double = 0.25

    object PID {
        const val P: Double = 0.0
        const val I: Double = 0.0
        const val D: Double = 0.0
    }
}

object ShooterConstants {
    object Ports {
        val main = 6;
        val kicker = 7;
    }
    val mainVelocity = 10587
    val kickerVelocity = 17362
    // 10587 velocity, 1.64 ratio is good
}

object IndexerConstants {
    object Ports {
        val motor = 8;
    }
    val outputPercent: Double = 1.0; // x / 1
    val ticksPerIndex: Double = 1.0;
}

object FeederConstants {
    object Ports {
        val motor = 15;
    }
    val idlePercent = 0.2;
    val activePercent = 0.9;
}

object IntakeConstants {
    object Ports {
        val motor = 16;
    }
    val outputPercent = 0.9;
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
    public val turnTolerance = 3.0 // arbitrary
    public val throttleTolerance = 3.0 // arbitrary
    public val maxAutoAlignSpeed = 0.15
    public val targetOffset = 0.0
    public const val maxOffsetFor2XZoom = 10.0

    object TurnPID {
        public const val P = 0.01
        public const val I = 0.006
        public const val D = 0.0005
    }

    object ThrottlePID {
        public const val P = 0.5
        public const val I = 0.0
        public const val D = 0.0
    }
}

object ClimberConstants {
    object Ports {
        val left1 = 9
        val right1 = 10
        val left2 = 11
        val right2 = 12
        val left3 = 13
        val right3 = 14
    }
}
