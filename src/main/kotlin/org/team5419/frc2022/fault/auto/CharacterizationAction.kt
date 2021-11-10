package org.team5419.frc2022.auto

import org.team5419.frc2022.fault.math.units.inMeters
import edu.wpi.first.wpilibj.Timer
import edu.wpi.first.wpilibj.RobotController
import edu.wpi.first.networktables.NetworkTableInstance
import org.team5419.frc2022.fault.subsystems.drivetrain.AbstractTankDrive
import org.team5419.frc2022.fault.auto.Action
import org.team5419.frc2022.fault.math.units.Second
import org.team5419.frc2022.fault.math.units.SIUnit

public class CharacterizationAction(val drivetrain: AbstractTankDrive) : Action() {

    // connect to the characterization python app so we can send and get data from it
    val autoSpeedEntry = NetworkTableInstance.getDefault().getEntry("/robot/autospeed")
    val telemetryEntry = NetworkTableInstance.getDefault().getEntry("/robot/telemetry")

    var priorAutospeed = 0.0

    override fun start() {
    }

    override fun update(dt: SIUnit<Second>) {
        // get current time
        val now = Timer.getFPGATimestamp()

        // what volatage is the battery at?
        val battery = RobotController.getBatteryVoltage()

        // get the target speed from the app
        val autospeed = autoSpeedEntry.getDouble(0.0)
        drivetrain.setPercent(autospeed, autospeed)

        // calculate how much voltage we told the motors to last frame
        val leftMotorVolts = battery * Math.abs(priorAutospeed)
        val rightMotorVolts = battery * Math.abs(priorAutospeed)

        // send the data to the controlle panel
        telemetryEntry.setNumberArray(arrayOf<Double>(
            now,
            battery,
            autospeed,

            leftMotorVolts,
            rightMotorVolts,

            // curent position in meters of each side
            drivetrain.leftDistance.inMeters(),
            drivetrain.rightDistance.inMeters(),

            // curent velocity of each side in meters per second
            drivetrain.leftDistance.inMeters(),
            drivetrain.rightDistance.inMeters()
        ))

        // save the auto speed
        priorAutospeed = autospeed
    }
}
