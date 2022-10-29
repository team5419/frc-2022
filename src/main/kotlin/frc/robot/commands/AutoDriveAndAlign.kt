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
import edu.wpi.first.math.geometry.Pose2d;

class AutoDriveAndAlign(_subsystems: SubsystemHolder, _driver: XboxController, _x: Double, _y: Double) : CommandBase() {
  private val subsystems: SubsystemHolder = _subsystems
  private val driver: XboxController = _driver;
  private var setpoint: LookupEntry = LookupEntry(0.0, 0.0, 0.0, RGB(0, 0, 0))
  private val x: Double = _x;
  private val y: Double = _y;

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
  }

  override fun execute() {
    val pose: Pose2d = subsystems.drivetrain.pose();
    var turnOutput: Double = subsystems.vision.autoAlignTurn();

    subsystems.drivetrain.drive(
        Math.min(DriveConstants.pXY * Util.withDeadband((x - pose.getX()), DriveConstants.epsilonXY), DriveConstants.SwerveRamsete.maxVelocity),
        Math.min(DriveConstants.pXY * Util.withDeadband((y - pose.getY()), DriveConstants.epsilonXY), DriveConstants.SwerveRamsete.maxVelocity), 
        turnOutput, true, true);
    
  }

  override fun isFinished(): Boolean {
    val pose: Pose2d = subsystems.drivetrain.pose();
    return subsystems.vision.turnAligned() && subsystems.drivetrain.getAverageSpeed() < 0.1 && Math.abs(pose.getX() - x) < DriveConstants.epsilonXY && Math.abs(pose.getY() - y) < DriveConstants.epsilonXY;
  }

  override fun end(interrupted: Boolean) {
      println("done aligning")
      subsystems.vision.off();
      subsystems.lights.setColor(RGB(0, 0, 0));
      subsystems.drivetrain.stop();
      subsystems.drivetrain.brakeMode = false;
  }
}
