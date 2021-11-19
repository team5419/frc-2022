package org.team5419.frc2022

object DriveConstants {
    const val driverPort: Int = 0
    const val slowMultiplier: Double = 0.5
    object Left {
        const val leaderPort: Int = 1
        const val followerPort: Int = 2
    }
    object Right {
        const val leaderPort: Int = 3
        const val followerPort: Int = 4
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
