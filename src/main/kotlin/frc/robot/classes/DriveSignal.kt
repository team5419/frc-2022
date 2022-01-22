package frc.robot.classes

open class DriveSignal {
    val left: Double
    val right: Double
    constructor(_left: Double, _right: Double) {
        this.left = _left;
        this.right = _right;
    }
}