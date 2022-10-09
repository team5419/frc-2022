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

import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.trajectory.TrapezoidProfile;

import frc.robot.ClimberConstants
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab

import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout
import edu.wpi.first.wpilibj.AnalogInput


class Climber(tab: ShuffleboardTab) : SubsystemBase() {

    public val leftArm: TalonFX;
    public val rightArm: TalonFX;

    // ClimberSingle(TalonFX(ClimberConstants.Ports.left1), false, /*AnalogInput(ClimberConstants.Ports.lsensor0), */ ClimberConstants.Pair0.Left.min, ClimberConstants.Pair0.Left.max), 
    // ClimberSingle(TalonFX(ClimberConstants.Ports.right1), true, /*AnalogInput(ClimberConstants.Ports.rsensor0), */ ClimberConstants.Pair0.Right.min, ClimberConstants.Pair0.Right.max)

    private val layout: ShuffleboardLayout = tab.getLayout("Climber", BuiltInLayouts.kList).withPosition(0, 0).withSize(2, 4);

    //private val m_constraints : TrapezoidProfile.Constraints = TrapezoidProfile.Constraints(1.75, 0.75);
    //private val m_controller : ProfiledPIDController = ProfiledPIDController(1.3, 0.0, 0.7, m_constraints, 0.02);

    private var lastLeftSetpoint: Double = 0.0
    // public val leftSensor0 = AnalogInput(ClimberConstants.Ports.lsensor0)
    // public val rightSensor0 = AnalogInput(ClimberConstants.Ports.rsensor0)
    // public val leftSensor1 = AnalogInput(ClimberConstants.Ports.lsensor1)
    // public val rightSensor1 = AnalogInput(ClimberConstants.Ports.rsensor1)


    // configure the motors and add to shuffleboard
    init {

        leftArm = TalonFX(11)
        rightArm = TalonFX(1)

        val armArray = arrayOf(leftArm, rightArm);
        for(j in 0..armArray.size - 1) {
            armArray[j].apply {
                configFactoryDefault(100)

                setNeutralMode(NeutralMode.Brake)
                setSensorPhase(false)

                configSupplyCurrentLimit(SupplyCurrentLimitConfiguration(true, 65.0, 0.0, 0.0), 100)

                // bang bang PID
                config_kP(0, 10000.0, 100)
                config_kI(0, 0.0, 100)
                config_kD(0, 0.0, 100)
                config_kF(0, 0.0, 100)

                // velocity controlle PID
                config_kP(1, ClimberConstants.PID.P, 100)
                config_kI(1, ClimberConstants.PID.I, 100)
                config_kD(1, ClimberConstants.PID.D, 100)
                config_kF(1, ClimberConstants.PID.F, 100)

                // velocity controlle PID selected
                selectProfileSlot(1, 0)

                setSelectedSensorPosition(0.0, 0, 100)
                configClosedloopRamp(1.0, 100)

                configClosedLoopPeriod(0, 1, 100)

                configPeakOutputForward(1.0, 100)
                configPeakOutputReverse(-1.0, 100)

                setStatusFramePeriod(StatusFrame.Status_2_Feedback0, 50, 100)
                setControlFramePeriod(ControlFrame.Control_3_General, 50)
            }
        }
            
        leftArm.setInverted(false);
        rightArm.setInverted(true);
    }


    public fun setLeftArm(left: Int, throttle: Double) {
        val f: Double = 0.5;
        leftArm.set(ControlMode.PercentOutput, -throttle * f);
    }

    public fun setRightArm(right: Int, throttle: Double) {
        val f: Double = 0.5;
        rightArm.set(ControlMode.PercentOutput, -throttle * f);
    }

    override fun periodic() {
    }

    override fun simulationPeriodic() {
    }
}
