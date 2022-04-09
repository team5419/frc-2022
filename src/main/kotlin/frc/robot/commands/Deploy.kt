package frc.robot.commands; 

import frc.robot.subsystems.DeploySubsystem;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.Timer
import frc.robot.IndexerConstants
import frc.robot.classes.SubsystemHolder


class Deploy(_subsystems: SubsystemHolder, _percent: Double = 1.0) : CommandBase() {
  private val subsystems: SubsystemHolder = _subsystems
  private val percent: Double = _percent

  init {
  }

  override fun initialize() {
    subsystems.deploy.runDeploy(percent)
  }

  override fun execute() {
  }

  override fun end(interrupted: Boolean) {
    subsystems.deploy.deployStop()
  }

  // end command if time has elapsed
  override fun isFinished(): Boolean {
    return false
  }

}