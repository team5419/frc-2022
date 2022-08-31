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

import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout
import frc.robot.classes.RGB;

class Shooter(tab: ShuffleboardTab) : SubsystemBase() {

    // declare motors and ports
    public val kickerMotor = TalonFX(ShooterConstants.Ports.kicker)
    public val mainMotor = TalonFX(ShooterConstants.Ports.main)

    public var mainVelocity: Double = ShooterConstants.mainVelocity
    public var kickerVelocity: Double = ShooterConstants.kickerVelocity
    public var currentColor: RGB = RGB(0, 0, 255);
    public var setpointMain = 0.0
    public var setpointKicker = 0.0
    public var shooterMultiplier : Double = 1.0

    private val layout: ShuffleboardLayout = tab.getLayout("Shooter", BuiltInLayouts.kList).withPosition(6, 0).withSize(2, 4);

    // configure the motors and add to shuffleboard
    init {
        kickerMotor.apply {
            configFactoryDefault(100)

            setNeutralMode(NeutralMode.Coast)
            setSensorPhase(false)
            setInverted(false)

            configSupplyCurrentLimit(SupplyCurrentLimitConfiguration(true, 40.0, 0.0, 0.0), 100)

            // bang bang PID
            config_kP(0, 10000.0, 100)
            config_kI(0, 0.0, 100)
            config_kD(0, 10.0, 100)
            config_kF(0, 0.0, 100)

            // velocity controlle PID
            config_kP(1, 0.5, 100)
            config_kI(1, 0.0, 100)
            config_kD(1, 0.0, 100)
            config_kF(1, 0.06, 100)

            // bang bang PID control
            selectProfileSlot(1, 0)

            setSelectedSensorPosition(0.0, 0, 100)
            configClosedloopRamp(1.0, 100)

            configClosedLoopPeriod(0, 1, 100)

            configPeakOutputForward(1.0, 100)
            configPeakOutputReverse(-1.0, 100)
            setStatusFramePeriod(StatusFrame.Status_2_Feedback0, 50, 100)
            setControlFramePeriod(ControlFrame.Control_3_General, 50)
        }

        mainMotor.apply {
            configFactoryDefault(100)

            setNeutralMode(NeutralMode.Coast)
            setSensorPhase(false)
            setInverted(true)

            configSupplyCurrentLimit(SupplyCurrentLimitConfiguration(true, 40.0, 0.0, 0.0), 100)

            // bang bang PID
            config_kP(0, 10000.0, 100)
            config_kI(0, 0.0, 100)
            config_kD(0, 10.0, 100)
            config_kF(0, 0.0, 100)

            // velocity controlle PID
            config_kP(1, 0.7, 100) // 0.7
            config_kI(1, 0.0, 100)
            config_kD(1, 0.007, 100)
            config_kF(1, 0.06, 100) // .06

            // bang bang PID
            selectProfileSlot(1, 0)

            setSelectedSensorPosition(0.0, 0, 100)
            configClosedloopRamp(1.0, 100)

            configClosedLoopPeriod(0, 1, 100)

            configPeakOutputForward(1.0, 100)
            configPeakOutputReverse(-1.0, 100)
            setStatusFramePeriod(StatusFrame.Status_2_Feedback0, 50, 100)
            setControlFramePeriod(ControlFrame.Control_3_General, 50)
        }

        // layout.addNumber("Attempted Main Velocity", { setpointMain })
        // layout.addNumber("Attempted Kicker Velocity", { setpointKicker })
        // layout.addNumber("Real Main Velocity", { flyWheelVelocity(mainMotor) })
        // layout.addNumber("Real Kicker Velocity", { flyWheelVelocity(kickerMotor) })
        // layout.addBoolean("Sped up", { isSpedUp() });
        // layout.addNumber("multiplier", { shooterMultiplier })
        // tab.add("Main shooter velocity", 0.0)
        //     .withPosition(8, 1)
        //     .withSize(2, 1)
        //     .withWidget(BuiltInWidgets.kNumberSlider)
        //     .withProperties(mapOf("min" to -15000, "max" to 22000))
        //     .getEntry()
        //     .addListener({ value: EntryNotification -> this.mainVelocity = value.value.getDouble() }, EntryListenerFlags.kUpdate)
        // tab.add("Kicker shooter velocity", 0.0)
        //     .withPosition(8, 2)
        //     .withSize(2, 1)
        //     .withWidget(BuiltInWidgets.kNumberSlider)
        //     .withProperties(mapOf("min" to -15000, "max" to 22000))
        //     .getEntry()
        //     .addListener({ value: EntryNotification -> this.kickerVelocity = value.value.getDouble() }, EntryListenerFlags.kUpdate)
    }

    // get velocity of flywheel
    public fun flyWheelVelocity(motor: TalonFX): Double {
        return motor.getSelectedSensorVelocity(0)
    }

    //check if flywheel velocity is at target
    public fun isSpedUp(): Boolean {
        return flyWheelVelocity(mainMotor) / shooterMultiplier >= setpointMain -600.0 && flyWheelVelocity(kickerMotor) / shooterMultiplier >= setpointKicker -600.0 && (setpointMain != 0.0 || setpointKicker != 0.0)
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
        mainMotor.set(ControlMode.Velocity, setpointMain * shooterMultiplier)
        kickerMotor.set(ControlMode.Velocity, setpointKicker * shooterMultiplier)
    }

    override fun periodic() {
    }

    override fun simulationPeriodic() {
    }
}