package frc.robot.commands; 

import frc.robot.subsystems.DeploySubsystem;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.Timer
import frc.robot.IndexerConstants


class Deploy(_deploy: DeploySubsystem, _percent: Double = 1.0) : CommandBase() {
  private val deploy: DeploySubsystem = _deploy
  private val percent: Double = _percent

  init {
  }

  override fun initialize() {
    deploy.runDeploy(percent)
  }

  override fun execute() {
  }

  override fun end(interrupted: Boolean) {
    deploy.deployStop()
  }

  // end command if time has elapsed
  override fun isFinished(): Boolean {
    return false
  }

}