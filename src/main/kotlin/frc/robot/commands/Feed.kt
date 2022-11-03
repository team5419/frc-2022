package frc.robot.commands; 

import frc.robot.subsystems.Intake;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.XboxController;

class Feed(_intake: Intake, _reverse: Boolean = false) : CommandBase() {
  private val intake: Intake = _intake;
  private val reverse: Boolean = _reverse;

  init {
    addRequirements(_intake);
  }

  override fun initialize() {
    intake.feedForward()
  }

  override fun execute() {
  }

  override fun end(interrupted: Boolean) {
    intake.stop();
  }

  override fun isFinished(): Boolean {
    return false;
  }
}