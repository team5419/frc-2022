package frc.robot.commands; 

import frc.robot.subsystems.Indexer;
import frc.robot.subsystems.Shooter;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.Timer
import frc.robot.IndexerConstants
import frc.robot.subsystems.Lights
import frc.robot.classes.RGB


class DefaultIndex(_indexer: Indexer, _lights: Lights) : CommandBase() {
  private val indexer: Indexer = _indexer
  private val lights: Lights = _lights

  private val timer: Timer = Timer()

  init {
    addRequirements(_indexer);
  }

  override fun initialize() {
    timer.reset();
    timer.stop();
  }

  override fun execute() {
    if(timer.get() == 0.0) {
      if(indexer.atPositionOne() && indexer.atPositionTwo() && indexer.atPositionThree() && lights.isEqualTo(0, 0, 0)) {
        lights.currentRGB = RGB(224, 66, 245);
        lights.blinking = false;
        timer.start();
      } else if(indexer.atPositionThree()) {
        lights.currentRGB = RGB(126, 66, 245);
        lights.blinking = false;
        timer.start();
      } else {
        if(lights.isEqualTo(224, 66, 245) || lights.isEqualTo(126, 66, 245)) {
          lights.currentRGB = RGB(0, 0, 0);
          lights.blinking = false;
          timer.start();
        }
      }
    }
    if(timer.get() >= 0.5) {
      timer.reset();
      timer.stop();
    }
    
    if(indexer.atPositionOne() && (!indexer.atPositionTwo()) && (!indexer.atPositionThree())) {
        indexer.index(0.4);
    } else {
        indexer.stop();
    }
  }

  override fun end(interrupted: Boolean) {
    indexer.stop()
    timer.stop();
  }

  // end command if time has elapsed
  override fun isFinished(): Boolean {
    return false
  }

}
