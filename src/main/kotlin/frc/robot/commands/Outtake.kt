package frc.robot.commands; 

import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Indexer;
import frc.robot.subsystems.Feeder;
import frc.robot.FeederConstants;
import frc.robot.IndexerConstants;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.Timer;

class Outtake(_feeder: Feeder, _indexer: Indexer, _intake: Intake)  : CommandBase() {
  private val feeder: Feeder = _feeder
  private val indexer: Indexer = _indexer
  private val intake: Intake = _intake
  private val previousVel: Double = feeder.currentVel
  private var startingPosition: Double = 0.0

  init {
    addRequirements(_indexer)
    addRequirements(_intake)
  }

  override fun initialize() {
    startingPosition = indexer.encoder.getPosition()
    feeder.currentVel = -0.9; //FeederConstants.reversePercent
    intake.reverse()
  }

  override fun execute() {
    // if(Math.abs(indexer.encoder.getPosition() - startingPosition) < IndexerConstants.ticksPerIndex) {
    //     indexer.index(-1.0)
    // } else {
    //     indexer.stop();
    // }
  }

  override fun isFinished(): Boolean {
    return false
  }

  override fun end(interrupted: Boolean) {
      feeder.currentVel = previousVel
      intake.stop()
      indexer.stop()
  }
}