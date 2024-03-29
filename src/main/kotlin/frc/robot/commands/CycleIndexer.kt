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

class CycleIndexer(_indexer: Indexer, _shooter: Shooter) : SequentialCommandGroup() {
  private val indexer: Indexer = _indexer;
  private val shooter: Shooter = _shooter;

  init {
    addCommands(
      WaitForShooter(shooter, 0.5), 
      Index(indexer)  
    )
  }

  override fun end(interrupted: Boolean) {
    indexer.stop()
  }
}
