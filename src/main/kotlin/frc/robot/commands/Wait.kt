package frc.robot.commands; 

import edu.wpi.first.wpilibj2.command.CommandBase;

import edu.wpi.first.wpilibj.Timer

class Wait(_delay: Double) : CommandBase() {
  private val timer: Timer = Timer()
  private val delay: Double = _delay

  init {
  }

  override fun initialize() {
    timer.reset();
    timer.start();
  }

  override fun execute() {
    
  }

  override fun end(interrupted: Boolean) {
    timer.stop()
  }

  override fun isFinished(): Boolean {
    return (timer.get() >= delay);
  }
}