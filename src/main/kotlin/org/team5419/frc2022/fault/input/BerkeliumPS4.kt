@file:Suppress("MatchingDeclarationName")
package org.team5419.frc2022.fault.input

import edu.wpi.first.wpilibj.GenericHID
import edu.wpi.first.wpilibj.Joystick

typealias BerkeleiumPS4Controller = BerkeleiumHID<Joystick>
typealias BerkeleiumPS4Builder = BerkeleiumHIDBuilder<Joystick>

fun ps4Controller(
    port: Int,
    block: BerkeleiumPS4Builder.() -> Unit
) = Joystick(port).mapControls(block)

fun BerkeleiumPS4Builder.button(
    button: PS4Button,
    block: HIDButtonBuilder.() -> Unit = {}
) = button(button.id, block)

fun BerkeleiumPS4Builder.triggerAxisButton(
    hand: GenericHID.Hand,
    threshold: Double = HIDButton.kDefaultThreshold,
    block: HIDButtonBuilder.() -> Unit = {}
) = axisButton(yTriggerAxisToRawAxis(hand), threshold, block)

fun BerkeleiumPS4Controller.getY(hand: GenericHID.Hand) = getRawAxis(yAxisToRawAxis(hand))
fun BerkeleiumPS4Controller.getX(hand: GenericHID.Hand) = getRawAxis(xAxisToRawAxis(hand))
fun BerkeleiumPS4Controller.getRawButton(button: PS4Button) = getRawButton(button.id)

private fun yAxisToRawAxis(hand: GenericHID.Hand) = if (hand == GenericHID.Hand.kLeft) 1 else 5
private fun xAxisToRawAxis(hand: GenericHID.Hand) = if (hand == GenericHID.Hand.kLeft) 0 else 2
private fun yTriggerAxisToRawAxis(hand: GenericHID.Hand) = if (hand == GenericHID.Hand.kLeft) 2 else 3 // check this

enum class PS4Button(val id: Int) {
    Square(1),
    X(2),
    Circle(3),
    Triangle(4),
    LeftBumper(5),
    RightBumper(6),
    LeftTrigger(7),
    RightTrigger(8),
    Share(9),
    Options(10),
    LeftStick(11),
    RightStick(12),
    Playstation(13),
    Touchpad(14)
}
