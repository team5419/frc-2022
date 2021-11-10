package org.team5419.frc2022

import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab
import edu.wpi.first.networktables.NetworkTableInstance
import com.ctre.phoenix.motorcontrol.can.VictorSPX
import com.ctre.phoenix.motorcontrol.can.TalonSRX
import org.team5419.frc2022.fault.math.geometry.Pose2d
import org.team5419.frc2022.fault.hardware.ctre.*
import org.team5419.frc2022.fault.auto.Routine
import com.ctre.phoenix.motorcontrol.*

import edu.wpi.first.wpilibj.TimedRobot

import org.team5419.frc2022.subsystems.*

val tab: ShuffleboardTab = Shuffleboard.getTab("Master")

@SuppressWarnings("TooManyFunctions")

val subsystems = mutableListOf<Subsystem>()

object Robot : TimedRobot(0.02) {

    init {
        NetworkTableInstance.getDefault().setUpdateRate(0.01)
        subsystems.add(Drivetrain)
    }

    override fun robotInit() {
    }

    override fun robotPeriodic() {
    }

    override fun disabledInit() {
    }

    override fun disabledPeriodic() {
    }

    override fun autonomousInit() {
    }

    override fun autonomousPeriodic() {
    }

    override fun teleopInit() {
    }

    override fun teleopPeriodic() {
    }

    override fun testInit() {
    }

    override fun testPeriodic() {
    }
}
