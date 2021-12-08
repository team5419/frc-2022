package org.team5419.frc2022.controllers

import kotlin.math.*

import org.team5419.frc2022.input.*
import org.team5419.frc2022.subsystems.*

object TeleopController : Controller() {
    override fun start() {

    }

    override fun update() {
        this.updateDrivetrain()
    }

    override fun reset() {

    }

    private fun updateDrivetrain() {
        println(
            "throttle: ${DriverControls.getThrottle()}.
            turn: ${DriverControls.getTurn()}. slow: ${DriverControls.slowMove()}."
        );
        Drivetrain.drive(DriverControls.getThrottle(), DriverControls.getTurn(), DriverControls.slowMove())
    }
}
