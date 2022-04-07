package frc.robot.commands; 

import frc.robot.subsystems.Feeder;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.Timer
import frc.robot.FeederConstants;


class Feed(_feeder: Feeder, _velocity: Double = FeederConstants.idlePercent) : CommandBase() {
  private val feeder: Feeder = _feeder
  private val velocity: Double = _velocity

  init {
    addRequirements(_feeder);
    feeder.currentVel = velocity

  }

  override fun initialize() {
    feeder.currentVel = velocity
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
