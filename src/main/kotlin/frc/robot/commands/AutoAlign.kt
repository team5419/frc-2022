package frc.robot.commands; 

import frc.robot.subsystems.Vision;
import frc.robot.subsystems.Drivetrain
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.XboxController;
import frc.robot.subsystems.Shooter;
import frc.robot.LookupEntry;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab
import edu.wpi.first.wpilibj.Timer
import frc.robot.classes.RGB;
import frc.robot.subsystems.Lights;
import frc.robot.classes.SubsystemHolder;
import frc.robot.Util;
import frc.robot.DriveConstants;

class AutoAlign(_subsystems: SubsystemHolder, _driver: XboxController, _time: Double = 0.0, _throttling: Boolean = true) : CommandBase() {
  private val subsystems: SubsystemHolder = _subsystems
  private val driver: XboxController = _driver;
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
    var throttleOutput: Double = 0.0;
    if(throttling) {
      throttleOutput = subsystems.vision.autoAlignThrottle(setpoint.distance)
    }
    var turnOutput: Double = subsystems.vision.autoAlignTurn();
    println("align throttle: ${throttleOutput}, turn: ${turnOutput}");

    var lefty: Double = Util.withDeadband(driver.getLeftY().toDouble())
    var leftx: Double = Util.withDeadband(driver.getLeftX().toDouble());
    subsystems.drivetrain.drive(lefty * DriveConstants.speedMultiplier, leftx * DriveConstants.speedMultiplier, turnOutput, true, true);

    //subsystems.drivetrain.drive(/*throttleOutput*/0.0, 0.0, turnOutput, false, true)
    
  }

  override fun isFinished() : Boolean {
    return time != 0.0 && (timer.get() > time || (timer.get() > 0.2 && subsystems.vision.turnAligned() && ((!throttling) || subsystems.vision.throttleAligned(setpoint.distance))))
  }

  override fun end(interrupted: Boolean) {
      println("done aligning")
      subsystems.vision.off();
      timer.stop()
      subsystems.lights.setColor(RGB(0, 0, 0));
      subsystems.drivetrain.stop();
      subsystems.drivetrain.brakeMode = false;
  }
}
