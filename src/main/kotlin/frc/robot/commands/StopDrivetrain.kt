package frc.robot.commands;
import frc.robot.DriveConstants;
import frc.robot.subsystems.Drivetrain;
import edu.wpi.first.wpilibj2.command.CommandBase;
class StopDrivetrain(_drivetrain: Drivetrain) : CommandBase() {
    private val drivetrain: Drivetrain = _drivetrain;

  init {
    addRequirements(_drivetrain);
  }

  override fun initialize() {}

  override fun execute() {}

  override fun end(interrupted: Boolean) {
    drivetrain.drive(0.0, 0.0, 0.0);
  }

  override fun isFinished(): Boolean {
    return true;
  }
}