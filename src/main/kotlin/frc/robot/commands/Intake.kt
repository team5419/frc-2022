package frc.robot.commands; 

import frc.robot.subsystems.IntakeSub;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.XboxController;

class Intake(_intake: IntakeSub) : CommandBase() {
  private val intake: IntakeSub = _intake;

  init {
    addRequirements(_intake);
  }

  override fun initialize() {
    intake.intakeStart();
  }
 
  override fun execute() {
    println("executing")
  }

  override fun end(interrupted: Boolean) {
    intake.intakeStop();
  }

  override fun isFinished(): Boolean {
    return false
  }
}