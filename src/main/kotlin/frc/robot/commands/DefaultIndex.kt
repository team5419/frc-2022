package frc.robot.commands; 

import frc.robot.subsystems.Indexer;
import frc.robot.subsystems.Shooter;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.Timer
import frc.robot.IndexerConstants


class DefaultIndex(_indexer: Indexer) : CommandBase() {
  private val indexer: Indexer = _indexer

  init {
    addRequirements(_indexer);
  }

  override fun initialize() {
  }

  override fun execute() {
    if(indexer.atPositionOne() && (!indexer.atPositionThree())) {
        indexer.index(0.3);
    } else {
        indexer.stop();
    }
  }

  override fun end(interrupted: Boolean) {
    indexer.stop()
  }

  // end command if time has elapsed
  override fun isFinished(): Boolean {
    return false
  }

}
