package frc.robot.commands; 

import frc.robot.subsystems.Intake;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.Timer

class Shoot(_intake: Intake) : CommandBase() {
  private val intake: Intake = _intake;
  private val time: Double = 0.0;
  private val timer: Timer = Timer()

  init {
    addRequirements(_intake);
  }

  override fun initialize() {
    timer.reset()
    timer.start()
    intake.shoot(true);
  }

  override fun execute() {
    println("executing")
  }

  override fun end(interrupted: Boolean) {
    println("ended")
    intake.shoot(true);
    timer.start();
  }

  override fun isFinished(): Boolean {
    return (timer.get() > .5);
  }
}