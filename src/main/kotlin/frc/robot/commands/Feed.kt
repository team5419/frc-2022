package frc.robot.commands; 

import frc.robot.subsystems.Intake;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.XboxController;

class Feed(_intake: Intake, _driver: XboxController) : CommandBase() {
  private val intake: Intake = _intake;
  private val codriver: XboxController = _driver;

  init {
    addRequirements(_intake);
  }

  override fun initialize() {}

  override fun execute() {
    intake.feedForward()
    //println("trying to climb")
  }

  override fun end(interrupted: Boolean) {
    intake.stop();
  }

  override fun isFinished(): Boolean {
    return false;
  }
}