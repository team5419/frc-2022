package frc.robot.commands; 

import frc.robot.subsystems.Shooter;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.Timer


class Shoot(_shooter: Shooter, _main: Double = -1.0, _kicker: Double = -1.0, _time: Double = 0.0) : CommandBase() {
  private val shooter: Shooter = _shooter;
  private val main: Double = _main;
  private val kicker: Double = _kicker;
  private val time: Double = _time;
  private val timer: Timer = Timer()

  init {
    addRequirements(_shooter);
  }

  override fun initialize() {
    timer.reset()
    timer.start()
  }

  override fun execute() {
    shooter.shoot(main, kicker);
  }

  // end command if time has elapsed
  override fun isFinished(): Boolean {
    return (time != 0.0 && timer.get() >= time)
  }

  override fun end(interrupted: Boolean) {
    shooter.stop()
    timer.stop()
  }
}
