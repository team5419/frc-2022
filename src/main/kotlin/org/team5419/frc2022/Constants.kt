package org.team5419.frc2022

object DriveConstants {
    object Ramsete {
        const val kv: Double = 2.3 // arbitrary
        const val ka: Double = 0.463
        const val ks: Double = 0.191
        const val maxVelocity: Double = 3.0 // all in meters v
        const val maxAcceleration: Double = 1.5
        const val maxCentripetalAcceleration: Double = 3.0
        const val maxVoltage: Double = 12.0 // volts
        const val beta: Double = 1.8 // m^-2
        const val zeta: Double = 0.7 // unitless
        const val trackWidth: Double = 1.781 // meters
    }
    const val driverPort: Int = 0
    const val slowMultiplier: Double = 0.5
    object Left {
        const val leaderPort: Int = 1
        const val followerPort: Int = 2
    }
    object Right {
        const val leaderPort: Int = 4
        const val followerPort: Int = 3
    }
    object PID {
        const val P: Double = 0.0
        const val I: Double = 0.0
        const val D: Double = 0.0
    }
}

object Empty {
    // detekt wrapper be nice to us please
}
