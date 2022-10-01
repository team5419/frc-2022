package frc.robot.commands;
import frc.robot.DriveConstants;
import frc.robot.subsystems.Drivetrain;
import edu.wpi.first.wpilibj2.command.CommandBase;
class SlowMode(_drivetrain: Drivetrain) : CommandBase() {
    private val drivetrain: Drivetrain = _drivetrain;

  override fun initialize() {
    drivetrain.slowMode = true;
  }

  override fun execute() {}

  override fun end(interrupted: Boolean) {
    drivetrain.slowMode = false;
  }

  override fun isFinished(): Boolean {
    return false;
  }
}