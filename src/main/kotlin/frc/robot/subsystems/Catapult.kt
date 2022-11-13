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
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.hal.simulation.CTREPCMDataJNI;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import frc.robot.ClimberConstants
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab

import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout

import edu.wpi.first.wpilibj.Compressor

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.AnalogPotentiometer;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.PeriodicFrame;

class Catapult(tab: ShuffleboardTab) : SubsystemBase() {

    private val cataSolenoid: Solenoid;

    public var state: Boolean;

    private val layout: ShuffleboardLayout = tab.getLayout("Intake/Catapult", BuiltInLayouts.kList).withPosition(0, 0).withSize(2, 4);

    init {

        cataSolenoid = Solenoid(PneumaticsModuleType.CTREPCM, 1);
        cataSolenoid.set(false);
        state = false;

        layout.addBoolean("catapult", {cataSolenoid.get()})

    }

    public fun start() {
        state = ! state
        cataSolenoid.set(state);
    }

    public fun stop() {
        
    }

    override fun periodic() {

    }

    override fun simulationPeriodic() {

    }
}
