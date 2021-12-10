package org.team5419.frc2022

import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab
import edu.wpi.first.networktables.NetworkTableInstance
import com.ctre.phoenix.motorcontrol.can.VictorSPX
import com.ctre.phoenix.motorcontrol.can.TalonSRX
import com.ctre.phoenix.motorcontrol.*
import edu.wpi.first.wpilibj.Timer


import edu.wpi.first.wpilibj.TimedRobot

import org.team5419.frc2022.subsystems.*
import org.team5419.frc2022.controllers.*

val tab: ShuffleboardTab = Shuffleboard.getTab("Master")

val timer = Timer()

@SuppressWarnings("TooManyFunctions")
object Robot : TimedRobot(0.02) {

    private val subsystems = mutableListOf<Subsystem>()

    init {
        this.subsystems.add(Drivetrain)
        NetworkTableInstance.getDefault().setUpdateRate(0.01)
    }

    override fun robotInit() {
        timer.reset()
        timer.start()
    }

    override fun robotPeriodic() {
    }

    override fun disabledInit() {
        reset()
    }

    override fun disabledPeriodic() {
    }

    override fun autonomousInit() {
        reset()
    }

    override fun autonomousPeriodic() {
    }

    override fun teleopInit() {
        reset()
        TeleopController.start()
    }

    override fun teleopPeriodic() {
        TeleopController.update()
    }

    override fun testInit() {
        reset()
    }

    override fun testPeriodic() {
    }

    fun reset() {
        TeleopController.reset()
        for(subsystem in this.subsystems) {
            subsystem.reset()
        }
    }
}
