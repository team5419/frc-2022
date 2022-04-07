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
import frc.robot.classes.ClimberSingle;

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
        ClimberPair(
            ClimberSingle(TalonFX(ClimberConstants.Ports.left1), false, /*AnalogInput(ClimberConstants.Ports.lsensor0), */ ClimberConstants.Pair0.Left.min, ClimberConstants.Pair0.Left.max), 
            ClimberSingle(TalonFX(ClimberConstants.Ports.right1), true, /*AnalogInput(ClimberConstants.Ports.rsensor0), */ ClimberConstants.Pair0.Right.min, ClimberConstants.Pair0.Right.max)
        ),
        ClimberPair(
            ClimberSingle(TalonFX(ClimberConstants.Ports.left2), true, /*AnalogInput(ClimberConstants.Ports.lsensor1), */ ClimberConstants.Pair1.Left.min, ClimberConstants.Pair1.Left.max),
            ClimberSingle(TalonFX(ClimberConstants.Ports.right2), false, /*AnalogInput(ClimberConstants.Ports.rsensor1), */ ClimberConstants.Pair1.Right.min, ClimberConstants.Pair1.Right.max)
        )
    )
    private val layout: ShuffleboardLayout = tab.getLayout("Climber", BuiltInLayouts.kList).withPosition(0, 0).withSize(2, 4);

    // private val m_constraints : TrapezoidProfile.Constraints = TrapezoidProfile.Constraints(1.75, 0.75);
    // private val m_controller : ProfiledPIDController = ProfiledPIDController(1.3, 0.0, 0.7, m_constraints, 0.02);

    private var lastLeftSetpoint: Double = 0.0
    // public val leftSensor0 = AnalogInput(ClimberConstants.Ports.lsensor0)
    // public val rightSensor0 = AnalogInput(ClimberConstants.Ports.rsensor0)
    // public val leftSensor1 = AnalogInput(ClimberConstants.Ports.lsensor1)
    // public val rightSensor1 = AnalogInput(ClimberConstants.Ports.rsensor1)


    // configure the motors and add to shuffleboard
    init {
        for(i in 0..pairs.size - 1) {
            val pairArray = arrayOf(pairs[i].left.motor, pairs[i].right.motor);
            for(j in 0..pairArray.size - 1) {
                pairArray[j].apply {
                    configFactoryDefault(100)

                    setNeutralMode(NeutralMode.Brake)
                    setSensorPhase(false)

                    configSupplyCurrentLimit(SupplyCurrentLimitConfiguration(true, 20.0, 0.0, 0.0), 100)

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
            
            pairs[i].left.motor.setInverted(pairs[i].left.inverted);
            pairs[i].right.motor.setInverted(pairs[i].right.inverted);

            //val newLayout: ShuffleboardLayout = layout.getLayout("Pair ${i}", BuiltInLayouts.kList);
            //newLayout.addNumber("left sensor", { left(i) });
            //newLayout.addNumber("right sensor", { right(i) });
            //newLayout.addNumber("left", { pairs[i].left.getSelectedSensorVelocity() });
            //newLayout.addNumber("right", { pairs[i].right.getSelectedSensorVelocity() });
        }

        layout.addNumber("setpoint", { lastLeftSetpoint });
    }

    fun left(pair: Int): Double {
        return 0.0;
        //return pairs[pair].left.sensor.getValue().toDouble()
    }

    fun right(pair: Int): Double {
        return 0.0;
        //return pairs[pair].right.sensor.getValue().toDouble()
    }

    // val left0: Double
    //     get() = leftSensor0.getValue().toDouble()
    // val right0: Double
    //     get() = rightSensor0.getValue().toDouble()
    // val left1: Double 
    //     get() = leftSensor1.getValue().toDouble()
    // val right1: Double
    //     get() = rightSensor1.getValue().toDouble()

    fun getAllVelocities() : List<Double> {
        var returning: MutableList<Double> = mutableListOf<Double>();
        for(pair in pairs) {
            returning.add(pair.left.motor.getSelectedSensorVelocity());
            returning.add(pair.right.motor.getSelectedSensorVelocity());
        }
        return returning.toList();
    }

    public fun stop() {
        for(pair in pairs) {
            pair.left.motor.set(ControlMode.PercentOutput, 0.0)
            pair.right.motor.set(ControlMode.PercentOutput, 0.0)
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
        val leftturn: Double = /*if (turn < 0)*/ withDeadband(turn, 0.1)// else 0.0;
        val rightturn: Double =/*  if(turn > 0) */ withDeadband(turn, 0.1)// else 0.0;
        pairs[pair].left.motor.set(ControlMode.Velocity, (withDeadband(-throttle - leftturn, 0.1)) * f);
        pairs[pair].right.motor.set(ControlMode.Velocity, (withDeadband(-throttle + rightturn, 0.1)) * f);
        lastLeftSetpoint = (withDeadband(-throttle - leftturn, 0.1)) * f;
        //println("setting velocity to: " + ((withDeadband(-throttle, 0.1)) * f))
    }

    public fun setPairPIDAdjust(pair: Int, throttle: Double, turn: Double = 0.0) {
        val f: Double = 15000.0;
        val leftturn: Double = /*if (turn < 0)*/ withDeadband(turn, 0.05)// else 0.0;
        pairs[pair].left.motor.set(ControlMode.Velocity, (withDeadband(-throttle - leftturn, 0.1)) * f);
        m_controller.setGoal(left(pair));
        pairs[pair].right.motor.set(ControlMode.Velocity, m_controller.calculate(right(pair)));
        // note from theo:
        // this function uses a pid controller to control the right climber hook
        // it sets the left hook normally and then uses the sensor values to calculate the output for the right hook
        // this might make the climbers a little wonky on the way up, but once they stop moving, they should level out
    }

    public fun setPairWithStops(pair: Int, throttle: Double, turn: Double = 0.0) {
        if(pairs[pair].previousThrottle == throttle && pairs[pair].previousTurn == turn) {
            return;
        }
        pairs[pair].previousThrottle = throttle;
        pairs[pair].previousTurn = turn;
        val f: Double = 15000.0;
        val _turn: Double = withDeadband(turn, 0.05)
        val _left = (withDeadband(-throttle - _turn, 0.1)) * f
        val _right = (withDeadband(-throttle + _turn, 0.1)) * f
        if((_left < 0 && left(pair) < pairs[pair].left.max) || (_left > 0 && left(pair) > pairs[pair].left.min)) {
            pairs[pair].left.motor.set(ControlMode.Velocity, _left);
        }
        if((_right < 0 && right(pair) < pairs[pair].right.max) || (_right > 0 && right(pair) > pairs[pair].right.min)) {
            pairs[pair].right.motor.set(ControlMode.Velocity, _right);
        }
        // note from theo:
        // i'm making two assumptions when writing this: first, that the maximum is greater than the minimum 
        // (in other words the sensors aren't backwards)
        // second, that negative power on the motors actually makes the climbers go up rather than down
        // (this is just based on what i remember from previous testing)
        // if this second assumption is wrong (aka setpoint is positive), then...
        // replace line 183, column 19 with >
        // replace line 183, column 71 with <
        // replace line 186, column 20 with >
        // and replace line 186, column 75 with < 
        // P.S. unlike all of these other testing functions, this one should definitely work
        // because it's just a clone of the original climbing functions with some limits imposed.
        // if it doesn't work, tell theo or emma on slack and specify what wasn't working
    }

    public fun setPairSlightAdjust(pair: Int, throttle: Double, turn: Double = 0.0) {
        val f: Double = 15000.0;
        val velocity: Double = (withDeadband(-throttle, 0.1)) * f
        var leftturn: Double = withDeadband(turn, 0.1)
        var rightturn: Double = leftturn
        if(leftturn == 0.0) {
            val leftResult = left(pair);
            val rightResult = right(pair);
            if(abs(leftResult - leftResult) > ClimberConstants.adjustmentDeadband) {
                if(leftResult < rightResult) {
                    leftturn = ClimberConstants.adjustment;
                } else if(rightResult < leftResult) {
                    rightturn = ClimberConstants.adjustment;
                }
            }
        }
        
        pairs[pair].left.motor.set(ControlMode.Velocity, velocity  - leftturn);
        pairs[pair].right.motor.set(ControlMode.Velocity, velocity + rightturn);
        // note from theo:
        // this function is going to require a lot of precise tuning to work
        // if the climbers don't seem to be adjusting at all, go to constants and increase ClimberConstants.adjustment
        // if this doesn't work, try setting ClimberConstants.adjustmentDeadband all the way to 0.0
        // if the climbers are adjusting the wrong way, make ClimberConstants.adjustment positive instead of negative
        // once it starts sort of working, you'll need to find a good deadband that will prevent the climbers from
        // jerking back and forth, as well as an adjustment rate that successfully levels the climbers without
        // speeding past the deadband and causing a huge oscillation.
    }

    public fun toMax(pair: Int) {
        pairs[pair].left.motor.set(ControlMode.Position, pairs[pair].left.max);
        pairs[pair].right.motor.set(ControlMode.Position, pairs[pair].right.max);
    }

    public fun toMin(pair: Int) {
        pairs[pair].left.motor.set(ControlMode.Position, pairs[pair].left.min);
        pairs[pair].right.motor.set(ControlMode.Position, pairs[pair].right.min);
    }

    public fun setPair(pair: Int, throttle: Double) {
        val f: Double = 0.5;
        pairs[pair].left.motor.set(ControlMode.PercentOutput, withDeadband(-throttle, 0.001) * f);
        pairs[pair].right.motor.set(ControlMode.PercentOutput, withDeadband(-throttle, 0.001) * f);
    }

    override fun periodic() {
    }

    override fun simulationPeriodic() {
    }
}