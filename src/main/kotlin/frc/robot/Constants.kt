package frc.robot;
import kotlin.math.PI

object DriveConstants {
    object Ports {
        val leftLeader = 2; //2
        val leftFollower = 3; //3
        val rightLeader = 4; //4
        val rightFollower = 5; // 1
        val gyroPort = 1
    }
    
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
        val front = 6;
        val back = 7;
    }
    // 10587 velocity, 1.64 ratio is good
}

object FeederConstants {
    object Ports {
        val motor = 8;
    }
    val outputPercent: Double = 1.0; // x / 1
}

object PrototypeMotorConstants {
    object Ports {
        val motor = 20;
    }
}

object VisionConstants {

    public val cameraAngle = 10.0 // degrees
    public val cameraHeight = 0.23495 // meters
    public val targetHeight = 0.5 // meters
    public val tolerance = 3.0 // arbitrary
    public val maxAutoAlignSpeed = 0.15
    public val targetOffset = 0.0
    public const val maxOffsetFor2XZoom = 10.0

    object PID {
        public const val P = 0.01
        public const val I = 0.006
        public const val D = 0.0005
    }

    object PID2 {
        public const val P = 0.015
        public const val I = 0.003
        public const val D = 0.0005
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
