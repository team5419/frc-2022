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
   climber.setPairVelocity(0, codriver.getRightY(), codriver.getRightX() * 0.2)
   climber.setPairVelocity(1, codriver.getLeftY(), codriver.getLeftX() * 0.2)

    //climber.setPair0(codriver.getRightY(), codriver.getRightX() * 0.2)
    //climber.setPair1(codriver.getLeftY(), codriver.getLeftX() * 0.2)
  }

  override fun end(interrupted: Boolean) {
  }

  override fun isFinished(): Boolean {
    return false;
  }
}
