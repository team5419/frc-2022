package frc.robot.commands; 

import frc.robot.subsystems.Drivetrain;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.XboxController;
import frc.robot.Util;
//import frc.robot.classes.SubsystemHolder

class Drive(_drivetrain: Drivetrain, _driver: XboxController, _isSlow: Boolean = false) : CommandBase() {
  private val drivetrain: Drivetrain = _drivetrain
  private val driver: XboxController = _driver;
  private val isSlow: Boolean = _isSlow;

  init {
    addRequirements(_drivetrain);
  }

  override fun initialize() {
  }

  override fun execute() {
    val rightx: Double = Util.withDeadband(driver.getRightX().toDouble());
    val righty: Double = Util.withDeadband(driver.getRightY().toDouble());
    val mag: Double = Math.pow(Math.pow(rightx, 2.0) + Math.pow(righty, 2.0), 0.5);
    println("mag = ${mag}");
    val lefty: Double = Util.withDeadband(driver.getLeftY().toDouble())
    val leftx: Double = Util.withDeadband(driver.getLeftX().toDouble())
    if(mag == 0.0) {
      drivetrain.drive(lefty, leftx, 0.0);
      return;
    }
    val angle: Double = if (righty < 0) 2 * Math.PI - Math.acos(rightx / mag) else Math.acos(rightx / mag);
    println("angle = ${angle}")
    drivetrain.drive(lefty, leftx, angle - (drivetrain.angle % (2 * Math.PI)));
    //println("forward: ${lefty}, right: ${leftx}, angle: ${angle}");
    
  }

  override fun end(interrupted: Boolean) {}

  override fun isFinished(): Boolean {
    return false;
  }
}
