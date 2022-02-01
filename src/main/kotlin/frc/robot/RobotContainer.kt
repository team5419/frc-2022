package frc.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;

import frc.robot.auto.Baseline
import frc.robot.auto.TestDrive

import frc.robot.commands.Drive;
import frc.robot.commands.Shoot;
import frc.robot.commands.PrototypeSpin;
import frc.robot.commands.AutoAlign;
import frc.robot.commands.Feed;
import frc.robot.commands.Climb;

import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.PrototypeMotor;
import frc.robot.subsystems.Vision;
import frc.robot.subsystems.Feeder;
import frc.robot.subsystems.Climber;
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

// robot structure declared here (subsystems, commands, button mappings)
class RobotContainer(tab: ShuffleboardTab) {

  // subsystems
  private val m_drivetrain = Drivetrain(tab);
  private val m_shooter = Shooter(tab);
  private val m_protomotor = PrototypeMotor(tab);
  private val m_vision = Vision(tab, m_drivetrain);
  private val m_feeder = Feeder(tab);
  private val m_climber = Climber(tab);

  // default autonomous routine
  private val m_baseline = Baseline()

  // creates a tab in shuffleboard to select autonomous routine
  val autoSelector = SendableChooser<SequentialCommandGroup>()

  init {

    // configure the button bindings
    val driver = XboxController(0);
    val codriver = XboxController(1);
    val cocodriver = XboxController(2);
    configureButtonBindings(driver);

    // call drive command by default
    m_drivetrain.setDefaultCommand(Drive(m_drivetrain, driver));
    m_climber.setDefaultCommand(Climb(m_climber, codriver, cocodriver));

    // create and add autonomous routines to selector in shuffleboard
    tab.add("Auto Selector", autoSelector)
    autoSelector.setDefaultOption("Baseline", m_baseline)
    autoSelector.addOption("Baseline", m_baseline)
    autoSelector.addOption("Test Drive", TestDrive(m_drivetrain, m_shooter, m_vision))

    // field simulation (in progress)
    var m_field = Field2d()
    tab.add("field", m_field)
    m_field.getObject("traj").setTrajectory(
      RamseteAction(m_drivetrain, listOf( // negative x is forward, positive x is backward, positive y is left, negative y is right
      Pose2d(0.0, 0.0, Rotation2d(0.0)), 
      Pose2d(0.8, 0.0, Rotation2d(0.0))
      ), true).trajectory)
  }

  fun onAuto() {
    m_drivetrain.brakeMode = true
  }

  fun onTeleop() {
    m_drivetrain.brakeMode = false
  }
  
  fun configureButtonBindings(driver: XboxController) {

    // shoot (hold B)
    val bButton: JoystickButton = JoystickButton(driver, XboxController.Button.kB.value)
    bButton.whenHeld(Shoot(m_shooter)); 

    // spin prototype motor (hold B)
    val xButton: JoystickButton = JoystickButton(driver, XboxController.Button.kX.value)
    xButton.whenHeld(PrototypeSpin(m_protomotor));

    // auto-align
    val yButton: JoystickButton = JoystickButton(driver, XboxController.Button.kY.value)
    yButton.toggleWhenPressed(AutoAlignTurn(m_vision, m_drivetrain, m_shooter))

    val aButton: JoystickButton = JoystickButton(driver, XboxController.Button.kA.value)
    aButton.whenHeld(Feed(m_feeder))
  }

  // select autonomous command
  fun getAutonomousCommand(): Command {
    return autoSelector.getSelected() ?: m_baseline
  }
}