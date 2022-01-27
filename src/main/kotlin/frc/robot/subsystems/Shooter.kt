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
    val leaderMotor = TalonFX(ShooterConstants.Ports.leader)
    val followerMotor = TalonFX(ShooterConstants.Ports.follower)

    var defaultVelocity: Double = 0.0
    public var setpoint = 0.0

    // configure the motors and add to shuffleboard
    init {
        leaderMotor.apply {
            configFactoryDefault(100)

            setNeutralMode(NeutralMode.Coast)
            setSensorPhase(false)
            setInverted(true)

            configSupplyCurrentLimit(SupplyCurrentLimitConfiguration(true, 40.0, 0.0, 0.0), 100)

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

        followerMotor.apply {
            follow(leaderMotor)

            setInverted(false)
            setNeutralMode(NeutralMode.Coast)

            configSupplyCurrentLimit(SupplyCurrentLimitConfiguration(true, 40.0, 0.0, 0.0), 100)

            configPeakOutputForward(1.0, 100)
            configPeakOutputReverse(0.0, 100)
        }

        tab.addNumber("Attempted Velocity", { setpoint })
        tab.addNumber("Real Velocity", { flyWheelVelocity() })
        tab.add("B button shooter velocity", 0.0)
            .withWidget(BuiltInWidgets.kNumberSlider)
            .withProperties(mapOf("min" to 0, "max" to 5000))
            .getEntry()
            .addListener({ value: EntryNotification -> this.defaultVelocity = value.value.getDouble() }, EntryListenerFlags.kUpdate)
    }

    // get velocity of flywheel
    public fun flyWheelVelocity(): Double {
        return leaderMotor.getSelectedSensorVelocity(0)
    }

    //check if flywheel velocity is at target
    public fun isSpedUp(): Boolean {
        return setpoint != 0.0 && flyWheelVelocity() >= setpoint
    }

    public fun stop() {
        setpoint = 0.0
        leaderMotor.set(ControlMode.PercentOutput, 0.0)
    }

    public fun shoot(velocity: Double) {
        // set setpoint to velocity
        if(velocity != setpoint) {
            setpoint = if (velocity == -1.0) this.defaultVelocity else velocity
        }
        println("Setting Velocity: ${setpoint}")
        // spin flywheel at selected velocity
        leaderMotor.set(ControlMode.Velocity, setpoint)
    }

    override fun periodic() {
    }

    override fun simulationPeriodic() {
    }
}