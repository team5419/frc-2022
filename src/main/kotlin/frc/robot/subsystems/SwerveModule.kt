package frc.robot.subsystems;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.StatusFrame;
import com.ctre.phoenix.motorcontrol.ControlFrame;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import edu.wpi.first.math.geometry.Rotation2d;
import com.ctre.phoenix.motorcontrol.DemandType;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import frc.robot.Util;
import frc.robot.DriveConstants;

public class SwerveModule {
    private val driveMotor: TalonFX;
    private val turnMotor: TalonFX;
    private var lastAngle: Double;
    constructor(drivePort: Int, turnPort: Int, driveInverted: Boolean = false, turnInverted: Boolean = false) {
        this.driveMotor = TalonFX(drivePort);
        this.turnMotor = TalonFX(turnPort);
        this.turnMotor.apply {
            configFactoryDefault(100)
            configSupplyCurrentLimit(SupplyCurrentLimitConfiguration(true, 40.0, 0.0, 0.0), 100)

            setSensorPhase(false)
            setInverted(turnInverted)
            config_kP( 0, DriveConstants.TurnPID.P , 100 )
            config_kI( 0, DriveConstants.TurnPID.I , 100 )
            config_kD( 0, DriveConstants.TurnPID.D , 100 )
            config_kF( 0, 0.0 , 100 )

            setSelectedSensorPosition(0.0, 0, 100)

            configVoltageCompSaturation(12.0, 100)
            enableVoltageCompensation(true)
            setStatusFramePeriod(StatusFrame.Status_2_Feedback0, 10, 100)
            setControlFramePeriod(ControlFrame.Control_3_General, 10)

            setNeutralMode(NeutralMode.Coast)

            configClosedLoopPeakOutput(0, 0.1, 100)
        }
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
        this.lastAngle = getTurn().radians;
    }

    private fun getTurn(): Rotation2d {
        return Rotation2d(Util.nativeUnitsToRadians(turnMotor.getSelectedSensorPosition(0)));
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
    turnMotor.set(ControlMode.Position, turnOutput);
  }

  public fun resetEncoders() {
    driveMotor.setSelectedSensorPosition(0.0);
    turnMotor.setSelectedSensorPosition(0.0);
  }

  public fun setBrakeMode(on: Boolean = true) {
    val mode = (if (on) NeutralMode.Brake else NeutralMode.Coast);
    driveMotor.setNeutralMode(mode);
    turnMotor.setNeutralMode(mode); 
  }
}