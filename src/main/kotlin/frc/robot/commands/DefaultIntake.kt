package frc.robot.commands; 

import frc.robot.subsystems.Intake;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.Timer
import frc.robot.IndexerConstants


class DefaultIntake(_intake: Intake) : CommandBase() {
  private val intake: Intake = _intake
  init {
  }

  override fun initialize() {
  }

  override fun execute() {
      val gotten = intake.encoder.getPosition()
      if(Math.abs(gotten - intake.setpointTicks) < 1.0) {
          return;
      }
      if(gotten < intake.setpointTicks) {
          intake.runDeploy(0.5);
      } else {
          intake.runDeploy(-0.5);
      }
  }

  override fun end(interrupted: Boolean) {
    intake.runDeploy(0.0)
  }

  // end command if time has elapsed
  override fun isFinished(): Boolean {
    return false
  }

}