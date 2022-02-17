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
  }

  override fun execute() {
    feeder.feed();
  }

  override fun end(interrupted: Boolean) {
  }

  // end command if time has elapsed
  override fun isFinished(): Boolean {
    return false
  }

}
