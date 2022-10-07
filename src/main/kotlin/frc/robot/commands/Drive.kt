package frc.robot.commands; 

import frc.robot.subsystems.Drivetrain;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.XboxController;
import frc.robot.Util;
import frc.robot.DriveConstants;
import frc.robot.classes.SubsystemHolder
import frc.robot.FeederConstants;

class Drive(_subsystems: SubsystemHolder, _driver: XboxController, _isSlow: Boolean = false) : CommandBase() {
  private val subsystems: SubsystemHolder = _subsystems
  private val driver: XboxController = _driver;
  private val isSlow: Boolean = _isSlow;

  init {
    addRequirements(subsystems.drivetrain);
  }

  override fun initialize() {
  }

  override fun execute() {
    var rightx: Double = Util.withDeadband(driver.getRightX().toDouble());
    var lefty: Double = Util.withDeadband(driver.getLeftY().toDouble())
    var leftx: Double = Util.withDeadband(driver.getLeftX().toDouble());
    subsystems.drivetrain.drive(lefty * DriveConstants.speedMultiplier, leftx * DriveConstants.speedMultiplier, rightx * DriveConstants.turnMultiplier);
  }

  override fun end(interrupted: Boolean) {}

  override fun isFinished(): Boolean {
    return false;
  }
}