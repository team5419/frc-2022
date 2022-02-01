package frc.robot.commands; 

import frc.robot.subsystems.Vision;
import frc.robot.subsystems.Drivetrain
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.XboxController;
import frc.robot.classes.DriveSignal;
import frc.robot.subsystems.Shooter;

class AutoAlign(_vision: Vision, _drivetrain: Drivetrain, _shooter: Shooter) : CommandBase() {
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
    println("started aligning")
  }

  override fun execute() {
    // get closest shot setpoint
    val setpoint = vision.getShotSetpoint();
    // set the velocities of the shooting motors based on shot setpoint
    shooter.mainVelocity = setpoint.mainVelocity;
    shooter.kickerVelocity = setpoint.kickerVelocity;
    // use pid loops to calculate throttle and turn output to autoalign
    var throttleOutput = vision.autoAlignThrottle(1.2);
    var turnOutput = vision.autoAlignTurn();
    // set drivetrain velocity to integrate throttle and turn outputs
    var output = DriveSignal(throttleOutput.left + turnOutput.left, throttleOutput.right + turnOutput.right)
    drivetrain.setPercent(output.left, output.right)
    println("output ${output.left}, ${output.right}")
  }

  override fun end(interrupted: Boolean) {
      println("done aligning")
      vision.off();
      drivetrain.brakeMode = false;
  }
}
