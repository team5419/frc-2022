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
import frc.robot.classes.SubsystemHolder

class StartFeeding(_subsystems: SubsystemHolder, _percent: Double = FeederConstants.idlePercent)  : CommandBase() {
  private val subsystems: SubsystemHolder = _subsystems
  private val percent: Double = _percent

  init {
  }

  override fun initialize() {
    subsystems.feeder.currentVel = percent;
  }

  override fun execute() {
  }

  override fun isFinished(): Boolean {
    return true;
  }

  override fun end(interrupted: Boolean) {
  }
}
