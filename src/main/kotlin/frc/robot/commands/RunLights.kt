package frc.robot.commands; 

import frc.robot.subsystems.Lights;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.Timer


class RunLights(_lights: Lights) : CommandBase() {
  private val lights: Lights = _lights

  init {
    addRequirements(_lights);
  }

  override fun initialize() {
  }

  override fun execute() {
    lights.testLights();
  }

  override fun end(interrupted: Boolean) {
    lights.stop();
  }

  // end command if time has elapsed
  override fun isFinished(): Boolean {
    return false
  }

}
