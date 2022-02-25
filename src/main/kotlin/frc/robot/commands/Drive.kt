package frc.robot.commands; 

import frc.robot.subsystems.Drivetrain;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.XboxController;

class Drive(_drivetrain: Drivetrain, _driver: XboxController, _isSlow: Boolean = false) : CommandBase() {
  private val drivetrain: Drivetrain = _drivetrain;
  private val driver: XboxController = _driver;
  private val isSlow: Boolean = _isSlow;

  init {
    addRequirements(_drivetrain);
  }

  override fun initialize() {}

  override fun execute() {
    drivetrain.drive(-1 * driver.getLeftY().toDouble(), driver.getRightX().toDouble() * 0.33, isSlow);
  }

  override fun end(interrupted: Boolean) {}

  override fun isFinished(): Boolean {
    return false;
  }
}
