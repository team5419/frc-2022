package frc.robot.classes
import com.ctre.phoenix.motorcontrol.can.TalonFX
import com.ctre.phoenix.motorcontrol.can.VictorSPX
import edu.wpi.first.wpilibj.AnalogInput

open class ClimberSingle {
    val motor: TalonFX
    val inverted: Boolean
    val sensor: AnalogInput
    val min: Double
    val max: Double
    constructor(_motor: TalonFX, _inverted: Boolean, _sensor: AnalogInput, _min: Double, _max: Double) {
        this.motor = _motor;
        this.inverted = _inverted;
        this.sensor = _sensor;
        this.min = _min;
        this.max = _max;
    }
}

open class ClimberPair {
    val left: ClimberSingle
    val right: ClimberSingle
    var previousThrottle : Double
    var previousTurn : Double
    constructor(_left: ClimberSingle, _right: ClimberSingle) {
        this.left = _left;
        this.right = _right;
        this.previousThrottle = -2.0;
        this.previousTurn = -2.0;
    }
}