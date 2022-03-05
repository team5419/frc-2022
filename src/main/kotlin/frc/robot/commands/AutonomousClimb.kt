package frc.robot.commands; 

import frc.robot.subsystems.Climber;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.Timer

class AutoClimb(_climber: Climber, _index: Int, _ticks: Double, _speed: Double = 1.0) : CommandBase() {
  private val climber: Climber = _climber;
  private val index: Int = _index;
  private val ticks: Double = _ticks;
  private val speed: Double = _speed;
  private var startingLeft: Double = 0.0;
  private var startingRight: Double = 0.0;

  init {
    addRequirements(_climber);
  }

  override fun initialize() {
    startingLeft = climber.pairs[index].left.getSelectedSensorPosition();
    startingRight = climber.pairs[index].right.getSelectedSensorPosition();
  }

  override fun execute() {
    if(climber.pairs[index].left.getSelectedSensorPosition() - startingLeft < ticks) {
        climber.setIndividual(climber.pairs[index].left, speed);
    }
    if(climber.pairs[index].right.getSelectedSensorPosition() - startingRight < ticks) {
        climber.setIndividual(climber.pairs[index].right, speed);
    }
  }

  override fun end(interrupted: Boolean) {
    climber.setPair(index, 0.0, 0.0);
  }

  override fun isFinished(): Boolean {
    return climber.pairs[index].left.getSelectedSensorPosition() - startingLeft >= ticks && climber.pairs[index].right.getSelectedSensorPosition() - startingRight >= ticks
  }
}
