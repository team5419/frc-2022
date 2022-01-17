// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands; 

import frc.robot.subsystems.Shooter;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.XboxController;

class Shoot(_shooter: Shooter, _velocity: Double) : CommandBase() {
  private val shooter: Shooter = _shooter;
  private val velocity: Double = _velocity;

  init {
    addRequirements(_shooter);
  }

  // Called when the command is initially scheduled.
  override fun initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  override fun execute() {
    shooter.shoot(velocity);
  }

  // Called once the command ends or is interrupted.
  override fun end(interrupted: Boolean) {}
}
