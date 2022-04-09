package frc.robot.commands; 

import frc.robot.subsystems.Feeder;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.Timer
import frc.robot.FeederConstants;
import frc.robot.classes.SubsystemHolder

class Feed(_subsystems: SubsystemHolder, _velocity: Double = FeederConstants.idlePercent) : CommandBase() {
  private val subsystems: SubsystemHolder = _subsystems
  private val velocity: Double = _velocity

  init {
    addRequirements(_subsystems.feeder);
    subsystems.feeder.currentVel = velocity

  }

  override fun initialize() {
    subsystems.feeder.currentVel = velocity
  }

  override fun execute() {
    //feeder.feed()
  }

  override fun end(interrupted: Boolean) {
  }

  // end command if time has elapsed
  override fun isFinished(): Boolean {
    return false
  }

}
