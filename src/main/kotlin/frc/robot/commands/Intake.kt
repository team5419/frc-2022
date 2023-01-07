package frc.robot.commands; 

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.XboxController;

import frc.robot.subsystems.IntakeSub;

import edu.wpi.first.wpilibj.Timer

class Intake(_intake: IntakeSub) : CommandBase() {
  private val intake: IntakeSub = _intake;
  //private val timer: Timer = Timer()

  init {
    //addRequirements(_intake);
  }

  override fun initialize() {
    // timer.reset();
    // timer.start();
    intake.intake123();
  }

  override fun execute() {
    
  }

  override fun end(interrupted: Boolean) {
    //timer.stop()
    
  }

  override fun isFinished(): Boolean {
    return true//(timer.get() >= 0.25);
    
  }
}