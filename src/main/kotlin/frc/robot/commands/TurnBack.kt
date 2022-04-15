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
import frc.robot.classes.RGB;
import frc.robot.subsystems.Lights;
import frc.robot.classes.SubsystemHolder;

class TurnBack(_subsystems: SubsystemHolder, _time: Double = 0.0, _throttling: Boolean = true) : CommandBase() {
  private val subsystems: SubsystemHolder = _subsystems
  private var setpoint: LookupEntry = LookupEntry(0.0, 0.0, 0.0, RGB(0, 0, 0))
  private val time: Double = _time
  private val timer: Timer = Timer()
  private val throttling: Boolean = _throttling

  init {
    addRequirements(_subsystems.vision);
    addRequirements(_subsystems.drivetrain);
  }

  override fun initialize() {
    subsystems.vision.on();
    subsystems.drivetrain.brakeMode = true;
    //println("started aligning")
    setpoint = subsystems.vision.getShotSetpoint();
    subsystems.shooter.mainVelocity = setpoint.mainVelocity;
    subsystems.shooter.kickerVelocity = setpoint.kickerVelocity;
    subsystems.shooter.currentColor = setpoint.color;
    subsystems.lights.setColor(setpoint.color);
    timer.reset()
    timer.start()
  }

  override fun execute() {

    println("setpoint distance = ${setpoint.distance}");
    //println("actual distance = ${vision.getHorizontalDistance()}");
    var throttleOutput: DriveSignal = DriveSignal(0.0, 0.0)
    if(throttling) {
      throttleOutput = subsystems.vision.autoAlignThrottle(setpoint.distance)
    }
    var turnOutput: DriveSignal = subsystems.vision.autoAlignTurn();
    println("SETPOINT LEFT ${throttleOutput.left + turnOutput.left}");
    subsystems.drivetrain.setPercent( -turnOutput.left, -turnOutput.right)
  }

  override fun isFinished() : Boolean {
    return time != 0.0 && (timer.get() > time || (timer.get() > 0.2 && subsystems.vision.turnAligned() && ((!throttling) || subsystems.vision.throttleAligned(setpoint.distance))))
  }

  override fun end(interrupted: Boolean) {
      println("done aligning")
      subsystems.vision.off();
      timer.stop()
      subsystems.lights.setColor(RGB(0, 0, 0));
      subsystems.drivetrain.setPercent(0.0, 0.0)
      subsystems.drivetrain.brakeMode = false;
  }
}
