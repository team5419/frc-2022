package frc.robot.commands; 

import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Feeder;
import frc.robot.subsystems.DeploySubsystem;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.Timer
import frc.robot.FeederConstants


class RunIntake(_intake: Intake, _deploy: DeploySubsystem, _feeder: Feeder, _time: Double = 0.0, _velocity: Double = 1.0) : CommandBase() {
  
  private val intake: Intake = _intake
  private val deploy: DeploySubsystem = _deploy
  private val feeder: Feeder = _feeder
  private val time: Double = _time
  private val timer: Timer = Timer()
  private val previousVel: Double = feeder.currentVel
  private val velocity: Double = _velocity

  init {
    addRequirements(_intake)
  }

  override fun initialize() {
      timer.reset()
      timer.start()
      feeder.currentVel = FeederConstants.activePercent
      intake.intake(velocity)
      deploy.changeSetpoint(25.0);
  }

  override fun execute() {
    println("running intake")
  }

  override fun end(interrupted: Boolean) {
    intake.stop()
    timer.stop()
    feeder.currentVel = previousVel;
    deploy.changeSetpoint(0.0)
    println("ended")
  }

  // end command if time has elapsed
  override fun isFinished(): Boolean {
    return time > 0.0 && timer.get() >= time
  }
}
