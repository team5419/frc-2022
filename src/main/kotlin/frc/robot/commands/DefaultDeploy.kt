package frc.robot.commands; 

import frc.robot.subsystems.DeploySubsystem;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.Timer
import frc.robot.IndexerConstants


class DefaultDeploy(_deploy: DeploySubsystem) : CommandBase() {
  private val deploy: DeploySubsystem = _deploy

  init {
    addRequirements(_deploy);
  }

  override fun initialize() {
  }

  override fun execute() {
      val gotten = deploy.encoder.getPosition()
      println("setpoint: " + deploy.setpointTicks)
      if(Math.abs(gotten - deploy.setpointTicks) < 2.0) {
          return;
      }
      if(gotten < deploy.setpointTicks && deploy.setpointTicks != 0.0) {
          deploy.runDeploy(0.2);
      } else {
          deploy.runDeploy(-0.2);
      }
  }

  override fun end(interrupted: Boolean) {
    deploy.runDeploy(0.0)
  }

  // end command if time has elapsed
  override fun isFinished(): Boolean {
    return false
  }

}