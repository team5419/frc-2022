package frc.robot.modules;
import frc.robot.modules.ISwerveModule;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout
import edu.wpi.first.math.Matrix;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.numbers.N2;
import edu.wpi.first.math.numbers.N1;
import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.StatusFrame;
import com.ctre.phoenix.motorcontrol.ControlFrame;
import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.wpilibj.RobotController;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import edu.wpi.first.math.geometry.Rotation2d;
import com.ctre.phoenix.motorcontrol.DemandType;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import frc.robot.Util;
import com.revrobotics.CANSparkMaxLowLevel.PeriodicFrame;
import frc.robot.DriveConstants;
import edu.wpi.first.wpilibj.simulation.EncoderSim;
import edu.wpi.first.wpilibj.simulation.FlywheelSim;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.jni.CANSparkMaxJNI;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.RelativeEncoder;
import com.ctre.phoenix.sensors.CANCoderSimCollection;
import com.ctre.phoenix.sensors.CANCoder;
import com.ctre.phoenix.sensors.CANCoderConfiguration;
import com.ctre.phoenix.sensors.SensorTimeBase;
import edu.wpi.first.math.system.plant.LinearSystemId;
import com.ctre.phoenix.motorcontrol.TalonFXSimCollection;

public class SwerveModule : ISwerveModule {
    private val driveMotor: TalonFX;
    private val turnMotor: CANSparkMax;
    private val turnEncoder: CANCoder;
    private val offset: Double;
    private val tab: ShuffleboardTab = Shuffleboard.getTab("Drivetrain");
    private var lastTurnOutput: Double;
    private var lastPercentOutput: Double;
    private var lastTurnPercent: Double;
    constructor(drivePort: Int, turnPort: Int, cancoderPort: Int, _offset: Double, driveInverted: Boolean = false, turnInverted: Boolean = false) {
      this.offset = _offset;
      this.lastTurnOutput = 0.0;
      this.lastPercentOutput = 0.0;
      this.lastTurnPercent = 0.0;
      this.driveMotor = TalonFX(drivePort, "canivore");
        this.turnMotor = CANSparkMax(turnPort, MotorType.kBrushless);
        this.turnMotor.apply {
          restoreFactoryDefaults()
          setIdleMode(IdleMode.kCoast)
          setInverted(turnInverted)
          //setSensorPhase(false)
          setSmartCurrentLimit(40)
          setClosedLoopRampRate(1.0)
          setControlFramePeriodMs(50)
          setPeriodicFramePeriod(PeriodicFrame.kStatus2, 50)
        }
        this.turnEncoder = CANCoder(cancoderPort, "canivore");
        val config: CANCoderConfiguration = CANCoderConfiguration();
        config.sensorCoefficient = Math.PI / 2048.0;
        config.unitString = "rad";
        config.sensorTimeBase = SensorTimeBase.PerSecond;
        this.turnEncoder.configAllSettings(config, 100);
        this.turnEncoder.setPositionToAbsolute(100);
        this.driveMotor.apply {
            configFactoryDefault(100)
            configSupplyCurrentLimit(SupplyCurrentLimitConfiguration(true, 40.0, 0.0, 0.0), 100)

            setSensorPhase(false)
            setInverted(driveInverted)

            config_kP( 0, DriveConstants.Modules.DrivePID.P , 100 )
            config_kI( 0, DriveConstants.Modules.DrivePID.I , 100 )
            config_kD( 0, DriveConstants.Modules.DrivePID.D , 100 )
            config_kF( 0, 0.0 , 100 )
            selectProfileSlot(0, 0)

            setSelectedSensorPosition(0.0, 0, 100)

            configVoltageCompSaturation(12.0, 100)
            enableVoltageCompensation(true)
            setStatusFramePeriod(StatusFrame.Status_2_Feedback0, 10, 100)
            setControlFramePeriod(ControlFrame.Control_3_General, 10)

            setNeutralMode(NeutralMode.Coast)

            configClosedLoopPeakOutput(0, 1.0, 100)
        }
        val layout: ShuffleboardLayout = tab.getLayout("Turn port ${turnPort}", "List Layout");
        layout.addNumber("angle", { getTurn().radians });
        layout.addNumber("drive", { getDrive() });
        layout.addNumber("desired angle output", { this.lastTurnOutput });
        layout.addNumber("desired percent output", { this.lastPercentOutput });
        layout.addNumber("last angle output", { this.lastTurnPercent });
    }

  public override fun getTurn(): Rotation2d {
        return Rotation2d(turnEncoder.getPosition() - this.offset);
  }

  public override fun getDrive(): Double {
    return driveMotor.getSelectedSensorVelocity(0);
  }

  public override fun getState(): SwerveModuleState {
    return SwerveModuleState(Util.nativeUnitsToMetersPerSecond(getDrive()), getTurn());
  }

  private fun optimize(state: SwerveModuleState, turn: Rotation2d, slow: Boolean): SwerveModuleState {
    val stateRadians: Double = state.angle.getRadians();
    val a: Long = Math.round((turn.getRadians() - stateRadians) / Math.PI);
    return SwerveModuleState(
        state.speedMetersPerSecond * (if (a % 2 == 0L) 1.0 else -1.0) * (if (slow) DriveConstants.slowMultiplier else 1.0),
        Rotation2d(a * Math.PI + stateRadians)
    );
    // val a: Long = Math.round((turn.getRadians() - stateRadians) / (2 * Math.PI));
    // return SwerveModuleState(
    //     state.speedMetersPerSecond,
    //     Rotation2d(a * 2 * Math.PI + stateRadians)
    // );
  }

  public override fun setDesiredState(desiredState: SwerveModuleState, preventTurn: Boolean, slow: Boolean) {
    val turn: Rotation2d = getTurn();
    val state: SwerveModuleState = this.optimize(desiredState, turn, slow);
    val driveFeedForward: Double = DriveConstants.feedForward.calculate(state.speedMetersPerSecond);
    if(state.speedMetersPerSecond != 0.0) {
      println(state.speedMetersPerSecond)
    }

    this.lastPercentOutput = Util.metersPerSecondToNativeUnits(state.speedMetersPerSecond)
    //driveMotor.set(ControlMode.Velocity, this.lastPercentOutput);
    driveMotor.set(ControlMode.Velocity, this.lastPercentOutput, DemandType.ArbitraryFeedForward, driveFeedForward)

    // this.lastPercentOutput = state.speedMetersPerSecond / DriveConstants.SwerveRamsete.maxVelocity;
    //driveMotor.set(ControlMode.PercentOutput, this.lastPercentOutput);
    
    
    if(preventTurn) {
      turnMotor.setVoltage(0.0);
      return;
    }
    var newTurnOutput: Double = DriveConstants.Modules.turnController.calculate(turn.radians, state.angle.radians);
    this.lastTurnOutput = state.angle.radians;
    this.lastTurnPercent = newTurnOutput;
    turnMotor.setVoltage(newTurnOutput);
  }

  public override fun simulationPeriodic(dt: Double) {}

  public override fun resetEncoders() {
    driveMotor.setSelectedSensorPosition(0.0);
    turnEncoder.setPosition(0.0);
  }

  public override fun setBrakeMode(on: Boolean) {
    driveMotor.setNeutralMode((if (on) NeutralMode.Brake else NeutralMode.Coast));
    turnMotor.setIdleMode((if (on) IdleMode.kBrake else IdleMode.kCoast)); 
  }

  public override fun test() {
    driveMotor.set(ControlMode.PercentOutput, 0.1);
  }
}