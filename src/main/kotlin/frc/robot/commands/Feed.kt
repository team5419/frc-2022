package frc.robot.commands; 

import frc.robot.subsystems.Climber;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.XboxController;

class Feed(_intake: Climber, _driver: XboxController) : CommandBase() {
  private val climber: Climber = _climber;
  private val codriver: XboxController = _driver;

  init {
    addRequirements(_intake);
  }

  override fun initialize() {}

  override fun execute() {
    intake.feedForward()
    //println("trying to climb")
  }

  override fun end(interrupted: Boolean) {}

  override fun isFinished(): Boolean {
    return false;
  }
}