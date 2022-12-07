// package frc.robot.subsystems;

// import edu.wpi.first.wpilibj2.command.SubsystemBase;
// import com.ctre.phoenix.motorcontrol.can.TalonFX
// import com.ctre.phoenix.motorcontrol.can.VictorSPX
// import com.ctre.phoenix.motorcontrol.can.TalonSRX
// import edu.wpi.first.networktables.NetworkTableEntry
// import edu.wpi.first.networktables.EntryListenerFlags
// import edu.wpi.first.networktables.EntryNotification
// import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets
// import com.ctre.phoenix.motorcontrol.*
// import kotlin.math.*
// import edu.wpi.first.wpilibj.DoubleSolenoid;

// import edu.wpi.first.math.controller.ProfiledPIDController;
// import edu.wpi.first.math.trajectory.TrapezoidProfile;
// import edu.wpi.first.wpilibj.PneumaticsModuleType;
// import frc.robot.ClimberConstants
// import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard
// import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab

// import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts
// import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout

// import edu.wpi.first.wpilibj.Compressor

// import edu.wpi.first.wpilibj.AnalogInput;
// import edu.wpi.first.wpilibj.AnalogPotentiometer;


// class Feeder(tab: ShuffleboardTab, reverse: Boolean) : SubsystemBase() {

//     public val pcmCompressor: Compressor;
//     public val solenoid: DoubleSolenoid;

//     public val feeder: TalonFX;
//     public val reverse: Boolean;
//     //public val phCompressor: Compressor;

//     //momomomotteres

//     // ClimberSingle(TalonFX(ClimberConstants.Ports.left1), false, /*AnalogInput(ClimberConstants.Ports.lsensor0), */ ClimberConstants.Pair0.Left.min, ClimberConstants.Pair0.Left.max), 
//     // ClimberSingle(TalonFX(ClimberConstants.Ports.right1), true, /*AnalogInput(ClimberConstants.Ports.rsensor0), */ ClimberConstants.Pair0.Right.min, ClimberConstants.Pair0.Right.max)

//     private val layout: ShuffleboardLayout = tab.getLayout("Intake", BuiltInLayouts.kList).withPosition(0, 0).withSize(2, 4);

//     //private val m_constraints : TrapezoidProfile.Constraints = TrapezoidProfile.Constraints(1.75, 0.75);
//     //private val m_controller : ProfiledPIDController = ProfiledPIDController(1.3, 0.0, 0.7, m_constraints, 0.02);

//     init {

//         feeder = TalonFX(13);
//         reverse = false;

//         // val enabled: Boolean = pcmCompressor.enabled();
//         // val pressureSwitch: Boolean = pcmCompressor.getPressureSwitchValue();
//         // val current: Double = pcmCompressor.getCompressorCurrent();

//         // tab.addNumber("left arm encoder:", {leftArm.getSelectedSensorPosition(0)})
//         // tab.addNumber("right arm encoder:", {rightArm.getSelectedSensorPosition(0)})
            
//         // leftArm.setInverted(false);
//         // rightArm.setInverted(true);
//     }

//     public fun feedForward(Boolean: reverse) {
//         if(reverse) {
//             feeder.set(-100)
//         }  else {
//             feeder.set(100)
//         }
        
//     }

//     public fun stop() {
        
//     }

//     override fun periodic() {
//     }

//     override fun simulationPeriodic() {
//     }
// }
