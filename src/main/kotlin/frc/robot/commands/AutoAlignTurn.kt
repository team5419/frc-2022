package frc.robot.commands; 

import frc.robot.subsystems.Vision;
import frc.robot.subsystems.Drivetrain
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.XboxController;
import frc.robot.classes.DriveSignal;
import frc.robot.subsystems.Shooter;
import frc.robot.LookupEntry;

class AutoAlignTurn(_vision: Vision, _drivetrain: Drivetrain, _shooter: Shooter) : CommandBase() {
  private val vision: Vision = _vision;
  private val drivetrain: Drivetrain = _drivetrain;
  private val shooter: Shooter = _shooter;
  private var setpoint: LookupEntry = LookupEntry(0.0, 0.0, 0.0)

  init {
    addRequirements(_vision);
    addRequirements(_drivetrain);
  }

  override fun initialize() {
    vision.on();
    drivetrain.brakeMode = true;
    println("started aligning")
    setpoint = vision.getShotSetpoint();
    shooter.mainVelocity = setpoint.mainVelocity;
    shooter.kickerVelocity = setpoint.kickerVelocity;
  }

  override fun execute() {
    var throttleOutput: DriveSignal = vision.autoAlignThrottle(1.2 /*setpoint.distance*/);
    println("throttle output ${throttleOutput.left}, ${throttleOutput.right}")
    //drivetrain.setPercent(throttleOutput.left, throttleOutput.right)
  }

  override fun end(interrupted: Boolean) {
      println("done aligning")
      vision.off();
      drivetrain.brakeMode = false;
  }
}
