package frc.robot.commands; 

import frc.robot.subsystems.Intake;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.Timer
import frc.robot.IndexerConstants


class Deploy(_intake: Intake, _percent: Double = 1.0) : CommandBase() {
  private val intake: Intake = _intake
  private val percent: Double = _percent
  init {
  }

  override fun initialize() {
    intake.runDeploy(percent)
  }

  override fun execute() {
  }

  override fun end(interrupted: Boolean) {
    intake.deployStop()
  }

  // end command if time has elapsed
  override fun isFinished(): Boolean {
    return false
  }

}