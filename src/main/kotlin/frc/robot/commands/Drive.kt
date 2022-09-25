package frc.robot.commands; 

import frc.robot.subsystems.Drivetrain;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.XboxController;
import frc.robot.Util;
import frc.robot.DriveConstants;
import frc.robot.classes.SubsystemHolder

class Drive(_subsystems: SubsystemHolder, _driver: XboxController, _isSlow: Boolean = false) : CommandBase() {
  private val subsystems: SubsystemHolder = _subsystems
  private val driver: XboxController = _driver;
  private val isSlow: Boolean = _isSlow;

  init {
    addRequirements(subsystems.drivetrain);
  }

  override fun initialize() {}

  override fun execute() {
    val rightx: Double = Util.withDeadband(driver.getRightX().toDouble());
    val lefty: Double = Util.withDeadband(driver.getLeftY().toDouble())
    val leftx: Double = Util.withDeadband(driver.getLeftX().toDouble())
    subsystems.drivetrain.drive(lefty * DriveConstants.speedMultiplier, -leftx * DriveConstants.speedMultiplier, rightx * -0.1);
  }

  override fun end(interrupted: Boolean) {}

  override fun isFinished(): Boolean {
    return false;
  }
}