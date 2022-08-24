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
import frc.robot.commands.Drive;
import edu.wpi.first.wpilibj.Encoder;
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

public class SimulatedSwerveModule : ISwerveModule {
    private val driveMotor: FlywheelSim;
    private val turnMotor: FlywheelSim;
    private val driveBaseEncoder: Encoder;
    private val turnBaseEncoder: Encoder;
    private val driveEncoder: EncoderSim;
    private val turnEncoder: EncoderSim;
    private var turnOutput: Double;
    private var driveOutput: Double;
    private var driveDistance: Double;
    private var turnDistance: Double;
    constructor(fakeDrivePortA: Int, fakeDrivePortB: Int, fakeTurnPortA: Int, fakeTurnPortB: Int) {
        this.driveMotor = FlywheelSim(
            LinearSystemId.identifyVelocitySystem(DriveConstants.Ramsete.kv, DriveConstants.Ramsete.ka),
            DriveConstants.driveMotorGearbox,
            DriveConstants.driveMotorGearRatio
        );
        this.turnMotor = FlywheelSim(
            LinearSystemId.identifyVelocitySystem(DriveConstants.kvRadians, DriveConstants.kaRadians),
            DriveConstants.turnMotorGearbox,
            DriveConstants.turnMotorGearRatio
        );
        this.driveBaseEncoder = Encoder(fakeDrivePortA, fakeDrivePortB);
        this.turnBaseEncoder = Encoder(fakeTurnPortA, fakeTurnPortB);
        this.driveBaseEncoder.setDistancePerPulse(DriveConstants.Modules.kDriveEncoderDistancePerPulse);
        this.turnBaseEncoder.setDistancePerPulse(DriveConstants.Modules.kTurningEncoderDistancePerPulse);
        this.driveEncoder = EncoderSim(this.driveBaseEncoder);
        this.turnEncoder = EncoderSim(this.turnBaseEncoder);
        this.turnOutput = 0.0;
        this.driveOutput = 0.0;
        this.driveDistance = 0.0;
        this.turnDistance = 0.0;
    }

  public override fun getTurn(): Rotation2d {
        return Rotation2d(turnBaseEncoder.getDistance());
  }

  public override fun getState(): SwerveModuleState {
    val returning: SwerveModuleState =  SwerveModuleState(driveBaseEncoder.getRate(), getTurn());
    //println("state: drive: ${returning.speedMetersPerSecond}, angle: ${returning.angle}");
    return returning;
  }

  public override fun setDesiredState(desiredState: SwerveModuleState) {
    val state: SwerveModuleState = SwerveModuleState.optimize(desiredState, getTurn());
    val newDriveOutput: Double = DriveConstants.Modules.driveController.calculate(driveBaseEncoder.getRate(), state.speedMetersPerSecond);
  
    var newTurnOutput: Double = /*if ((Math.abs(desiredState.speedMetersPerSecond)) <= (DriveConstants.Ramsete.maxVelocity * 0.01)) this.turnOutput 
    else */ DriveConstants.Modules.turnController.calculate(turnBaseEncoder.getDistance(), state.angle.radians);

    this.driveOutput = newDriveOutput;
    this.turnOutput = newTurnOutput;
  }

  public override fun simulationPeriodic(dt: Double) {
    turnMotor.setInputVoltage(turnOutput / DriveConstants.Modules.kMaxModuleAngularSpeedRadiansPerSecond * RobotController.getBatteryVoltage());
    driveMotor.setInputVoltage(driveOutput / DriveConstants.Ramsete.maxVelocity * RobotController.getBatteryVoltage());

    turnMotor.update(dt);
    driveMotor.update(dt);

    val turnangvel: Double = turnMotor.getAngularVelocityRadPerSec();
    this.turnDistance += turnangvel * dt;
    turnEncoder.setDistance(this.turnDistance);
    turnEncoder.setRate(turnangvel);

    val driveangvel: Double = driveMotor.getAngularVelocityRadPerSec();
    this.driveDistance += driveangvel * 0.02;
    driveEncoder.setDistance(this.driveDistance);
    driveEncoder.setRate(driveangvel);
  }

  public override fun resetEncoders() {
    driveBaseEncoder.reset();
    turnBaseEncoder.reset();
  }

  public override fun setBrakeMode(on: Boolean) {}
}