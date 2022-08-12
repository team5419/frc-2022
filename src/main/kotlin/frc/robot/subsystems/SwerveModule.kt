package frc.robot.subsystems;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.math.geometry.Pose2d;
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

public class SwerveModule {
    private val driveMotor: TalonFX;
    private val turnMotor: CANSparkMax;
    private val turnController: SparkMaxPIDController;
    private var lastAngle: Double;
    private val driveSim: TalonFXSimCollection;
    private val turnEncoder: CANCoder;
    private val turnEncoderSim: CANCoderSimCollection;
    constructor(drivePort: Int, turnPort: Int, cancoderPort: Int, driveInverted: Boolean = false, turnInverted: Boolean = false) {
        this.driveMotor = TalonFX(drivePort);
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
        this.turnController = this.turnMotor.getPIDController();
        this.turnController.apply {
            setP(DriveConstants.TurnPID.P, 1)
            setI(DriveConstants.TurnPID.I, 1)
            setD(DriveConstants.TurnPID.D, 1)
        }
        this.turnEncoder = CANCoder(cancoderPort);
        val config: CANCoderConfiguration = CANCoderConfiguration();
        config.sensorCoefficient = Math.PI / 2048.0;
        config.unitString = "rad";
        config.sensorTimeBase = SensorTimeBase.PerSecond;
        this.turnEncoder.configAllSettings(config, 100);
        this.driveMotor.apply {
            configFactoryDefault(100)
            configSupplyCurrentLimit(SupplyCurrentLimitConfiguration(true, 40.0, 0.0, 0.0), 100)

            setSensorPhase(false)
            setInverted(driveInverted)

            config_kP( 0, DriveConstants.DrivePID.P , 100 )
            config_kI( 0, DriveConstants.DrivePID.I , 100 )
            config_kD( 0, DriveConstants.DrivePID.D , 100 )
            config_kF( 0, 0.0 , 100 )

            setSelectedSensorPosition(0.0, 0, 100)

            configVoltageCompSaturation(12.0, 100)
            enableVoltageCompensation(true)
            setStatusFramePeriod(StatusFrame.Status_2_Feedback0, 10, 100)
            setControlFramePeriod(ControlFrame.Control_3_General, 10)

            setNeutralMode(NeutralMode.Coast)

            configClosedLoopPeakOutput(0, 0.1, 100)
        }
        this.driveSim = TalonFXSimCollection(this.driveMotor);
        this.turnEncoderSim = CANCoderSimCollection(this.turnEncoder);
        this.lastAngle = getTurn().radians;
    }

  public fun getTurn(): Rotation2d {
        return Rotation2d(turnEncoder.getPosition());
  }

  public fun getState(): SwerveModuleState {
    return SwerveModuleState(Util.nativeUnitsToMetersPerSecond(driveMotor.getSelectedSensorVelocity((0))), getTurn());
  }

  public fun setDesiredState(desiredState: SwerveModuleState) {
    val state: SwerveModuleState = SwerveModuleState.optimize(desiredState, getTurn());
    val driveOutput: Double = Util.metersPerSecondToNativeUnits(state.speedMetersPerSecond);
    var turnOutput: Double = if ((Math.abs(desiredState.speedMetersPerSecond)) <= (DriveConstants.Ramsete.maxVelocity * 0.01)) lastAngle else desiredState.angle.radians;
    turnOutput = Util.radiansToNativeUnits(turnOutput);
    driveMotor.set(ControlMode.Velocity, driveOutput, DemandType.ArbitraryFeedForward, DriveConstants.feedForward.calculate(desiredState.speedMetersPerSecond));
    //turnMotor.set(ControlMode.Position, turnOutput);
  }

  public fun simulationPeriodic(dt: Double) {
    // m_turnMotorSim.setInputVoltage(m_turnOutput / ModuleConstants.kMaxModuleAngularSpeedRadiansPerSecond * RobotController.getBatteryVoltage());
    this.driveSim.setBusVoltage(RobotController.getBatteryVoltage());

    // m_turnMotorSim.update(dt);
    // m_driveMotorSim.update(dt);

    // // Calculate distance traveled using RPM * dt
    // m_simTurnEncoderDistance += m_turnMotorSim.getAngularVelocityRadPerSec() * dt;
    // m_turningEncoderSim.setDistance(m_simTurnEncoderDistance);
    // m_turningEncoderSim.setRate(m_turnMotorSim.getAngularVelocityRadPerSec());

    // m_driveEncoderSim.setRate(m_driveMotorSim.getAngularVelocityRadPerSec());
  }

  public fun resetEncoders() {
    driveMotor.setSelectedSensorPosition(0.0);
    turnEncoder.setPosition(0.0);
  }

  public fun setBrakeMode(on: Boolean = true) {
    driveMotor.setNeutralMode((if (on) NeutralMode.Brake else NeutralMode.Coast));
    turnMotor.setIdleMode((if (on) IdleMode.kBrake else IdleMode.kCoast)); 
  }
}