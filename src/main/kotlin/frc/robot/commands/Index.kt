package frc.robot.commands; 

import frc.robot.subsystems.Indexer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.Timer
import frc.robot.IndexerConstants


class Index(_indexer: Indexer) : CommandBase() {
  private val indexer: Indexer = _indexer
  private var startingPosition: Double = 0.0

  init {
    addRequirements(_indexer);
  }

  override fun initialize() {
    startingPosition = indexer.encoder.getPosition();
    indexer.index();
  }

  override fun execute() {
    
  }

  override fun end(interrupted: Boolean) {
    indexer.stop()
  }

  // end command if time has elapsed
  override fun isFinished(): Boolean {
    return indexer.encoder.getPosition() - startingPosition >= IndexerConstants.ticksPerIndex
  }

}
