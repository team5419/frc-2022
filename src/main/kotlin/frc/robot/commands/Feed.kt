package frc.robot.commands; 

import frc.robot.subsystems.Feeder;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.Timer
import frc.robot.FeederConstants;


class Feed(_feeder: Feeder) : CommandBase() {
  private val feeder: Feeder = _feeder

  init {
    addRequirements(_feeder);
    feeder.idling = true
  }

  override fun execute() {
    feeder.feed(FeederConstants.idlePercent);
  }

  override fun end(interrupted: Boolean) {
    if(!interrupted) {
        feeder.stop()
    }
    feeder.idling = interrupted;
  }

  // end command if time has elapsed
  override fun isFinished(): Boolean {
    return false
  }

}
