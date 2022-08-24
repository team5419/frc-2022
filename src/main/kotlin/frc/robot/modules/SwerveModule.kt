package frc.robot.modules;
import frc.robot.modules.ISwerveModule;
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

public class SwerveModule : ISwerveModule {
    private val driveMotor: TalonFX;
    private val turnMotor: CANSparkMax;
    private var lastAngle: Double;
    private val turnEncoder: CANCoder;
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

            config_kP( 0, DriveConstants.Modules.DrivePID.P , 100 )
            config_kI( 0, DriveConstants.Modules.DrivePID.I , 100 )
            config_kD( 0, DriveConstants.Modules.DrivePID.D , 100 )
            config_kF( 0, 0.0 , 100 )

            setSelectedSensorPosition(0.0, 0, 100)

            configVoltageCompSaturation(12.0, 100)
            enableVoltageCompensation(true)
            setStatusFramePeriod(StatusFrame.Status_2_Feedback0, 10, 100)
            setControlFramePeriod(ControlFrame.Control_3_General, 10)

            setNeutralMode(NeutralMode.Coast)

            configClosedLoopPeakOutput(0, 0.1, 100)
        }
        this.lastAngle = getTurn().radians;
    }

  public override fun getTurn(): Rotation2d {
        return Rotation2d(turnEncoder.getPosition());
  }

  private fun getDrive(): Double {
    return driveMotor.getSelectedSensorVelocity(0);
  }

  public override fun getState(): SwerveModuleState {
    return SwerveModuleState(Util.nativeUnitsToMetersPerSecond(getDrive()), getTurn());
  }

  public override fun setDesiredState(desiredState: SwerveModuleState) {
    val state: SwerveModuleState = SwerveModuleState.optimize(desiredState, getTurn());
    val newDriveOutput: Double = DriveConstants.Modules.driveController.calculate(getDrive(), state.speedMetersPerSecond);

    var newTurnOutput: Double = /*if ((Math.abs(desiredState.speedMetersPerSecond)) <= (DriveConstants.Ramsete.maxVelocity * 0.01)) this.lastAngle 
    else*/ DriveConstants.Modules.turnController.calculate(turnEncoder.getPosition(), state.angle.radians);
    this.lastAngle = newTurnOutput;

    driveMotor.set(ControlMode.PercentOutput, newDriveOutput);
    turnMotor.set(newTurnOutput);
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
}