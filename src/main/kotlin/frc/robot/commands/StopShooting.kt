package frc.robot.commands; 

import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Indexer;
import frc.robot.subsystems.Feeder;
import frc.robot.FeederConstants;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.Timer
import frc.robot.commands.Feed


class StopShooting(_shooter: Shooter, _indexer: Indexer, _feeder: Feeder) : CommandBase() {
  private val shooter: Shooter = _shooter;
  private val indexer: Indexer = _indexer;
  private val feeder: Feeder = _feeder;

  init {
      addRequirements(_shooter, _indexer, _feeder)
  }

  override fun initialize() {
  }

  override fun execute() {
  }

  // end command if time has elapsed
  override fun isFinished(): Boolean {
    return true
  }

  override fun end(interrupted: Boolean) {
    if(feeder.idling) {
        Feed(feeder).schedule()
    } else {
        feeder.stop()
    }
    shooter.stop()
    indexer.stop()
  }
}
