package frc.robot.commands; 

import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Indexer;
import frc.robot.subsystems.Feeder;
import frc.robot.FeederConstants;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.Timer
import frc.robot.commands.WaitForShooter
import frc.robot.commands.Index
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

class CycleIndexer(_indexer: Indexer, _shooter: Shooter, _time: Double = 0.0) : SequentialCommandGroup() {
  private val indexer: Indexer = _indexer;
  private val shooter: Shooter = _shooter;
  private val time: Double = _time
  private val timer: Timer = Timer()

  init {
    addCommands(
      WaitForShooter(shooter, 0.15), 
      Index(indexer)  
    )
  }

  override fun initialize() {
    timer.reset()
    timer.start()
  }

  override fun end(interrupted: Boolean) {
    timer.stop()
    indexer.stop()
  }

  override fun isFinished(): Boolean {
    return time > 0.0 && timer.get() >= time
  }
}
