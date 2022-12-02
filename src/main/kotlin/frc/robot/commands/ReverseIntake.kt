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
class ReverseIntake(_subsystems: SubsystemHolder)  : CommandBase() {
  private val subsystems: SubsystemHolder = _subsystems
  private var startingPosition: Double = 0.0

  init {
    
  }

  override fun initialize() {
    subsystems.intake.reverseDirection()
  }

  override fun execute() {
    
  }

  override fun isFinished(): Boolean {
    return true
  }

  override fun end(interrupted: Boolean) {
      
  }
}