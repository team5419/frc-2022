package frc.robot.commands; 

import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Feeder;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.Timer
import frc.robot.FeederConstants


class RunIntake(_intake: Intake, _feeder: Feeder, _time: Double = 0.0) : CommandBase() {
  private val intake: Intake = _intake
  private val feeder: Feeder = _feeder
  private val time: Double = _time
  private val timer: Timer = Timer()

  init {
    addRequirements(_intake);
    addRequirements(_feeder)
  }

  override fun initialize() {
      timer.reset()
      timer.start()
  }

  override fun execute() {
    intake.intake();
    feeder.feed(FeederConstants.activePercent)
  }

  override fun end(interrupted: Boolean) {
    intake.stop()
    timer.stop()
    if(feeder.idling) {
      Feed(feeder).schedule()
    } else {
      feeder.stop()
    }
  }

  // end command if time has elapsed
  override fun isFinished(): Boolean {
    return time > 0.0 && timer.get() >= time
  }
}
