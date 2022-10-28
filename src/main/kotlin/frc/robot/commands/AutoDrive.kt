package frc.robot.commands; 

import frc.robot.subsystems.Drivetrain;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.XboxController;
import frc.robot.Util;
import frc.robot.DriveConstants;
import frc.robot.classes.SubsystemHolder
import frc.robot.FeederConstants;
import kotlin.math.pow;
import edu.wpi.first.math.geometry.Pose2d;

class AutoDrive(_subsystems: SubsystemHolder, _x: Double, _y: Double, _rotation: Double) : CommandBase() { // meters, meters, degrees
  private val subsystems: SubsystemHolder = _subsystems
  private val x: Double = _x;
  private val y: Double = _y;
  private var rotation: Double = _rotation;

  init {
    addRequirements(subsystems.drivetrain);
  }

  override fun initialize() {
  }

  override fun execute() {
    val theta: Double = subsystems.drivetrain.angle;
    val target: Double = Math.round((theta - rotation) / 360) * 360 + rotation;
    val pose: Pose2d = subsystems.drivetrain.pose();

    println("theta: ${DriveConstants.pTheta * (Math.PI / 180) * (target - theta)}");
    subsystems.drivetrain.drive(Math.min(DriveConstants.pXY * Util.withDeadband((x - pose.getX()), DriveConstants.epsilonXY), DriveConstants.SwerveRamsete.maxVelocity),
    Math.min(DriveConstants.pXY * Util.withDeadband((y - pose.getY()), DriveConstants.epsilonXY), DriveConstants.SwerveRamsete.maxVelocity), 
    -1 * DriveConstants.pTheta * (Math.PI / 180) * Util.withDeadband((target - theta), DriveConstants.epsilonTheta), 
    true, true);
  }

  override fun end(interrupted: Boolean) {
    println("finished");
    subsystems.drivetrain.stop();
  }

  override fun isFinished(): Boolean {
    val pose: Pose2d = subsystems.drivetrain.pose();
    val theta: Double = subsystems.drivetrain.angle;
    val target: Double = Math.round((theta - rotation) / 360) * 360 + rotation;
    return Math.abs(pose.getX() - x) < DriveConstants.epsilonXY && Math.abs(pose.getY() - y) < DriveConstants.epsilonXY && Math.abs(target - theta) < DriveConstants.epsilonTheta;
  }
}