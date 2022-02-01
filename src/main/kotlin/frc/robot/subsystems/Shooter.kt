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
    val kickerMotor = TalonFX(ShooterConstants.Ports.kicker)
    val mainMotor = TalonFX(ShooterConstants.Ports.main)

    public var mainVelocity: Double = 0.0
    public var kickerVelocity: Double = 0.0
    public var setpointMain = 0.0
    public var setpointKicker = 0.0

    // configure the motors and add to shuffleboard
    init {
        kickerMotor.apply {
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
            configPeakOutputReverse(-1.0, 100)
        }

        mainMotor.apply {
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
            configPeakOutputReverse(-1.0, 100)
        }

        tab.addNumber("Attempted Velocity", { setpointMain })
        tab.addNumber("Real Velocity", { flyWheelVelocity(mainMotor) })
        tab.add("Main shooter velocity", 0.0)
            .withWidget(BuiltInWidgets.kNumberSlider)
            .withProperties(mapOf("min" to -15000, "max" to 22000))
            .getEntry()
            .addListener({ value: EntryNotification -> this.mainVelocity = value.value.getDouble() }, EntryListenerFlags.kUpdate)
        tab.add("Kicker shooter velocity", 0.0)
            .withWidget(BuiltInWidgets.kNumberSlider)
            .withProperties(mapOf("min" to -15000, "max" to 22000))
            .getEntry()
            .addListener({ value: EntryNotification -> this.kickerVelocity = value.value.getDouble() }, EntryListenerFlags.kUpdate)
    }

    // get velocity of flywheel
    public fun flyWheelVelocity(motor: TalonFX): Double {
        return motor.getSelectedSensorVelocity(0)
    }

    //check if flywheel velocity is at target
    public fun isSpedUp(): Boolean {
        return flyWheelVelocity(mainMotor) >= setpointMain && flyWheelVelocity(kickerMotor) >= setpointKicker && (setpointMain != 0.0 || setpointKicker != 0.0)
    }

    public fun stop() {
        setpointMain = 0.0
        setpointKicker = 0.0
        mainMotor.set(ControlMode.PercentOutput, 0.0)
        kickerMotor.set(ControlMode.PercentOutput, 0.0)
    }

    public fun shoot(main: Double, kicker: Double) {
        // set setpoint to velocity
        if(main != setpointMain) {
            setpointMain = if (main == -1.0) this.mainVelocity else main
        }
        if(kicker != setpointKicker) {
            setpointKicker = if (kicker == -1.0) this.kickerVelocity else kicker
        }
        println("Setting Velocity: ${setpointMain}")
        // spin flywheel at selected velocity
        mainMotor.set(ControlMode.Velocity, setpointMain)
        kickerMotor.set(ControlMode.Velocity, setpointKicker)
    }

    override fun periodic() {
    }

    override fun simulationPeriodic() {
    }
}