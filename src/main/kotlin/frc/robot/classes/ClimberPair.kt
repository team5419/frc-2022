package frc.robot.classes
import com.ctre.phoenix.motorcontrol.can.TalonFX
import com.ctre.phoenix.motorcontrol.can.VictorSPX


open class ClimberPair {
    val left: TalonFX
    val right: TalonFX
    val invertedLeft : Boolean
    val invertedRight : Boolean
    constructor(_left: TalonFX, _invertedLeft : Boolean = false, _right: TalonFX, _invertedRight : Boolean = false) {
        this.left = _left;
        this.right = _right;
        this.invertedLeft = _invertedLeft;
        this.invertedRight = _invertedRight;
    }
}