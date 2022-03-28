package frc.robot.commands; 

import frc.robot.subsystems.Drivetrain;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.Timer
import frc.robot.IndexerConstants


class InvertDrive(_drivetrain: Drivetrain) : CommandBase() {
  private val drivetrain: Drivetrain = _drivetrain
  init {
  }

  override fun initialize() {
    drivetrain.inverted *= -1;
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