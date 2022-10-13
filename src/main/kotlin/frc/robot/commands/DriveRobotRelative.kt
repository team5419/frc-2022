package frc.robot.commands; 

import frc.robot.subsystems.Drivetrain;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.XboxController;
import frc.robot.Util;
import frc.robot.DriveConstants;
import frc.robot.classes.SubsystemHolder
import frc.robot.FeederConstants;
import kotlin.math.pow;

class DriveRobotRelative(_subsystems: SubsystemHolder, _forward: Double, _left: Double, _dist: Double) : CommandBase() {
  private val subsystems: SubsystemHolder = _subsystems
  private val forward: Double = _forward;
  private val left: Double = _left;
  private var startX: Double = 0.0;
  private var startY: Double = 0.0;
  private var dist: Double = _dist;

  init {
    addRequirements(subsystems.drivetrain);
  }

  override fun initialize() {
    val pose = subsystems.drivetrain.pose();
    this.startX = pose.getX();
    this.startY = pose.getY();
  }

  override fun execute() {
    subsystems.drivetrain.drive(forward, left, 0.0, false, true);
  }

  override fun end(interrupted: Boolean) {
    subsystems.drivetrain.stop();
  }

  override fun isFinished(): Boolean {
    val pose = subsystems.drivetrain.pose();
    val currentDist = ((pose.getX() - startX).pow(2.0) + (pose.getY() - startY).pow(2.0)).pow(0.5);
    return currentDist > dist;
  }
}