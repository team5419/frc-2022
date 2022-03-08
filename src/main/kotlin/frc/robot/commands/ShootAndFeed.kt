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

class ShootAndFeed(_shooter: Shooter, _feeder: Feeder, _indexer: Indexer, _lights: Lights, _main: Double = -1.0, _kicker: Double = -1.0, _time: Double = 0.0)  : CommandBase() {
  private val shooter: Shooter = _shooter;
  private val main: Double = _main;
  private val kicker: Double = _kicker;
  private val feeder: Feeder = _feeder;
  private val indexer: Indexer = _indexer
  private val previousVel: Double = feeder.currentVel
  private val time: Double = _time
  private val timer: Timer = Timer()
  private val lights: Lights = _lights;

  init {
    addRequirements(_shooter)
    addRequirements(_indexer);
  }

  override fun initialize() {
    timer.reset()
    timer.start()
    feeder.currentVel = FeederConstants.activePercent
    lights.currentRGB = RGB(86, 255, 51); // pink
    lights.blinking = true;
    shooter.shoot(main, kicker)
  }

  override fun execute() {
    if(shooter.isSpedUp())
    {
      indexer.index(0.75);
    }
  }

  override fun isFinished(): Boolean {
    return time > 0.0 && timer.get() >= time
  }

  override fun end(interrupted: Boolean) {
      timer.stop()
      feeder.currentVel = previousVel
      lights.currentRGB = RGB(0, 0, 0)
      lights.blinking = false
      shooter.stop()
      indexer.stop()
  }
}
