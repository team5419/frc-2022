package frc.robot.commands; 

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.XboxController;

class Shoot(_reverse: Boolean = false) : CommandBase() {
  private val reverse: Boolean = _reverse;

  init {
    addRequirements();
  }

  override fun initialize() {
  }

  override fun execute() {
  }

  override fun end(interrupted: Boolean) {
    
  }

  override fun isFinished(): Boolean {
    return false;
  }
}