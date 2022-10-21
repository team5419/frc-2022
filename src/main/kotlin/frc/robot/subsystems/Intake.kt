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


class Intake(tab: ShuffleboardTab) : SubsystemBase() {

    public val pcmCompressor: Compressor;
    public val solenoid: DoubleSolenoid;
    //public val phCompressor: Compressor;

    //momomomotteres

    // ClimberSingle(TalonFX(ClimberConstants.Ports.left1), false, /*AnalogInput(ClimberConstants.Ports.lsensor0), */ ClimberConstants.Pair0.Left.min, ClimberConstants.Pair0.Left.max), 
    // ClimberSingle(TalonFX(ClimberConstants.Ports.right1), true, /*AnalogInput(ClimberConstants.Ports.rsensor0), */ ClimberConstants.Pair0.Right.min, ClimberConstants.Pair0.Right.max)

    private val layout: ShuffleboardLayout = tab.getLayout("Intake", BuiltInLayouts.kList).withPosition(0, 0).withSize(2, 4);

    //private val m_constraints : TrapezoidProfile.Constraints = TrapezoidProfile.Constraints(1.75, 0.75);
    //private val m_controller : ProfiledPIDController = ProfiledPIDController(1.3, 0.0, 0.7, m_constraints, 0.02);

    init {

        pcmCompressor = Compressor(0, PneumaticsModuleType.CTREPCM);
        solenoid = DoubleSolenoid(PneumaticsModuleType.CTREPCM, 0, 1);
        solenoid.set(DoubleSolenoid.Value.kOff);
        //phCompressor = Compressor(1, PneumaticsModuleType.REVPH)
        pcmCompressor.enableAnalog(115.0, 125.0);
        // pcmCompressor.enableDigital();
        // pcmCompressor.disable();

        // val enabled: Boolean = pcmCompressor.enabled();
        // val pressureSwitch: Boolean = pcmCompressor.getPressureSwitchValue();
        // val current: Double = pcmCompressor.getCompressorCurrent();

        // tab.addNumber("left arm encoder:", {leftArm.getSelectedSensorPosition(0)})
        // tab.addNumber("right arm encoder:", {rightArm.getSelectedSensorPosition(0)})
            
        // leftArm.setInverted(false);
        // rightArm.setInverted(true);
    }


    public fun feedForward() {
        pcmCompressor.enableDigital()
        //pcmCompressor.enableAnalog(115.0, 125.0)
        solenoid.set(DoubleSolenoid.Value.kForward);
    }

    public fun stop() {
        solenoid.set(DoubleSolenoid.Value.kOff);
    }

    override fun periodic() {
    }

    override fun simulationPeriodic() {
    }
}
