package frc.robot.commands; 

import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Indexer;
import frc.robot.subsystems.Feeder;
import frc.robot.FeederConstants;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.Timer
import frc.robot.commands.Feed


class WaitForShooter(_shooter: Shooter, _time: Double) : CommandBase() {
  private val shooter: Shooter = _shooter;
  private val time: Double = _time;
  private val timer: Timer = Timer()

  init {
  }

  override fun initialize() {
    timer.reset()
    timer.start()
  }

  override fun execute() {
    println("waiting")
  }

  // end command if time has elapsed
  override fun isFinished(): Boolean {
    println("done spinning up: ${shooter.isSpedUp()}")
    return (time != 0.0 && timer.get() >= time) && shooter.isSpedUp()
  }

  override fun end(interrupted: Boolean) {
    timer.stop()
  }
}