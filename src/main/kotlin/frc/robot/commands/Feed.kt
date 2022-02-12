package frc.robot.commands; 

import frc.robot.subsystems.Feeder;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.Timer


class Feed(_feeder: Feeder, _time: Double = 0.0) : CommandBase() {
  private val feeder: Feeder = _feeder
  private val time: Double = _time
  private val timer: Timer = Timer()

  init {
    addRequirements(_feeder);
  }

  override fun execute() {
    feeder.feed();
  }

  override fun end(interrupted: Boolean) {
    feeder.stop()
    timer.stop()
  }

  override fun initialize() {
    timer.reset()
    timer.start()
  }

  // end command if time has elapsed
  override fun isFinished(): Boolean {
    return (time != 0.0 && timer.get() >= time)
  }

}
