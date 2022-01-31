package frc.robot.commands; 

import frc.robot.subsystems.Vision;
import frc.robot.subsystems.Drivetrain
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.XboxController;
import frc.robot.classes.DriveSignal;
import frc.robot.subsystems.Shooter;

class AutoAlignTurn(_vision: Vision, _drivetrain: Drivetrain, _shooter: Shooter) : CommandBase() {
  private val vision: Vision = _vision;
  private val drivetrain: Drivetrain = _drivetrain;
  private val shooter: Shooter = _shooter;
  private var isThrottling: Boolean = false;

  init {
    addRequirements(_vision);
    addRequirements(_drivetrain);
  }

  override fun initialize() {
    vision.on();
    drivetrain.brakeMode = true;
  }

  override fun execute() {
    var output: DriveSignal;
    if(isThrottling) {
      val setpoint = vision.getShotSetpoint();
      shooter.defaultVelocity = setpoint.velocity;
      output = vision.autoAlignThrottle(setpoint.distance);
    } else {
      output = vision.autoAlignTurn();
      if(output.left == 0.0 && output.right == 0.0) {
        isThrottling = true;
      }
    }
    drivetrain.setPercent(output.left, output.right)
  }

  override fun end(interrupted: Boolean) {
      vision.off();
      drivetrain.brakeMode = false;
  }
}
