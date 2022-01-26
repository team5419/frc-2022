package frc.robot.commands; 

import frc.robot.subsystems.Vision;
import frc.robot.subsystems.Drivetrain
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.XboxController;

class AutoAlign(_vision: Vision, _drivetrain: Drivetrain) : CommandBase() {
  private val vision: Vision = _vision;
  private val drivetrain: Drivetrain = _drivetrain;

  init {
    addRequirements(_vision);
    addRequirements(_drivetrain);
  }

  // Called when the command is initially scheduled.
  override fun initialize() {
    vision.on();
    drivetrain.brakeMode = true;
  }

  // Called every time the scheduler runs while the command is scheduled.
  override fun execute() {
    drivetrain.setPercent(vision.autoAlign().left, vision.autoAlign().right)
  }

  // Called once the command ends or is interrupted.
  override fun end(interrupted: Boolean) {
      vision.off()
      drivetrain.brakeMode = true;
  }
}
