package frc.robot;
import kotlin.math.PI

object DriveConstants {
    object Ports {
        val leftLeader = 2; // 3
        val leftFollower = 3; //4
        val rightLeader = 4; //1
        val rightFollower = 5; // 2
        val gyroPort = 20;
    }

    val autoCheckVelocities = arrayOf(1000.0, 1000.0, 1000.0, 1000.0)
    val gearRatio: Double = (10.3333 / 1.0)
    val ticksPerRotation: Double = (2048.0 * gearRatio)
    const val wheelRadius = 0.0508
    const val wheelDiameter = wheelRadius * 2.0
    const val wheelCircumference = wheelDiameter * PI

    object Ramsete {
        const val kv: Double = 2.3 // arbitrary
        const val ka: Double = 0.463
        const val ks: Double = 0.191
        const val maxVelocity: Double = 3.0 // all in m/?
        const val maxAcceleration: Double = 1.5
        const val maxCentripetalAcceleration: Double = 3.0
        const val maxVoltage: Double = 12.0 // volts
        const val beta: Double = 2.0 // m^-2
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
    public val turnTolerance = 3.0 // arbitrary
    public val throttleTolerance = 3.0 // arbitrary
    public val maxAutoAlignSpeed = 0.15
    public val targetOffset = 0.0
    public const val maxOffsetFor2XZoom = 10.0

    object TurnPID {
        public const val P = 0.02
        public const val I = 0.006
        public const val D = 0.0005
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
        val lsensor0 = 25
        val lsensor1 = 26
        val rsensor0 = 27
        val rsensor1 = 28
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
}

object LightsConstants {
    object Ports {
        val lights1 = 25;
    }
}
