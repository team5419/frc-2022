package frc.robot.commands; 

import frc.robot.subsystems.Indexer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.Timer
import frc.robot.IndexerConstants
import frc.robot.subsystems.Shooter;


class Index(_indexer: Indexer, _shooter: Shooter) : CommandBase() {
  private val indexer: Indexer = _indexer
  private val shooter: Shooter = _shooter
  private var startingPosition: Double = 0.0
  init {
    addRequirements(_indexer);
  }

  override fun initialize() {
    startingPosition = indexer.encoder.getPosition()
  }

  override fun execute() {
    println("setting indexer !!!");
    if(shooter.isSpedUp()) {
      indexer.index(0.5);
    } else {
      indexer.index(0.0);
    }
    //indexer.index(0.4);
  }

  override fun end(interrupted: Boolean) {
    indexer.stop()
  }

  // end command if time has elapsed
  override fun isFinished(): Boolean {
    //return indexer.encoder.getPosition() - startingPosition >= IndexerConstants.ticksPerIndex
    return false
  }

}