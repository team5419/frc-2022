// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;
import kotlin.math.PI

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
object DriveConstants {
    object Ports {
        val leftLeader = 3;
        val leftFollower = 4;
        val rightLeader = 1;
        val rightFollower = 2;
        val gyroPort = 20

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
    object PID {
        const val P: Double = 0.0
        const val I: Double = 0.0
        const val D: Double = 0.0
    }
}

