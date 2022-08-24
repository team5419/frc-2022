package frc.robot.commands; 

import frc.robot.subsystems.Drivetrain;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.XboxController;
import frc.robot.Util;
import frc.robot.DriveConstants;
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
    val lefty: Double = Util.withDeadband(driver.getLeftY().toDouble())
    val leftx: Double = Util.withDeadband(driver.getLeftX().toDouble())
    drivetrain.drive(leftx * DriveConstants.speedMultiplier, -lefty * DriveConstants.speedMultiplier, rightx * -0.1);
    
  }

  override fun end(interrupted: Boolean) {}

  override fun isFinished(): Boolean {
    return false;
  }
}
