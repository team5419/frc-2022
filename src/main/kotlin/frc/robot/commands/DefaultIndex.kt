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
  private var hasUpdated: Boolean = false

  init {
    addRequirements(_subsystems.indexer);
  }

  override fun initialize() {
    timer.reset();
    timer.stop();
  }

  override fun execute() {
    if(!hasUpdated) {
      if(subsystems.indexer.atPositionOne() && subsystems.indexer.atPositionTwo() && subsystems.indexer.atPositionThree()) {
        subsystems.lights.setColor(RGB(255, 0, 0));
        timer.start();
        hasUpdated = true;
      } else if(subsystems.indexer.atPositionThree()) {
        subsystems.lights.setColor(RGB(0, 255, 0));
        timer.start();
        hasUpdated = true;
      } else {
        if(subsystems.lights.isEqualTo(0, 255, 0) || subsystems.lights.isEqualTo(255, 0, 0)) {
          subsystems.lights.setColor(RGB(0, 0, 0));
          timer.start();
          hasUpdated = true;
        }
      }
    }
    if(timer.get() >= 0.5) {
      timer.stop();
      timer.reset();
      hasUpdated = false;
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
