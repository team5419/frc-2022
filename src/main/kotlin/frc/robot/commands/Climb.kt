package frc.robot.commands; 

import frc.robot.subsystems.Climber;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.Timer

class Climb(_climber: Climber, _codriver: XboxController, _time: Double = 0.0) : CommandBase() {
  private val climber: Climber = _climber;
  private val codriver: XboxController = _codriver;
  private val time: Double = _time
  private val timer: Timer = Timer()

  init {
    addRequirements(_climber);
  }

  override fun initialize() {
    timer.reset()
    timer.start()
  }

  override fun execute() {
    climber.setPair(0, codriver.getLeftY())
    climber.setPair(1, codriver.getRightY())
    //println("trying to climb")
  }

  override fun end(interrupted: Boolean) {
    timer.stop()
  }

  override fun isFinished(): Boolean {
    return time > 0.0 && timer.get() >= time;
  }
}
