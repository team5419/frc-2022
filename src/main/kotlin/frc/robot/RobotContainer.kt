package frc.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;

import frc.robot.auto.*;
import frc.robot.commands.*;
import frc.robot.subsystems.*;

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

import frc.robot.ShooterConstants


// robot structure declared here (subsystems, commands, button mappings)
class RobotContainer(tab: ShuffleboardTab) {

  // subsystems
  private val m_drivetrain = Drivetrain(tab);
  private val m_shooter = Shooter(tab);
  private val m_vision = Vision(tab, m_drivetrain);
  private val m_indexer = Indexer(tab);
  private val m_climber = Climber(tab);
  private val m_feeder = Feeder(tab);
  private val m_intake = Intake(tab);
  private val m_lights = Lights(tab);
  // default autonomous routine
  private val m_baseline = Baseline()
  private val m_autocheck = PreMatchCheck(m_climber, m_drivetrain, m_feeder, m_indexer, m_intake, m_shooter);

  // creates a tab in shuffleboard to select autonomous routine
  val autoSelector = SendableChooser<SequentialCommandGroup>()
  val driver = XboxController(0);
  val codriver = XboxController(1);

  init {
    configureButtonBindings(driver, codriver);
    
    // create and add autonomous routines to selector in shuffleboard
    tab.add("Auto Selector", autoSelector).withPosition(8, 3).withSize(2, 1);
    autoSelector.setDefaultOption("Baseline", m_baseline)
    autoSelector.addOption("Baseline", m_baseline)
    autoSelector.addOption("Two Ball Auto", TwoBallAuto(m_drivetrain, m_shooter, m_vision, m_indexer, m_feeder, m_intake, m_lights))
    autoSelector.addOption("Five Ball Auto", FiveBallAuto(m_drivetrain, m_shooter, m_vision, m_indexer, m_feeder, m_intake, m_lights))
    autoSelector.addOption("Five Ball Auto 2", FiveBallAuto(m_drivetrain, m_shooter, m_vision, m_indexer, m_feeder, m_intake, m_lights))
    autoSelector.addOption("Four Ball Auto", FourBallAuto(m_drivetrain, m_shooter, m_vision, m_indexer, m_feeder, m_intake, m_lights))
    autoSelector.addOption("Pre-Match Check", m_autocheck)

    // field simulation (in progress)
    // var m_field = Field2d()
    // tab.add("field", m_field)
    // m_field.getObject("traj").setTrajectory(
    //   RamseteAction(m_drivetrain, listOf( // negative x is forward, positive x is backward, positive y is left, negative y is right
    //   Pose2d(0.0, 0.0, Rotation2d(0.0)), 
    //   Pose2d(0.8, 0.0, Rotation2d(0.0))
    //   ), true).trajectory)
  }

  fun onAuto() {
    m_drivetrain.brakeMode = true
  }

  fun onTeleop() {
    m_drivetrain.brakeMode = false
  }
  
  fun configureButtonBindings(driver: XboxController, codriver: XboxController) {

    // auto-align (toggle A)
    val aButton: JoystickButton = JoystickButton(driver, XboxController.Button.kA.value)
    aButton.whenHeld(AutoAlign(m_vision, m_drivetrain, m_shooter, m_lights))

    // enable drivetrain slow mode (hold left bumper)
    val lBumper : JoystickButton = JoystickButton(driver, XboxController.Button.kLeftBumper.value)
    lBumper.whenHeld(Drive(m_drivetrain, driver, true))

    // intake and run feeder (toggle X)
    val xButton: JoystickButton = JoystickButton(driver, XboxController.Button.kX.value)
    xButton.toggleWhenPressed(RunIntake(m_intake, m_feeder))

    // outtake (hold Y)
    val yButton: JoystickButton = JoystickButton(driver, XboxController.Button.kY.value)
    yButton.whenHeld(Outtake(m_feeder, m_indexer, m_intake))

    // shoot and run feeder/indexer (hold right bumper) 
    val rBumper: JoystickButton = JoystickButton(driver, XboxController.Button.kRightBumper.value)
    rBumper.whenHeld(ShootAndFeed(m_shooter, m_feeder, m_indexer, m_lights));

    // autonomous climb (hold A button)
    val aButton2: JoystickButton = JoystickButton(codriver, XboxController.Button.kA.value)
    aButton2.whenPressed(TestClimb(m_climber))

    // val bButton: JoystickButton = JoystickButton(codriver, XboxController.Button.kB.value)
    // bButton.whenHeld(RunLights(m_lights))
  }

  // select autonomous command
  fun getAutonomousCommand(): Command {
    return autoSelector.getSelected() ?: m_baseline
  }

  fun setDefaults() {
    if(autoSelector.getSelected() != m_autocheck) {
      // call drive command by default
      m_drivetrain.setDefaultCommand(Drive(m_drivetrain, driver));
      m_climber.setDefaultCommand(Climb(m_climber, codriver));
      m_feeder.setDefaultCommand(Feed(m_feeder));
      m_indexer.setDefaultCommand(DefaultIndex(m_indexer, m_lights));
      m_lights.setDefaultCommand(RunLights(m_lights));
    }
  }
}