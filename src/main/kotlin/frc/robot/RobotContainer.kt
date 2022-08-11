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
import edu.wpi.first.math.geometry.Pose2d
import edu.wpi.first.math.geometry.Rotation2d
import edu.wpi.first.wpilibj.smartdashboard.Field2d

import frc.robot.ShooterConstants

// robot structure declared here (subsystems, commands, button mappings)
class RobotContainer() {

  // subsystems
  public val m_drivetrain = Drivetrain();
  // creates a tab in shuffleboard to select autonomous routine
  // val autoSelector = SendableChooser<SequentialCommandGroup>()
  val driver = XboxController(0);
  // val codriver = XboxController(1);

  init {

    configureButtonBindings(driver);
    setDefaults();
    
    // create and add autonomous routines to selector in shuffleboard
    //Shuffleboard.getTab("Limelight").add("Limelight link", "10.54.19.88:5801/");
    // tab.add("Auto Selector", autoSelector).withPosition(8, 3).withSize(2, 1);
    // autoSelector.setDefaultOption("Baseline", Baseline())
    // autoSelector.addOption("Baseline", Baseline())
    // autoSelector.addOption("Two Ball Auto", TwoBallAuto(m_subsystems, driver))
    // autoSelector.addOption("Four Ball Auto", FourBallAuto(m_subsystems, driver))
    // autoSelector.addOption("Four Ball Auto 2", FourBall2(m_subsystems, driver))
    // autoSelector.addOption("Five Ball Auto", FiveBallAuto(m_subsystems, driver))
    //autoSelector.addOption("Pre-Match Check", PreMatchCheck(m_climber, m_drivetrain, m_feeder, m_indexer, m_intake, m_shooter))

  }
  
  fun configureButtonBindings(driver: XboxController) {
    
  }


  // select autonomous command
  // fun getAutonomousCommand(): Command {
  //   return autoSelector.getSelected() ?: Baseline()
  // }

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