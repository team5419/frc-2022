package frc.robot.commands; 

import frc.robot.subsystems.Climber;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.XboxController;

class Climb(_climber: Climber, _codriver: XboxController) : CommandBase() {
  private val climber: Climber = _climber;
  private val codriver: XboxController = _codriver;

  init {
    addRequirements(_climber);
  }

  override fun initialize() {}

  override fun execute() {
    climber.setLeftArm(0, codriver.getLeftY())
    climber.setRightArm(1, codriver.getRightY()) //r
    //println("trying to climb")
  }

  override fun end(interrupted: Boolean) {}

  override fun isFinished(): Boolean {
    return false;
  }
}