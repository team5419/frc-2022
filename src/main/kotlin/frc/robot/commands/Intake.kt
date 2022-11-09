package frc.robot.commands; 

import frc.robot.subsystems.IntakeSub;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.Timer

class Intake(_intake: IntakeSub) : CommandBase() {
  private val intake: IntakeSub = _intake;
  private val timer: Timer = Timer()

  init {
    addRequirements(_intake);
  }

  override fun initialize() {
    timer.reset()
    timer.start()
    intake.start();
  }

  override fun execute() {
    println("executing")
  }

  override fun end(interrupted: Boolean) {
    timer.stop();
    intake.stop();
  }

  override fun isFinished(): Boolean {
    return (timer.get() > .5);
  }
}