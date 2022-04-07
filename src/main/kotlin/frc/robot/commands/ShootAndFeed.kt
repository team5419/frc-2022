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

import edu.wpi.first.wpilibj.GenericHID.RumbleType

class ShootAndFeed(_shooter: Shooter, _feeder: Feeder, _indexer: Indexer, _lights: Lights, _driver: XboxController, _main: Double = -1.0, _kicker: Double = -1.0, _time: Double = 0.0)  : CommandBase() {
  private val shooter: Shooter = _shooter;
  private val main: Double = _main;
  private val kicker: Double = _kicker;
  private val feeder: Feeder = _feeder;
  private val indexer: Indexer = _indexer
  private val time: Double = _time
  private val timer: Timer = Timer()
  private val lights: Lights = _lights;
  private val driver: XboxController = _driver;

  init {
    addRequirements(_shooter)
    //addRequirements(_indexer)
  }

  override fun initialize() {
    timer.reset()
    timer.start()
    feeder.currentVel = FeederConstants.activePercent
    lights.setColor(shooter.currentColor);
    shooter.shoot(main, kicker)
    println("shooting")

    driver.setRumble(RumbleType.kLeftRumble, 1.0);
    driver.setRumble(RumbleType.kRightRumble, 1.0);
  }

  override fun execute() {
    // if(indexer.atPositionTwo() || indexer.atPositionThree()) {
    //   feeder.currentVel = -0.1;
    // } else {
    //   feeder.currentVel = FeederConstants.activePercent;
    // }
    // if(shooter.isSpedUp()) {
    //   indexer.index(0.8);
    // }
  }

  override fun isFinished(): Boolean {
    return (time > 0.0 && timer.get() >= time) && (!indexer.atPositionOne() && !indexer.atPositionTwo() && !indexer.atPositionThree())
  }

  override fun end(interrupted: Boolean) {
      timer.stop()
      feeder.currentVel = FeederConstants.idlePercent
      lights.setColor(RGB(0, 0, 0))
      shooter.stop()
      indexer.stop()
      driver.setRumble(RumbleType.kLeftRumble, 0.0);
      driver.setRumble(RumbleType.kRightRumble, 0.0);
  }
}
