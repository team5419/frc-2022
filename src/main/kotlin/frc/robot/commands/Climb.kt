package frc.robot.commands; 

import frc.robot.subsystems.Climber;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.Timer

class Climb(_climber: Climber, _codriver: XboxController) : CommandBase() {
  private val climber: Climber = _climber;
  private val codriver: XboxController = _codriver;

  init {
    addRequirements(_climber);
  }

  override fun initialize() {
  }

  override fun execute() {
    climber.setPair(0, codriver.getLeftY() * 0.5, codriver.getLeftX() * 0.5)
    climber.setPair(1, codriver.getRightY() * 0.5, codriver.getRightX() * 0.5)
    //println("trying to climb")
  }

  override fun end(interrupted: Boolean) {
  }

  override fun isFinished(): Boolean {
    return false;
  }
}
