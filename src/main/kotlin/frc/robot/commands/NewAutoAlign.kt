package frc.robot.commands; 

import frc.robot.subsystems.Drivetrain;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.XboxController;
import frc.robot.Util;
import frc.robot.classes.RGB;
import frc.robot.LookupEntry;
import edu.wpi.first.wpilibj.Timer
import frc.robot.DriveConstants;
import frc.robot.classes.SubsystemHolder
import frc.robot.FeederConstants;
import kotlin.math.pow;
import edu.wpi.first.math.geometry.Pose2d;

class NewAutoAlign(_subsystems: SubsystemHolder, _driver: XboxController, _time: Double = 0.0) : CommandBase() { // meters, meters, degrees
  private val subsystems: SubsystemHolder = _subsystems
  private val time: Double = _time
  private val timer: Timer = Timer()
  private var rotation: Double = 0.0;
  private var sawTarget: Boolean = false;
  private val driver: XboxController = _driver;

  init {
    addRequirements(subsystems.drivetrain);
  }

  override fun initialize() {
    subsystems.vision.on();
    val setpoint: LookupEntry = subsystems.vision.getShotSetpoint();
    subsystems.shooter.mainVelocity = setpoint.mainVelocity;
    subsystems.shooter.kickerVelocity = setpoint.kickerVelocity;
    subsystems.shooter.currentColor = setpoint.color;
    subsystems.lights.setColor(setpoint.color);
    timer.reset()
    timer.start()
    sawTarget = false;
    val pose: Pose2d = subsystems.drivetrain.pose();
    rotation = Math.atan(pose.getY() / pose.getX()) * 180 / Math.PI;
    if(pose.getX() > 0) {
      //rotation += 180;
    }
    println("theta ${rotation}");
  }

  override fun execute() {
    val theta: Double = subsystems.drivetrain.angle;
    val target: Double = Math.round((theta - rotation) / 360) * 360 + rotation;

    if(subsystems.vision.isTargetFound() && !sawTarget) {
      sawTarget = true;
    }

    var lefty: Double = Util.withDeadband(driver.getLeftY().toDouble())
    var leftx: Double = Util.withDeadband(driver.getLeftX().toDouble());
    subsystems.drivetrain.drive(lefty * DriveConstants.speedMultiplier, leftx * DriveConstants.speedMultiplier, if (sawTarget)
      subsystems.vision.autoAlignTurn()
      else -1 * DriveConstants.pTheta * (Math.PI / 180) * Util.withDeadband((target - theta), DriveConstants.epsilonTheta)
    , true, true);

    // println("theta: ${DriveConstants.pTheta * (Math.PI / 180) * (target - theta)}");
    // println("already saw target: ${sawTarget}");
    // subsystems.drivetrain.drive(Math.min(DriveConstants.pXY * Util.withDeadband((x - pose.getX()), DriveConstants.epsilonXY), DriveConstants.SwerveRamsete.maxVelocity),
    // Math.min(DriveConstants.pXY * Util.withDeadband((y - pose.getY()), DriveConstants.epsilonXY), DriveConstants.SwerveRamsete.maxVelocity), 
    // if(sawTarget) subsystems.vision.autoAlignTurn() 
    // else -1 * DriveConstants.pTheta * (Math.PI / 180) * Util.withDeadband((target - theta), DriveConstants.epsilonTheta), 
    // true, true);
  }

  override fun end(interrupted: Boolean) {
      println("done aligning")
      subsystems.vision.off();
      timer.stop()
      subsystems.lights.setColor(RGB(0, 0, 0));
      subsystems.drivetrain.stop();
      subsystems.drivetrain.brakeMode = false;
  }

  override fun isFinished() : Boolean {
    return time != 0.0 && (timer.get() > time || (timer.get() > 0.2 && subsystems.vision.turnAligned()))
  }
}