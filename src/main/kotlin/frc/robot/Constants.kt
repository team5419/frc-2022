package frc.robot;
import kotlin.math.PI

object DriveConstants {
    object Ports {
        val leftLeader = 3; 
        val leftFollower = 4; 
        val rightLeader = 1; 
        val rightFollower = 2; 
    }
    const val slowMultiplier: Double = 0.25

    object PID {
        const val P: Double = 0.0
        const val I: Double = 0.0
        const val D: Double = 0.0
    }
}