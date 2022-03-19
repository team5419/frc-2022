package frc.robot.commands; 

import frc.robot.subsystems.Lights;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.Timer
import frc.robot.FeederConstants
import frc.robot.classes.RGB;


class RunLights(_lights: Lights) : CommandBase() {
  private val lights: Lights = _lights
  private val timer: Timer = Timer()

  init {
      addRequirements(_lights)
  }

  override fun initialize() {
      lights.currentRGB = RGB(0, 0, 0);
      lights.blinking = false;
      timer.reset();
      timer.start();
  }

  override fun execute() {
    //   if(lights.blinking && Math.round(timer.get() * 2).rem(2).toInt() == 0) {
    //       lights.off();
    //   } else {
    //       lights.setColor();
    //   } 
  }

  override fun end(interrupted: Boolean) {
      lights.stop();
  }

  // end command if time has elapsed
  override fun isFinished(): Boolean {
    return false
  }
}
