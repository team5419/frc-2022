package frc.robot.commands; 

import frc.robot.subsystems.Indexer;
import frc.robot.subsystems.Feeder;
import frc.robot.subsystems.Shooter;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.Timer
import frc.robot.IndexerConstants
import frc.robot.subsystems.Lights
import frc.robot.classes.RGB
import frc.robot.classes.SubsystemHolder


class DefaultIndex(_subsystems: SubsystemHolder) : CommandBase() {
  private val subsystems: SubsystemHolder = _subsystems

  private val timer: Timer = Timer()

  init {
    addRequirements(_subsystems.indexer);
  }

  override fun initialize() {
    timer.reset();
    timer.stop();
  }

  override fun execute() {
    if(timer.get() == 0.0) {
      if(subsystems.indexer.atPositionOne() && subsystems.indexer.atPositionTwo() && subsystems.indexer.atPositionThree()) {
        subsystems.lights.setColor(RGB(0, 255, 0));
        timer.start();
      } else if(subsystems.indexer.atPositionThree()) {
        subsystems.lights.setColor(RGB(126, 66, 245));
        timer.start();
      } else {
        if(subsystems.lights.isEqualTo(0, 255, 0) || subsystems.lights.isEqualTo(126, 66, 245)) {
          subsystems.lights.setColor(RGB(0, 0, 0));
          timer.start();
        }
      }
    }
    if(timer.get() >= 0.5) {
      timer.stop();
      timer.reset();
    }
    
    if(subsystems.indexer.atPositionOne() && (!subsystems.indexer.atPositionTwo()) && (!subsystems.indexer.atPositionThree())) {
        subsystems.indexer.index(0.4);
    } else {
        subsystems.indexer.stop();
    }
  }

  override fun end(interrupted: Boolean) {
    subsystems.indexer.stop()
    timer.stop();
  }

  // end command if time has elapsed
  override fun isFinished(): Boolean {
    return false
  }

}
