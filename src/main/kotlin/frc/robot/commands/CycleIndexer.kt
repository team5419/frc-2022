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

import frc.robot.classes.SubsystemHolder
class CycleIndexer(_subsystems: SubsystemHolder, _numtimes: Int = 1) : SequentialCommandGroup() {
  private val subsystems: SubsystemHolder = _subsystems
  private val numtimes: Int = _numtimes

  init {
    // for(i in 1..numtimes) {
         addCommands(
    //         WaitForShooter(shooter, 0.0), 
            Index(subsystems)  
         )
    // }
  }

  override fun end(interrupted: Boolean) {
    subsystems.indexer.stop()
  }

  override fun isFinished(): Boolean {
    return false
  }
}