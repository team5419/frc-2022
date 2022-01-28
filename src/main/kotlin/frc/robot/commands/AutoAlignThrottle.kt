package frc.robot.commands; 

import frc.robot.subsystems.Vision;
import frc.robot.subsystems.Drivetrain
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.XboxController;

class AutoAlignThrottle(_vision: Vision, _drivetrain: Drivetrain) : CommandBase() {
  private val vision: Vision = _vision;
  private val drivetrain: Drivetrain = _drivetrain;

  init {
    addRequirements(_vision);
    addRequirements(_drivetrain);
  }

  override fun initialize() {
    vision.on();
    drivetrain.brakeMode = true;
  }

  override fun execute() {
    drivetrain.setPercent(vision.autoAlignThrottle(1.0).left, vision.autoAlignThrottle(1.0).right)
  }

  override fun end(interrupted: Boolean) {
      vision.off()
      drivetrain.brakeMode = true;
  }
}