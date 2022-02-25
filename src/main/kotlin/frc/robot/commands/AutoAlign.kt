package frc.robot.commands; 

import frc.robot.subsystems.Vision;
import frc.robot.subsystems.Drivetrain
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.XboxController;
import frc.robot.classes.DriveSignal;
import frc.robot.subsystems.Shooter;
import frc.robot.LookupEntry;

class AutoAlign(_vision: Vision, _drivetrain: Drivetrain, _shooter: Shooter) : CommandBase() {
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
    //println("started aligning")
    setpoint = vision.getShotSetpoint();
    shooter.mainVelocity = setpoint.mainVelocity;
    shooter.kickerVelocity = setpoint.kickerVelocity;
  }

  override fun execute() {
    var throttleOutput: DriveSignal = vision.autoAlignThrottle(setpoint.distance);
    var turnOutput: DriveSignal = vision.autoAlignTurn();
    //println("throttle output ${throttleOutput.left}, ${throttleOutput.right}")
    //println("turn output ${turnOutput.left}, ${turnOutput.right}")
    drivetrain.setPercent(throttleOutput.left + turnOutput.left, throttleOutput.right + turnOutput.right)
  }

  override fun end(interrupted: Boolean) {
      //println("done aligning")
      vision.off();
      drivetrain.brakeMode = false;
  }
}
