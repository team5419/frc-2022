package frc.robot.commands; 

import frc.robot.subsystems.DeploySubsystem;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.Timer
import frc.robot.IndexerConstants
import frc.robot.classes.SubsystemHolder


class Deploy(_subsystems: SubsystemHolder, _percent: Double = 1.0, _time: Double = 0.0) : CommandBase() {
  private val subsystems: SubsystemHolder = _subsystems
  private val percent: Double = _percent
  private val time: Double = _time
  private val timer: Timer = Timer()

  init {
  }

  override fun initialize() {
    println("deploying")
    subsystems.deploy.runDeploy(percent)
    timer.reset()
    timer.start()
  }

  override fun execute() {
  }

  override fun end(interrupted: Boolean) {
    subsystems.deploy.deployStop()
  }

  // end command if time has elapsed
  override fun isFinished(): Boolean {
    return time != 0.0 && timer.get() > time
  }

}