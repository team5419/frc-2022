package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.ctre.phoenix.motorcontrol.can.TalonFX
import com.ctre.phoenix.motorcontrol.can.TalonSRX
import com.ctre.phoenix.motorcontrol.*
import kotlin.math.*

import frc.robot.PrototypeMotorConstants
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab

class PrototypeMotor(tab: ShuffleboardTab) : SubsystemBase() {

    // declare motor and port
    val motor = TalonFX(PrototypeMotorConstants.Ports.motor)

    // configure the motor
    init {
        motor.apply {
            configFactoryDefault(100)
            setNeutralMode(NeutralMode.Coast)
            setSensorPhase(false)
            setInverted(true)
            configSupplyCurrentLimit(SupplyCurrentLimitConfiguration(true, 40.0, 0.0, 0.0), 100)

            config_kP(0, 10000.0, 100)
            config_kI(0, 0.0, 100)
            config_kD(0, 0.0, 100)
            config_kF(0, 0.0, 100)

            // velocity control PID
            config_kP(1, 0.5, 100)
            config_kI(1, 0.0, 100)
            config_kD(1, 0.0, 100)
            config_kF(1, 0.06, 100)

            // we want to use velocity control
            selectProfileSlot(1, 0)

            setSelectedSensorPosition(0.0, 0, 100)
            configClosedloopRamp(1.0, 100)

            configClosedLoopPeriod(0, 1, 100)

            configPeakOutputForward(1.0, 100)
            configPeakOutputReverse(0.0, 100)
        }
    }

    public var setpoint = 0.0

    // get velocity of motor
    public fun flyWheelVelocity(): Double {
        return motor.getSelectedSensorVelocity(0)
    }

    // check if motor velocity is at target
    public fun isSpedUp(): Boolean {
        return setpoint != 0.0 && flyWheelVelocity() >= setpoint
    }

    public fun stop() {
        setpoint = 0.0
        motor.set(ControlMode.PercentOutput, 0.0)
    }

    // run motor
    public fun shoot(velocity: Double) {
        if(velocity > 0 && velocity != setpoint) setpoint = velocity
        motor.set(ControlMode.Velocity, setpoint)
    }

    override fun periodic() {
    }

    override fun simulationPeriodic() {
    }
}