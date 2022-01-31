package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.ctre.phoenix.motorcontrol.can.TalonFX
import com.ctre.phoenix.motorcontrol.can.TalonSRX
import edu.wpi.first.networktables.NetworkTableEntry
import edu.wpi.first.networktables.EntryListenerFlags
import edu.wpi.first.networktables.EntryNotification
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets
import com.ctre.phoenix.motorcontrol.*
import kotlin.math.*

import frc.robot.ShooterConstants
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab

class Shooter(tab: ShuffleboardTab) : SubsystemBase() {

    // declare motors and ports
    val backMotor = TalonFX(ShooterConstants.Ports.back)
    val frontMotor = TalonFX(ShooterConstants.Ports.front)

    public var defaultVelocity: Double = 0.0
    var frontToBackRatio: Double = 1.25
    public var setpoint = 0.0

    // configure the motors and add to shuffleboard
    init {
        backMotor.apply {
            configFactoryDefault(100)

            setNeutralMode(NeutralMode.Coast)
            setSensorPhase(false)
            setInverted(false)

            //configSupplyCurrentLimit(SupplyCurrentLimitConfiguration(true, 40.0, 0.0, 0.0), 100)

            // bang bang PID
            config_kP(0, 10000.0, 100)
            config_kI(0, 0.0, 100)
            config_kD(0, 0.0, 100)
            config_kF(0, 0.0, 100)

            // velocity controlle PID
            config_kP(1, 0.5, 100)
            config_kI(1, 0.0, 100)
            config_kD(1, 0.0, 100)
            config_kF(1, 0.06, 100)

            // we want to use velocity controlle
            selectProfileSlot(1, 0)

            setSelectedSensorPosition(0.0, 0, 100)
            configClosedloopRamp(1.0, 100)

            configClosedLoopPeriod(0, 1, 100)

            configPeakOutputForward(1.0, 100)
            configPeakOutputReverse(0.0, 100)
        }

        frontMotor.apply {
            configFactoryDefault(100)

            setNeutralMode(NeutralMode.Coast)
            setSensorPhase(false)
            setInverted(true)

            //configSupplyCurrentLimit(SupplyCurrentLimitConfiguration(true, 40.0, 0.0, 0.0), 100)

            // bang bang PID
            config_kP(0, 10000.0, 100)
            config_kI(0, 0.0, 100)
            config_kD(0, 0.0, 100)
            config_kF(0, 0.0, 100)

            // velocity controlle PID
            config_kP(1, 0.5, 100)
            config_kI(1, 0.0, 100)
            config_kD(1, 0.0, 100)
            config_kF(1, 0.06, 100)

            // we want to use velocity controlle
            selectProfileSlot(1, 0)

            setSelectedSensorPosition(0.0, 0, 100)
            configClosedloopRamp(1.0, 100)

            configClosedLoopPeriod(0, 1, 100)

            configPeakOutputForward(1.0, 100)
            configPeakOutputReverse(0.0, 100)
        }

        tab.addNumber("Attempted Velocity", { setpoint })
        tab.addNumber("Real Velocity", { flyWheelVelocity() })
        tab.add("Default shooter velocity", 0.0)
            .withWidget(BuiltInWidgets.kNumberSlider)
            .withProperties(mapOf("min" to 0, "max" to 22000))
            .getEntry()
            .addListener({ value: EntryNotification -> this.defaultVelocity = value.value.getDouble() }, EntryListenerFlags.kUpdate)
        tab.add("Front to back ratio", 1.25)
            .withWidget(BuiltInWidgets.kNumberSlider)
            .withProperties(mapOf("min" to 0, "max" to 5))
            .getEntry()
            .addListener({ value: EntryNotification -> this.frontToBackRatio = value.value.getDouble() }, EntryListenerFlags.kUpdate)
    }

    // get velocity of flywheel
    public fun flyWheelVelocity(): Double {
        return frontMotor.getSelectedSensorVelocity(0)
    }

    public fun onStart() {
        /*val shuffleList = tab.getComponents()
        val velocityIndex = shuffleList.filter(
            ()
        )*/
    }

    //check if flywheel velocity is at target
    public fun isSpedUp(): Boolean {
        return setpoint != 0.0 && flyWheelVelocity() >= setpoint
    }

    public fun stop() {
        setpoint = 0.0
        frontMotor.set(ControlMode.PercentOutput, 0.0)
        backMotor.set(ControlMode.PercentOutput, 0.0)
    }

    public fun shoot(velocity: Double) {
        // set setpoint to velocity
        if(velocity != setpoint) {
            setpoint = if (velocity == -1.0) this.defaultVelocity else velocity
        }
        println("Setting Velocity: ${setpoint}")
        // spin flywheel at selected velocity
        frontMotor.set(ControlMode.Velocity, setpoint)
        backMotor.set(ControlMode.Velocity, setpoint * this.frontToBackRatio)
    }

    override fun periodic() {
    }

    override fun simulationPeriodic() {
    }
}