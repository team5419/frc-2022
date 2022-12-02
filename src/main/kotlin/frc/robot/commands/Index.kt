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

import frc.robot.ShooterConstants


class Index(_subsystems: SubsystemHolder /* , _targetMain : Double = -1.0, _targetKicker : Double = -1.0 */) : CommandBase() {
  private val subsystems: SubsystemHolder = _subsystems
  private var startingPosition: Double = 0.0
  private var targetMain : Double = 0.0
  private var targetKicker : Double = 0.0

 

  private var wasSped : Boolean = false

  init {
    addRequirements(_subsystems.indexer);
  }

  override fun initialize() {
    startingPosition = subsystems.indexer.encoder.getPosition()
  }

  override fun execute() {
    /*if (targetMain == -1.0 && targetKicker == -1.0){
      if (subsystems.vision.isTargetFound()){
        println("Index Sensing")
        val setpoint : LookupEntry = subsystems.vision.getShotSetpoint();
        targetMain = setpoint.mainVelocity;
        targetKicker = setpoint.kickerVelocity;
      }
      else {
        targetMain = ShooterConstants.mainVelocity
        targetKicker = ShooterConstants.kickerVelocity
      }
    } */
    if(subsystems.shooter.isSpedUp()) {
      println("indexing!!")
      subsystems.indexer.index(0.4);
      wasSped = true
    } 
    else {
      println("not indexing!!")
      if (wasSped){
        Wait(0.12)
      }
      subsystems.indexer.index(0.0);
      wasSped = false
    }
    //indexer.index(0.4);
  }

  override fun end(interrupted: Boolean) {
    subsystems.indexer.stop()
    wasSped = false
  }

  // end command if time has elapsed
  override fun isFinished(): Boolean {
    //return indexer.encoder.getPosition() - startingPosition >= IndexerConstants.ticksPerIndex
    return false
  }

}