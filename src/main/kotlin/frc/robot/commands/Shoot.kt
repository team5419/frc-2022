package frc.robot.commands; 

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.XboxController;

import frc.robot.subsystems.Catapult

import edu.wpi.first.wpilibj.Timer

class Shoot(_catapult: Catapult) : CommandBase() {
  private val catapult = _catapult;
  private val timer: Timer = Timer()

  init {
    addRequirements(_catapult);
  }

  override fun initialize() {
    timer.reset();
    timer.start();
    catapult.start();
  }

  override fun execute() {
    timer.reset();
    timer.start();
    catapult.state = !catapult.state;
    catapult.start();
    // catapult.start();
  }

  override fun end(interrupted: Boolean) {
    timer.stop()
    // catapult.stop();
  }

  override fun isFinished(): Boolean {
    return (timer.get() >= 2.0);
    // return false;
  }
}