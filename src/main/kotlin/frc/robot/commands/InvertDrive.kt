package frc.robot.commands; 

import frc.robot.subsystems.Drivetrain;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.Timer
import frc.robot.IndexerConstants
import frc.robot.classes.SubsystemHolder

class InvertDrive(_subsystems: SubsystemHolder) : CommandBase() {
  private val subsystems: SubsystemHolder = _subsystems
  init {
  }

  override fun initialize() {
    subsystems.drivetrain.inverted *= -1;
  }

  override fun execute() {
  }

  override fun end(interrupted: Boolean) {
  }

  // end command if time has elapsed
  override fun isFinished(): Boolean {
    return true;
  }

}