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
    println("Driving");
    val rightx: Double = Util.withDeadband(driver.getRightX().toDouble());
    val righty: Double = Util.withDeadband(driver.getRightY().toDouble());
    var angle: Double = if (righty < 0) -Math.acos(rightx) else Math.acos(rightx);
    drivetrain.drive(Util.withDeadband(driver.getLeftY().toDouble()), Util.withDeadband(driver.getLeftX().toDouble()), angle);
  }

  override fun end(interrupted: Boolean) {}

  override fun isFinished(): Boolean {
    return false;
  }
}
