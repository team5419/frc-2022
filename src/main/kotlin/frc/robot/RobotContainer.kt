package frc.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;

//import frc.robot.auto.*;
import frc.robot.commands.*;
import frc.robot.subsystems.*;
//import frc.robot.classes.SubsystemHolder;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

//import frc.robot.commands.RamseteAction
import frc.robot.Util;
import edu.wpi.first.math.geometry.Pose2d
import edu.wpi.first.math.geometry.Rotation2d
import edu.wpi.first.wpilibj.smartdashboard.Field2d

import frc.robot.ShooterConstants

// robot structure declared here (subsystems, commands, button mappings)
class RobotContainer() {

  // subsystems
  public val m_drivetrain = Drivetrain(true);
  // creates a tab in shuffleboard to select autonomous routine
  // val autoSelector = SendableChooser<SequentialCommandGroup>()
  val driver = XboxController(0);
  // val codriver = XboxController(1);
  private val autoCommand: Command;

  init {
    configureButtonBindings(driver);
    setDefaults();
    autoCommand = Util.generateRamsete(m_drivetrain, listOf(
      Pose2d(0.0, 0.0, Rotation2d(0.0)),
      Pose2d(10.0, 10.0, Rotation2d(0.0))
    ));
  }
  
  fun configureButtonBindings(driver: XboxController) {
    
  }


  // select autonomous command
  fun getAutonomousCommand(): Command {
     return autoCommand;
   }

  fun setDefaults() {
      // set default commands
      m_drivetrain.setDefaultCommand(Drive(m_drivetrain, driver, false));
      // m_subsystems.climber.setDefaultCommand(Climb(m_subsystems, codriver));
      // //m_feeder.setDefaultCommand(Feed(m_feeder));
      // m_subsystems.indexer.setDefaultCommand(DefaultIndex(m_subsystems));
      //m_deploy.setDefaultCommand(DefaultDeploy(m_deploy));
  }

  fun lightsOff() {
    //m_subsystems.lights.stop();
  }
}