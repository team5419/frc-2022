package frc.robot.commands; 

import frc.robot.subsystems.Indexer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.Timer
import frc.robot.IndexerConstants


class Index(_indexer: Indexer) : CommandBase() {
  private val indexer: Indexer = _indexer
  private val startingPosition: Double = _indexer.encoder.getPosition()

  init {
    addRequirements(_indexer);
  }

  override fun execute() {
    indexer.index();
  }

  override fun end(interrupted: Boolean) {
    indexer.stop()
  }

  // end command if time has elapsed
  override fun isFinished(): Boolean {
    return indexer.encoder.getPosition() - startingPosition >= IndexerConstants.ticksPerIndex
  }

}
