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

import frc.robot.IntakeConstants;

class IntakeSub(tab: ShuffleboardTab) : SubsystemBase() {

    public val motor: CANSparkMax = CANSparkMax(IntakeConstants.Ports.motor, MotorType.kBrushless);
    private val pcmCompressor: Compressor;
    private val solenoid: Solenoid;
    public var intakeState: Boolean;
    public var deployState: Boolean;

    private val layout: ShuffleboardLayout = tab.getLayout("Intake/Catapult", BuiltInLayouts.kList).withPosition(0, 0).withSize(2, 4);

    //private val m_constraints : TrapezoidProfile.Constraints = TrapezoidProfile.Constraints(1.75, 0.75);
    //private val m_controller : ProfiledPIDController = ProfiledPIDController(1.3, 0.0, 0.7, m_constraints, 0.02);

    init {
        
        motor.apply {
            restoreFactoryDefaults()
            setIdleMode(IdleMode.kCoast)
            setInverted(true)
            //setSensorPhase(false)
            setSmartCurrentLimit(40)
            setClosedLoopRampRate(1.0)
            setControlFramePeriodMs(50)
            setPeriodicFramePeriod(PeriodicFrame.kStatus2, 50)
        }

        pcmCompressor = Compressor(0, PneumaticsModuleType.CTREPCM);
        solenoid = Solenoid(PneumaticsModuleType.CTREPCM, 0);
        solenoid.set(false);

        pcmCompressor.enableDigital();

        layout.addBoolean("pressure switch:", {pcmCompressor.getPressureSwitchValue()})
        layout.addBoolean("deploy", {solenoid.get()})

        intakeState = false;
        deployState = false;
    }

    public fun deploy() {
        deployState = ! deployState
        solenoid.set(deployState)
    }

    public fun intake() {
        intakeState = ! intakeState
        println(intakeState)
        motor.set(if (intakeState) IntakeConstants.outputPercent else 0.0)
    }

    override fun periodic() {
    }

    override fun simulationPeriodic() {
    }
}
