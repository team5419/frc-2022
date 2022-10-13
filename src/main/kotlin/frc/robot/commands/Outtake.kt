package frc.robot.commands; 

import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Indexer;
import frc.robot.subsystems.Feeder;
import frc.robot.FeederConstants;
import frc.robot.IndexerConstants;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.classes.SubsystemHolder
class Outtake(_subsystems: SubsystemHolder)  : CommandBase() {
  private val subsystems: SubsystemHolder = _subsystems
  private var startingPosition: Double = 0.0

  init {
    addRequirements(_subsystems.indexer)
    addRequirements(_subsystems.intake)
  }

  override fun initialize() {
    startingPosition = subsystems.indexer.encoder.getPosition()
    subsystems.feeder.currentVel = -0.9; //FeederConstants.reversePercent
    subsystems.intake.reverse()
  }

  override fun execute() {
    subsystems.indexer.index(-0.5);
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
      subsystems.feeder.currentVel = FeederConstants.idlePercent
      subsystems.intake.stop()
      subsystems.indexer.stop()
  }
}