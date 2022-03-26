package frc.robot.commands; 

import frc.robot.subsystems.Drivetrain;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.XboxController;


class AutoDrive2(_drivetrain: Drivetrain, _velocity :Double = 0.5, _isSlow: Boolean = false, _ticks : Double) : CommandBase() {
  private val drivetrain: Drivetrain = _drivetrain;
  private val isSlow: Boolean = _isSlow;
  private val velocity = _velocity
  private val ticks = _ticks
  private var startingPosition: Double = 0.0

  init {
    addRequirements(_drivetrain);
  }

  override fun initialize() {
    startingPosition = drivetrain.leftDistance
  }

  override fun execute() {
    drivetrain.drive(velocity, 0.0, isSlow);
  }

  override fun end(interrupted: Boolean) {
      drivetrain.drive(0.0, 0.0, isSlow)
  }

  override fun isFinished(): Boolean {
    return Math.abs(drivetrain.leftDistance - startingPosition) >= ticks
  }
}