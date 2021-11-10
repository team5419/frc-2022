package org.team5419.frc2022.fault.auto

import org.team5419.frc2022.fault.math.units.Meter
import org.team5419.frc2022.fault.math.units.SIUnit
import org.team5419.frc2022.fault.math.units.derived.LinearVelocity
import org.team5419.frc2022.fault.math.units.derived.velocity
import org.team5419.frc2022.fault.math.units.inches
import org.team5419.frc2022.fault.subsystems.drivetrain.AbstractTankDrive
import kotlin.math.absoluteValue

class DriveStraightAction(
    private val drivetrain: AbstractTankDrive,
    private val distance: SIUnit<Meter>,
    private val acceptableDistanceError: SIUnit<Meter> = 2.inches, // inches
    private val acceptableVelocityError: SIUnit<LinearVelocity> = 2.inches.velocity // inches / s
) : Action() {

    private val distanceErrorAcceptable = {
            drivetrain.leftDistanceError.absoluteValue < acceptableDistanceError &&
                    drivetrain.rightDistanceError.absoluteValue < acceptableDistanceError
    }

    private val velocityErrorAcceptable = {
        drivetrain.leftMasterMotor.encoder.velocity.absoluteValue < acceptableVelocityError.value &&
                drivetrain.rightMasterMotor.encoder.velocity.absoluteValue < acceptableVelocityError.value
    }

    init {
        finishCondition += distanceErrorAcceptable
        finishCondition += velocityErrorAcceptable
    }

    override fun start() {
        super.start()
        drivetrain.setPosition(distance)
    }

    override fun finish() = drivetrain.zeroOutputs()
}
