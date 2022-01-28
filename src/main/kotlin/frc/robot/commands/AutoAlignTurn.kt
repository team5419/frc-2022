package frc.robot.commands; 

import frc.robot.subsystems.Vision;
import frc.robot.subsystems.Drivetrain
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.XboxController;

import frc.robot.commands.AutoAlignThrottle;

class AutoAlignTurn(_vision: Vision, _drivetrain: Drivetrain) : CommandBase() {
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
    drivetrain.setPercent(vision.autoAlignTurn().left, vision.autoAlignTurn().right)
  }

  override fun end(interrupted: Boolean) {
      vision.off()
      drivetrain.brakeMode = true;
      AutoAlignThrottle(vision, drivetrain)
  }
}
