package frc.robot.commands; 

import frc.robot.subsystems.Indexer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.Timer
import frc.robot.IndexerConstants
import frc.robot.subsystems.Shooter;
import frc.robot.classes.SubsystemHolder

import frc.robot.Lookup
import frc.robot.LookupEntry


class Index(_subsystems: SubsystemHolder) : CommandBase() {
  private val subsystems: SubsystemHolder = _subsystems
  private var startingPosition: Double = 0.0
  /*private var setpoint : LookupEntry
  private var targetMain : Double
  private var targetKicker : Double*/
  init {
    addRequirements(_subsystems.indexer);
  }

  override fun initialize() {
    startingPosition = subsystems.indexer.encoder.getPosition()
  }

  override fun execute() {
    val setpoint : LookupEntry = subsystems.vision.getShotSetpoint();
    val targetMain : Double = setpoint.mainVelocity;
    val targetKicker : Double = setpoint.kickerVelocity;
    if(subsystems.shooter.isSpedUp(targetMain, targetKicker)) {
      subsystems.indexer.index(0.4);
    } else {
      subsystems.indexer.index(0.0);
    }
    //indexer.index(0.4);
  }

  override fun end(interrupted: Boolean) {
    subsystems.indexer.stop()
  }

  // end command if time has elapsed
  override fun isFinished(): Boolean {
    //return indexer.encoder.getPosition() - startingPosition >= IndexerConstants.ticksPerIndex
    return false
  }

}