package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.ctre.phoenix.motorcontrol.can.TalonFX
import com.ctre.phoenix.motorcontrol.can.VictorSPX
import com.ctre.phoenix.motorcontrol.can.TalonSRX
import edu.wpi.first.networktables.NetworkTableEntry
import edu.wpi.first.networktables.EntryListenerFlags
import edu.wpi.first.networktables.EntryNotification
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets
import com.ctre.phoenix.motorcontrol.*
import kotlin.math.*
import frc.robot.classes.ClimberPair;

import frc.robot.ClimberConstants
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab

import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout

class Climber(tab: ShuffleboardTab) : SubsystemBase() {

    // declare motors and ports
    public val pairs: Array<ClimberPair> = arrayOf(
        ClimberPair(TalonFX(ClimberConstants.Ports.left1), false, TalonFX(ClimberConstants.Ports.right1), true),
        ClimberPair(TalonFX(ClimberConstants.Ports.left2), true, TalonFX(ClimberConstants.Ports.right2), false)
    )
    private val layout: ShuffleboardLayout = tab.getLayout("Climber", BuiltInLayouts.kList).withPosition(0, 0).withSize(2, 4);

    // configure the motors and add to shuffleboard
    init {
        for(i in 0..pairs.size - 1) {
            pairs[i].left.apply {
                configFactoryDefault(100)
                setInverted(pairs[i].invertedLeft)
                configPeakOutputForward(1.0)
                configPeakOutputReverse(-1.0)
                setNeutralMode(NeutralMode.Brake)
            }

            pairs[i].right.apply {
                configFactoryDefault(100)
                setInverted(pairs[i].invertedRight)
                configPeakOutputForward(1.0)
                configPeakOutputReverse(-1.0)
                setNeutralMode(NeutralMode.Brake)
            }
            val newLayout: ShuffleboardLayout = layout.getLayout("Pair ${i}", BuiltInLayouts.kList);
            newLayout.addNumber("left", { pairs[i].left.getSelectedSensorVelocity() });
            newLayout.addNumber("right", { pairs[i].right.getSelectedSensorVelocity() });
        }
    }

    public fun stop() {
        for(pair in pairs) {
            pair.left.set(ControlMode.PercentOutput, 0.0)
            pair.right.set(ControlMode.PercentOutput, 0.0)
        }
    }

    fun withDeadband(movement: Double, deadband: Double): Double {
        if(abs(movement) <= deadband) return 0.0;
        if(abs(movement) > 1.0) return sign(movement);
        return movement;
    }

    public fun setPairVelocity(pair: Int, throttle: Double, turn: Double) {
        val f: Double = 15000.0;
        pairs[pair].left.set(ControlMode.Velocity, withDeadband((-throttle - turn), 0.001) * f);
        pairs[pair].right.set(ControlMode.Velocity, withDeadband((-throttle + turn), 0.001) * f);
    }

    public fun setPair(pair: Int, throttle: Double, turn: Double) {
        val f: Double = 0.5;
        pairs[pair].left.set(ControlMode.PercentOutput, withDeadband((-throttle - turn), 0.001) * f);
        pairs[pair].right.set(ControlMode.PercentOutput, withDeadband((-throttle + turn), 0.001) * f);
    }

    public fun setIndividual(motor: TalonFX, percent: Double) {
        motor.set(ControlMode.PercentOutput, percent);
    }

    override fun periodic() {
    }

    override fun simulationPeriodic() {
    }
}