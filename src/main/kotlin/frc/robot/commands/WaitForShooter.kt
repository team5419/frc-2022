/*package frc.robot.commands; 

import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Indexer;
import frc.robot.subsystems.Feeder;
import frc.robot.FeederConstants;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.Timer
import frc.robot.classes.SubsystemHolder

class WaitForShooter(_subsystems: SubsystemHolder, _time: Double) : CommandBase() {
  private val subsystems: SubsystemHolder = _subsystems
  private val time: Double = _time;
  private val timer: Timer = Timer()

  init {
  }

  override fun initialize() {
    timer.reset()
    timer.start()
  }

  override fun execute() {
  }

  // end command if time has elapsed
  override fun isFinished(): Boolean {
    return (timer.get() >= time) && subsystems.shooter.isSpedUp() 
  }

  override fun end(interrupted: Boolean) {
    timer.stop()
  }
}*/