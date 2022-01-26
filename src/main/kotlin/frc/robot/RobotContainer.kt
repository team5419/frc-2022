// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;

import frc.robot.auto.Baseline
import frc.robot.auto.TestDrive
import frc.robot.auto.PathToShooter

import frc.robot.commands.ExampleCommand;
import frc.robot.commands.Drive;
import frc.robot.commands.Shoot;
import frc.robot.commands.PrototypeSpin;
import frc.robot.commands.AutoAlign;

import frc.robot.subsystems.ExampleSubsystem;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.PrototypeMotor;
import frc.robot.subsystems.Vision;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

import frc.robot.commands.RamseteAction
import edu.wpi.first.math.geometry.Pose2d
import edu.wpi.first.math.geometry.Rotation2d
import edu.wpi.first.wpilibj.smartdashboard.Field2d

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
class RobotContainer(tab: ShuffleboardTab) {
  // The robot's subsystems and commands are defined here...
  private val m_exampleSubsystem = ExampleSubsystem();
  private val m_drivetrain = Drivetrain(tab);
  private val m_shooter = Shooter(tab);
  private val m_protomotor = PrototypeMotor(tab);
  private val m_vision = Vision(tab, m_drivetrain);

  private val m_autoCommand = ExampleCommand(m_exampleSubsystem);
  private val m_baseline = Baseline()
  
  val autoSelector = SendableChooser<SequentialCommandGroup>()
  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  init {
    // Configure the button bindings
    val driver = XboxController(0);
    configureButtonBindings(driver);
    m_drivetrain.setDefaultCommand(Drive(m_drivetrain, driver));
    println("set default")
    tab.add("Auto Selector", autoSelector)
    autoSelector.setDefaultOption("Baseline", m_baseline)
    autoSelector.addOption("Baseline", m_baseline)
    autoSelector.addOption("Test Drive", TestDrive(m_drivetrain, m_shooter, m_vision))

    var m_field = Field2d()

    tab.add("field", m_field)

    m_field.getObject("traj").setTrajectory(
      RamseteAction(m_drivetrain, listOf( // negative x is forward, positive x is backward, positive y is left, negative y is right
      Pose2d(0.0, 0.0, Rotation2d(0.0)), 
      Pose2d(0.8, 0.0, Rotation2d(0.0))
      ), true).trajectory)
  }

  /**
   * Use this method to define your button->command mappings. Buttons can be created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a {@link
   * edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */

  fun onAuto() {
    m_drivetrain.brakeMode = true
  }

  fun onTeleop() {
    m_drivetrain.brakeMode = false
  }
  
  fun configureButtonBindings(driver: XboxController) {
    //val aButton: JoystickButton = JoystickButton(driver, XboxController.Button.kA.value)
    //aButton.whenHeld(Shoot(m_shooter, 0.0));
    val aButton: JoystickButton = JoystickButton(driver, XboxController.Button.kA.value)
    aButton.whenPressed(PathToShooter(m_drivetrain))
    val bButton: JoystickButton = JoystickButton(driver, XboxController.Button.kB.value)
    bButton.whenHeld(Shoot(m_shooter));
    val xButton: JoystickButton = JoystickButton(driver, XboxController.Button.kX.value)
    xButton.whenHeld(PrototypeSpin(m_protomotor));
    val yButton: JoystickButton = JoystickButton(driver, XboxController.Button.kY.value)
    yButton.toggleWhenPressed(AutoAlign(m_vision, m_drivetrain))

  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  fun getAutonomousCommand(): Command {

    // An ExampleCommand will run in autonomous
    return autoSelector.getSelected() ?: m_baseline
  }
}
