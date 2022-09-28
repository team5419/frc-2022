package frc.robot.commands;
import frc.robot.DriveConstants;
import frc.robot.subsystems.Drivetrain;
import edu.wpi.first.wpilibj2.command.CommandBase;
class ResetGyro(_drivetrain: Drivetrain) : CommandBase() {
    private val drivetrain: Drivetrain = _drivetrain;

  init {
  }

  override fun initialize() {}

  override fun execute() {}

  override fun end(interrupted: Boolean) {
    drivetrain.gyro.setYaw(0.0);
  }

  override fun isFinished(): Boolean {
    return true;
  }
}