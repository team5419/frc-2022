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
import frc.robot.classes.SubsystemHolder

class ShootAndFeed(_subsystems: SubsystemHolder, _driver: XboxController, _main: Double = -1.0, _kicker: Double = -1.0, _time: Double = 0.0)  : CommandBase() {
  private val subsystems: SubsystemHolder = _subsystems
  private val main: Double = _main;
  private val kicker: Double = _kicker;
  private val time: Double = _time
  private val timer: Timer = Timer()
  private val driver: XboxController = _driver;

  init {
    addRequirements(_subsystems.shooter)
    //addRequirements(_indexer)
  }

  override fun initialize() {
    timer.reset()
    timer.start()
    subsystems.feeder.currentVel = FeederConstants.activePercent
    subsystems.lights.setColor(subsystems.shooter.currentColor);
    subsystems.shooter.shoot(main, kicker)
    println("shooting")

    // driver.setRumble(RumbleType.kLeftRumble, 1.0);
    // driver.setRumble(RumbleType.kRightRumble, 1.0);
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
    //return time != 0.0 && timer.get() > time
    if(time > 0.0) {
      if(timer.get() < time) {
        return false;
      }
      return (!subsystems.indexer.atPositionOne() && !subsystems.indexer.atPositionTwo() && !subsystems.indexer.atPositionThree())
    }
    return false
  }

  override fun end(interrupted: Boolean) {
      timer.stop()
      subsystems.feeder.currentVel = FeederConstants.idlePercent
      subsystems.lights.setColor(RGB(0, 0, 0))
      subsystems.shooter.stop()
      subsystems.indexer.stop()
      // driver.setRumble(RumbleType.kLeftRumble, 0.0);
      // driver.setRumble(RumbleType.kRightRumble, 0.0);
  }
}
