package frc.robot.commands; 

import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Indexer;
import frc.robot.subsystems.Feeder;
import frc.robot.FeederConstants;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.subsystems.Lights;
import frc.robot.classes.RGB;

class Wait(_time: Double)  : CommandBase() {
  private val timer: Timer = Timer()
  private val time: Double = _time

  init {
  }

  override fun initialize() {
    timer.reset()
    timer.start()
  }

  override fun execute() {
  }

  override fun isFinished(): Boolean {
    return timer.get() >= time
  }

  override fun end(interrupted: Boolean) {
      timer.stop()
  }
}
