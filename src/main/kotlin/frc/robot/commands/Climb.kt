package frc.robot.commands; 

import frc.robot.subsystems.Climber;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.XboxController;

class Climb(_climber: Climber, _codriver: XboxController, _cocodriver: XboxController) : CommandBase() {
  private val climber: Climber = _climber;
  private val codriver: XboxController = _codriver;
  private val cocodriver: XboxController = _cocodriver;

  init {
    addRequirements(_climber);
  }

  override fun initialize() {}

  override fun execute() {
    climber.setPair(0, codriver.getLeftY())
    climber.setPair(1, codriver.getRightY())
    climber.setPair(2, cocodriver.getLeftY())
    //println("trying to climb")
  }

  override fun end(interrupted: Boolean) {}

  override fun isFinished(): Boolean {
    return false;
  }
}