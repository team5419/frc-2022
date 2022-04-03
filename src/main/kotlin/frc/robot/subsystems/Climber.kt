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

import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.trajectory.TrapezoidProfile;

import frc.robot.ClimberConstants
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab

import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout
import edu.wpi.first.wpilibj.AnalogInput


class Climber(tab: ShuffleboardTab) : SubsystemBase() {

    // declare motors and ports
    public val pairs: Array<ClimberPair> = arrayOf(
        ClimberPair(TalonFX(ClimberConstants.Ports.left1), false, TalonFX(ClimberConstants.Ports.right1), true),
        ClimberPair(TalonFX(ClimberConstants.Ports.left2), true, TalonFX(ClimberConstants.Ports.right2), false)
    )
    private val layout: ShuffleboardLayout = tab.getLayout("Climber", BuiltInLayouts.kList).withPosition(0, 0).withSize(2, 4);

    private val m_constraints : TrapezoidProfile.Constraints = TrapezoidProfile.Constraints(1.75, 0.75);
    private val m_controller : ProfiledPIDController = ProfiledPIDController(1.3, 0.0, 0.7, m_constraints, 0.02);

    public val leftSensor0 = AnalogInput(ClimberConstants.Ports.lsensor0)
    public val rightSensor0 = AnalogInput(ClimberConstants.Ports.rsensor0)
    public val leftSensor1 = AnalogInput(ClimberConstants.Ports.lsensor1)
    public val rightSensor1 = AnalogInput(ClimberConstants.Ports.rsensor1)


    // configure the motors and add to shuffleboard
    init {
        for(i in 0..pairs.size - 1) {
            val pairArray = arrayOf(pairs[i].left, pairs[i].right);
            for(j in 0..pairArray.size - 1) {
                pairArray[j].apply {
                    configFactoryDefault(100)

                    setNeutralMode(NeutralMode.Brake)
                    setSensorPhase(false)

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
            }
            
            pairs[i].left.setInverted(pairs[i].invertedLeft);
            pairs[i].right.setInverted(pairs[i].invertedRight);

            val newLayout: ShuffleboardLayout = layout.getLayout("Pair ${i}", BuiltInLayouts.kList);
            //newLayout.addNumber("left", { pairs[i].left.getSelectedSensorVelocity() });
            //newLayout.addNumber("right", { pairs[i].right.getSelectedSensorVelocity() });
        }
    }

    val left0: Double
        get() = leftSensor0.getValue().toDouble()
    val right0: Double
        get() = rightSensor0.getValue().toDouble()
    val left1: Double 
        get() = leftSensor1.getValue().toDouble()
    val right1: Double
        get() = rightSensor1.getValue().toDouble()

    fun getAllVelocities() : List<Double> {
        var returning: MutableList<Double> = mutableListOf<Double>();
        for(pair in pairs) {
            returning.add(pair.left.getSelectedSensorVelocity());
            returning.add(pair.right.getSelectedSensorVelocity());
        }
        return returning.toList();
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

    public fun setPairVelocity(pair: Int, throttle: Double, turn: Double = 0.0) {
        if(pairs[pair].previousThrottle == throttle && pairs[pair].previousTurn == turn) {
            return;
        }
        pairs[pair].previousThrottle = throttle;
        pairs[pair].previousTurn = turn;
        val f: Double = 15000.0;
        val leftturn: Double = /*if (turn < 0)*/ withDeadband(turn, 0.05)// else 0.0;
        val rightturn: Double =/*  if(turn > 0) */ withDeadband(turn, 0.05)// else 0.0;
        pairs[pair].left.set(ControlMode.Velocity, (withDeadband(-throttle - leftturn, 0.1)) * f);
        pairs[pair].right.set(ControlMode.Velocity, (withDeadband(-throttle + rightturn, 0.1)) * f);
        //println("setting velocity to: " + ((withDeadband(-throttle, 0.1)) * f))
    }

    public fun setPair0(throttle: Double, turn: Double = 0.0) {
        val f: Double = 15000.0;
        val leftturn: Double = /*if (turn < 0)*/ withDeadband(turn, 0.05)// else 0.0;
        pairs[0].left.set(ControlMode.Velocity, (withDeadband(-throttle - leftturn, 0.1)) * f);
        m_controller.setGoal(left0);
        pairs[0].right.set(ControlMode.Velocity, m_controller.calculate(right0));
    }

    public fun setPair1(throttle: Double, turn: Double = 0.0) {
        val f: Double = 15000.0;
        val leftturn: Double = /*if (turn < 0)*/ withDeadband(turn, 0.05)// else 0.0;
        pairs[1].left.set(ControlMode.Velocity, (withDeadband(-throttle - leftturn, 0.1)) * f);
        m_controller.setGoal(left1);
        pairs[1].right.set(ControlMode.Velocity, m_controller.calculate(right1));
    }

    public fun setPair(pair: Int, throttle: Double) {
        val f: Double = 0.5;
        pairs[pair].left.set(ControlMode.PercentOutput, withDeadband(-throttle, 0.001) * f);
        pairs[pair].right.set(ControlMode.PercentOutput, withDeadband(-throttle, 0.001) * f);
    }

    override fun periodic() {
    }

    override fun simulationPeriodic() {
    }
}