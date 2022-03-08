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

  init {
    addRequirements(_indexer);
  }

  override fun initialize() {
  }

  override fun execute() {
    if(indexer.atPositionOne() && indexer.atPositionTwo() && indexer.atPositionThree() && lights.isEqualTo(0, 0, 0)) {
      lights.currentRGB = RGB(51, 158, 255);
      lights.blinking = false;
    } else {
      if(lights.isEqualTo(51, 158, 255)) {
        lights.currentRGB = RGB(0, 0, 0);
        lights.blinking = false;
      }
    }
    if(indexer.atPositionOne() && !indexer.atPositionThree()) {
        indexer.index(0.3);
    } else {
        indexer.stop();
    }
  }

  override fun end(interrupted: Boolean) {
    indexer.stop()
  }

  // end command if time has elapsed
  override fun isFinished(): Boolean {
    return false
  }

}
