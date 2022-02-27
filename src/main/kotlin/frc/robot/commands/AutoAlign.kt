package frc.robot.commands; 

import frc.robot.subsystems.Vision;
import frc.robot.subsystems.Drivetrain
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.XboxController;
import frc.robot.classes.DriveSignal;
import frc.robot.subsystems.Shooter;
import frc.robot.LookupEntry;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab
import edu.wpi.first.wpilibj.Timer

class AutoAlign(_vision: Vision, _drivetrain: Drivetrain, _shooter: Shooter, _time: Double = 0.0, _throttling: Boolean = true) : CommandBase() {
  private val vision: Vision = _vision;
  private val drivetrain: Drivetrain = _drivetrain;
  private val shooter: Shooter = _shooter;
  private var setpoint: LookupEntry = LookupEntry(0.0, 0.0, 0.0)
  private val time: Double = _time
  private val timer: Timer = Timer()
  private val throttling: Boolean = _throttling

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
    timer.reset()
    timer.start()
  }

  override fun execute() {
    var throttleOutput: DriveSignal = vision.autoAlignThrottle(setpoint.distance);
    var turnOutput: DriveSignal = vision.autoAlignTurn();
    drivetrain.setPercent(throttleOutput.left + turnOutput.left, throttleOutput.right + turnOutput.right)
  }

  override fun isFinished() : Boolean {
    //return timer.get() >= time && time > 0.0 // 2 seconds to align
    return(vision.turnAligned() && vision.throttleAligned(setpoint.distance))
  }

  override fun end(interrupted: Boolean) {
      println("done aligning")
      vision.off();
      timer.stop()
      drivetrain.setPercent(0.0, 0.0)
      drivetrain.brakeMode = false;
  }
}
