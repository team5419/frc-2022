package frc.robot.commands; 

import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Feeder;
import frc.robot.FeederConstants;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.XboxController;
import frc.robot.classes.SubsystemHolder

class Drive(_subsystems: SubsystemHolder, _driver: XboxController, _isSlow: Boolean = false) : CommandBase() {
  private val subsystems: SubsystemHolder = _subsystems
  private val driver: XboxController = _driver;
  private val isSlow: Boolean = _isSlow;

  init {
    addRequirements(_subsystems.drivetrain);
  }

  override fun initialize() {
    subsystems.feeder.currentVel = FeederConstants.idlePercent
  }

  override fun execute() {
    subsystems.drivetrain.drive(driver.getLeftY().toDouble(), driver.getRightX().toDouble(), isSlow);
  }

  override fun end(interrupted: Boolean) {}

  override fun isFinished(): Boolean {
    return false;
  }
}
