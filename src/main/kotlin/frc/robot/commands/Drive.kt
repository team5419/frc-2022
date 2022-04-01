package frc.robot.commands; 

import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Feeder;
import frc.robot.FeederConstants;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.XboxController;

class Drive(_drivetrain: Drivetrain, _driver: XboxController, _isSlow: Boolean = false, _feeder: Feeder) : CommandBase() {
  private val drivetrain: Drivetrain = _drivetrain;
  private val driver: XboxController = _driver;
  private val isSlow: Boolean = _isSlow;
  private val feeder: Feeder = _feeder;

  init {
    addRequirements(_drivetrain);
  }

  override fun initialize() {
    feeder.currentVel = FeederConstants.idlePercent
  }

  override fun execute() {
    drivetrain.drive(driver.getLeftY().toDouble(), driver.getRightX().toDouble(), isSlow);
  }

  override fun end(interrupted: Boolean) {}

  override fun isFinished(): Boolean {
    return false;
  }
}
