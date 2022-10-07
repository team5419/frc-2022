package frc.robot.commands; 

import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Feeder;
import frc.robot.subsystems.DeploySubsystem;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.Timer
import frc.robot.FeederConstants
import frc.robot.classes.SubsystemHolder

class RunIntake(_subsystems: SubsystemHolder, _time: Double = 0.0, _velocity: Double = 1.0) : CommandBase() {
  private val subsystems: SubsystemHolder = _subsystems
  private val time: Double = _time
  private val timer: Timer = Timer()
  private val previousVel: Double = subsystems.feeder.currentVel
  private val velocity: Double = _velocity

  init {
    addRequirements(_subsystems.intake)
  }

  override fun initialize() {
      timer.reset()
      timer.start()
      subsystems.feeder.currentVel = FeederConstants.activePercent
      subsystems.intake.intake(velocity)
      //deploy.changeSetpoint(25.0);
  }

  override fun execute() {
    //println("running intake")
  }

  override fun end(interrupted: Boolean) {
    subsystems.intake.stop()
    timer.stop()
    subsystems.feeder.currentVel = previousVel;
    //deploy.changeSetpoint(0.0)
  }

  // end command if time has elapsed
  override fun isFinished(): Boolean {
    return time > 0.0 && timer.get() >= time
  }
}
