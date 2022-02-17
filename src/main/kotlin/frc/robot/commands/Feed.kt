package frc.robot.commands; 

import frc.robot.subsystems.Feeder;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.Timer
import frc.robot.FeederConstants;


class Feed(_feeder: Feeder) : CommandBase() {
  private val feeder: Feeder = _feeder
  private val previousVel: Double = feeder.currentVel


  init {
    addRequirements(_feeder);
  }

  override fun initialize() {
    feeder.currentVel = FeederConstants.activePercent
  }

  override fun execute() {
    
    }

  override fun end(interrupted: Boolean) {
    feeder.currentVel = previousVel
  }

  // end command if time has elapsed
  override fun isFinished(): Boolean {
    return false
  }

}
