package org.team5419.frc2022.input

import org.team5419.frc2022.input.DriverControls
import edu.wpi.first.wpilibj.XboxController
import edu.wpi.first.wpilibj.GenericHID.Hand

import org.team5419.frc2022.InputConstants

public val driverXbox = XboxController(InputConstants.XboxDrivePort)

@Suppress("TooManyFunctions")
object XboxDriver : DriverControls {
    override public fun getThrottle() = driverXbox.getY( Hand.kLeft )

    override public fun getTurn() = driverXbox.getX( Hand.kRight )

    override public fun slowMove() = driverXbox.getBumper( Hand.kLeft )

    override public fun superSlowMove(): Boolean = (driverXbox.getTriggerAxis(Hand.kLeft) != 0.0)

    override public fun fastTurn() = driverXbox.getBumper( Hand.kRight )

    override public fun invertDrivetrain(): Boolean = driverXbox.getXButtonPressed()

    // alignment functions

    override public fun togleAligning(): Boolean = driverXbox.getAButtonPressed()

    override public fun adjustOffsetLeft(): Double = 0.0 //driverXbox.getTriggerAxis(Hand.kLeft)

    override public fun adjustOffsetRight(): Double = 0.0 //driverXbox.getTriggerAxis(Hand.kRight)

    // hood functions

    override public fun retractHood(): Boolean =  false //driverXbox.getBButton()

    override public fun adjustHoodUp(): Boolean = driverXbox.getBButtonPressed()

    override public fun adjustHoodDown(): Boolean = driverXbox.getXButtonPressed()

    override public fun increaseShoogVelocity(): Boolean = false //driverXbox.getBButtonPressed()

    override public fun decreaseShoogVelocity(): Boolean = false //driverXbox.getXButtonPressed()

    // climb

    override public fun climb(): Boolean = driverXbox.getPOV() == 0

    override public fun unclimb(): Boolean = driverXbox.getPOV() == 180

    override public fun winch(): Boolean = driverXbox.getPOV() == 90

    override public fun unwinch(): Boolean = driverXbox.getPOV() == 270

}
