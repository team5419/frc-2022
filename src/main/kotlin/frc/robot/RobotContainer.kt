package frc.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;

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

  // subsystems
  public val m_drivetrain = Drivetrain(tab);
  val driver = XboxController(0);

  init {

    configureButtonBindings(driver);
    setDefaults();

  }
  
  fun configureButtonBindings(driver: XboxController) {
  }

  fun setDefaults() {
  }
}