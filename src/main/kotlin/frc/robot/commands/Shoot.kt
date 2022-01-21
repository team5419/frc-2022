// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands; 

import frc.robot.subsystems.Shooter;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.Timer


class Shoot(_shooter: Shooter, _velocity: Double = -1.0, _time: Double = 0.0) : CommandBase() {
  private val shooter: Shooter = _shooter;
  private val velocity: Double = _velocity;
  private val time: Double = _time;
  private val timer: Timer = Timer()

  init {
    addRequirements(_shooter);
  }

  // Called when the command is initially scheduled.
  override fun initialize() {
    timer.reset()
    timer.start()
  }

  // Called every time the scheduler runs while the command is scheduled.
  override fun execute() {
    shooter.shoot(velocity);
  }

  override fun isFinished(): Boolean {
    if(time != 0.0 && timer.get() >= time) {
      return true
    }
    return false
  }

  // Called once the command ends or is interrupted.
  override fun end(interrupted: Boolean) {
    shooter.stop()
    timer.stop()
  }
}
