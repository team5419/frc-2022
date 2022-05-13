package frc.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;

//import frc.robot.auto.*;
import frc.robot.commands.*;
import frc.robot.subsystems.*;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

import edu.wpi.first.math.geometry.Pose2d
import edu.wpi.first.math.geometry.Rotation2d
import edu.wpi.first.wpilibj.smartdashboard.Field2d

// robot structure declared here (subsystems, commands, button mappings)
class RobotContainer(tab: ShuffleboardTab) {
  val driver = XboxController(0)
  val codriver = XboxController(1)

  public val throttle = JoystickButton(driver, 1)

  public val drivetrain = ExampleDrivetrain(tab)

  init {
    //throttle.toggleWhenPressed(Drive(drivetrain, driver))
    drivetrain.setDefaultCommand(Drive(drivetrain, driver))
  }
}