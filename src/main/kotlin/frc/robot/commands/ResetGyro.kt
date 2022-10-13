package frc.robot.commands;
import frc.robot.DriveConstants;
import frc.robot.subsystems.Drivetrain;
import edu.wpi.first.wpilibj2.command.CommandBase;
class ResetGyro(_drivetrain: Drivetrain, _angle: Double = 0.0) : CommandBase() {
    private val drivetrain: Drivetrain = _drivetrain;
    private val angle: Double = _angle;

  init {
  }

  override fun initialize() {}

  override fun execute() {}

  override fun end(interrupted: Boolean) {
    drivetrain.gyro.setYaw(angle);
  }

  override fun isFinished(): Boolean {
    return true;
  }
}