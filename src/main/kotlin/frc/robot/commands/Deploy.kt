package frc.robot.commands; 

import frc.robot.subsystems.IntakeSub;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.XboxController;

class Deploy(_intake: IntakeSub) : CommandBase() {
  private val intake: IntakeSub = _intake;

  init {
    addRequirements(_intake);
  }

  override fun initialize() {
    intake.deployStart();
  }
 
  override fun execute() {
    println("executing")
  }

  override fun end(interrupted: Boolean) {
    intake.deployStop();
  }

  override fun isFinished(): Boolean {
    return false
  }
}