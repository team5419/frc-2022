package frc.robot;
import kotlin.math.PI

object DriveConstants {
    object Ports {
        val leftLeader = -1; 
        val leftFollower = -1; 
        val rightLeader = -1; 
        val rightFollower = -1; 
    }
    const val slowMultiplier: Double = 0.25

    object PID {
        const val P: Double = 0.0
        const val I: Double = 0.0
        const val D: Double = 0.0
    }
}