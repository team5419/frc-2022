package frc.robot.commands; 

import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Indexer;
import frc.robot.subsystems.Feeder;
import frc.robot.FeederConstants;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.subsystems.Lights;
import frc.robot.classes.RGB;

class StartFeeding(_feeder: Feeder)  : CommandBase() {
  private val feeder: Feeder = _feeder;

  init {
  }

  override fun initialize() {
    feeder.currentVel = FeederConstants.idlePercent;
  }

  override fun execute() {
  }

  override fun isFinished(): Boolean {
    return true;
  }

  override fun end(interrupted: Boolean) {
  }
}
