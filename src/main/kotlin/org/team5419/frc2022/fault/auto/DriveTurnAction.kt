package org.team5419.frc2022.fault.auto

import org.team5419.frc2022.fault.math.geometry.Rotation2d
import org.team5419.frc2022.fault.math.units.SIUnit
import org.team5419.frc2022.fault.math.units.derived.Radian
import org.team5419.frc2022.fault.math.units.derived.degrees
import org.team5419.frc2022.fault.math.units.derived.velocity
import org.team5419.frc2022.fault.math.units.derived.AngularVelocity

import org.team5419.frc2022.fault.subsystems.drivetrain.AbstractTankDrive

// ALLOW for absolute turn in future (using rotation2d math maybe)
class DriveTurnAction(
    private val drivetrain: AbstractTankDrive,
    private val rotation: Rotation2d,
    private val turnType: AbstractTankDrive.TurnType = AbstractTankDrive.TurnType.Relative,
    private val acceptableTurnError: SIUnit<Radian> = 3.degrees, // degrees
    private val acceptableVelocityError: SIUnit<AngularVelocity> = 5.0.degrees.velocity // inches / s
) : Action() {

    private val turnErrorAcceptable = { drivetrain.turnError < acceptableTurnError }
    private val angularVelocityAcceptable = { drivetrain.angularVelocity < acceptableVelocityError }

    override fun start() {
        super.start()
        drivetrain.setTurn(rotation, turnType)

        finishCondition += { turnErrorAcceptable() }
        finishCondition += { angularVelocityAcceptable() }
    }

    override fun finish() {
        super.finish()
        drivetrain.zeroOutputs()
    }
}
