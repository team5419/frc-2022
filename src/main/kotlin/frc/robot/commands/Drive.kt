// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.subsystems.Drivetrain;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.XboxController;

class Drive(_drivetrain: Drivetrain, _driver: XboxController) : CommandBase() {
  private val drivetrain: Drivetrain = _drivetrain;
  private val driver: XboxController = _driver;

  init {
    addRequirements(_drivetrain);
  }

  // Called when the command is initially scheduled.
  override fun initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  override fun execute() {
    drivetrain.drive(driver.getY( Hand.kLeft ).toDouble(), driver.getX( Hand.kRight ).toDouble(), false);
  }

  // Called once the command ends or is interrupted.
  override fun end(interrupted: Boolean) {}

  // Returns true when the command should end.
  override fun isFinished(): Boolean {
    return false;
  }
}
