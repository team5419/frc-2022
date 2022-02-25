package frc.robot.commands; 

import frc.robot.subsystems.Indexer;
import frc.robot.subsystems.Shooter;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.Timer
import frc.robot.IndexerConstants


class Index2(_indexer: Indexer, _shooter: Shooter, _time: Double = 0.0) : CommandBase() {
  private val indexer: Indexer = _indexer
  private val shooter: Shooter = _shooter
  private val time: Double = _time
  private val timer: Timer = Timer()

  init {
    addRequirements(_indexer);
  }

  override fun initialize() {
    timer.reset()
    timer.start()
  }

  override fun execute() {
    if(shooter.isSpedUp()) 
    {
      indexer.index();
    }
  }

  override fun end(interrupted: Boolean) {
    indexer.stop()
    timer.stop()
  }

  // end command if time has elapsed
  override fun isFinished(): Boolean {
    return time > 0.0 && timer.get() >= time
  }

}
