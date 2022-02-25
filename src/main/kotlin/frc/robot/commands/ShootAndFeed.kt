package frc.robot.commands; 

import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Indexer;
import frc.robot.subsystems.Feeder;
import frc.robot.FeederConstants;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.Timer
import frc.robot.commands.Feed


class ShootAndFeed(_shooter: Shooter, _feeder: Feeder, _main: Double = -1.0, _kicker: Double = -1.0, _time: Double = 0.0)  : CommandBase() {
  private val shooter: Shooter = _shooter;
  private val main: Double = _main;
  private val feeder: Feeder = _feeder;
  private val kicker: Double = _kicker;
  private val previousVel: Double = feeder.currentVel
  private val time: Double = _time
  private val timer: Timer = Timer()
  init {
    addRequirements(_shooter)
  }

  override fun initialize() {
    timer.reset()
    timer.start()
    feeder.currentVel = FeederConstants.activePercent
    shooter.shoot(main, kicker)
  }

  override fun execute() {
  }

  override fun isFinished(): Boolean {
    return time > 0.0 && timer.get() >= time
  }

  override fun end(interrupted: Boolean) {
      timer.stop()
      feeder.currentVel = previousVel
      shooter.stop()
  }
}
