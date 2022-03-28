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
  //private val m_camera = Camera(tab);

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
    Shuffleboard.getTab("Limelight").add("Limelight link", "10.54.19.88:5801/");
    autoSelector.setDefaultOption("Baseline", m_baseline)
    autoSelector.addOption("Baseline", m_baseline)
    autoSelector.addOption("Two Ball Auto", TwoBallAuto(m_drivetrain, m_shooter, m_vision, m_indexer, m_feeder, m_intake, m_lights, driver))
    autoSelector.addOption("Four Ball Auto", FourBallAuto(m_drivetrain, m_shooter, m_vision, m_indexer, m_feeder, m_intake, m_lights, driver))
    autoSelector.addOption("Five Ball Auto", FiveBallAuto(m_drivetrain, m_shooter, m_vision, m_indexer, m_feeder, m_intake, m_lights, driver))
    autoSelector.addOption("Pre-Match Check", m_autocheck)

    // field simulation (in progress)
    // var m_field = Field2d()
    // tab.add("field", m_field)
    // m_field.getObject("traj").setTrajectory(
    //   RamseteAction(m_drivetrain, listOf( // negative x is forward, positive x is backward, positive y is left, negative y is right
    //   Pose2d(0.0, 0.0, Rotation2d(0.0)), 
    //   Pose2d(0.8, 0.0, Rotation2d(0.0))
    //   ), true).trajectory)

    setDefaults();
  }
  
  fun configureButtonBindings(driver: XboxController, codriver: XboxController) {

    // enable drivetrain slow mode (hold left bumper)
    val lBumper : JoystickButton = JoystickButton(driver, XboxController.Button.kLeftBumper.value)
    lBumper.whenHeld(Drive(m_drivetrain, driver, true))

    // intake and run feeder (hold X)
    val xButton: JoystickButton = JoystickButton(driver, XboxController.Button.kX.value)
    xButton.toggleWhenPressed(RunIntake(m_intake, m_feeder))

    val yButton2: JoystickButton = JoystickButton(driver, XboxController.Button.kY.value)
    yButton2.whenHeld(Shoot(m_vision, m_drivetrain, m_shooter, m_indexer, m_feeder, m_lights, driver, 20000.0, 20000.0))

    // defense mode (hold B button) 
    val bButton: JoystickButton = JoystickButton(driver, XboxController.Button.kB.value)
    bButton.whenHeld(ToggleCurrent(m_drivetrain));

    // shoot (press right bumper)
    val rBumper: JoystickButton = JoystickButton(driver, XboxController.Button.kRightBumper.value)
    rBumper.whenHeld(Shoot(m_vision, m_drivetrain, m_shooter, m_indexer, m_feeder, m_lights, driver));

    // safe zone shot (press a button)
    val aButton: JoystickButton = JoystickButton(driver, XboxController.Button.kA.value)
    aButton.whenHeld(AlignSpin(m_vision, m_drivetrain, m_shooter, m_indexer, m_feeder, m_lights))

    // outtake (hold Y)
    val yButton: JoystickButton = JoystickButton(codriver, XboxController.Button.kY.value)
    yButton.whenHeld(Outtake(m_feeder, m_indexer, m_intake))

    // mini shoot
    val aButton2: JoystickButton = JoystickButton(codriver, XboxController.Button.kA.value)
    aButton2.whenHeld(Shoot(m_vision, m_drivetrain, m_shooter, m_indexer, m_feeder, m_lights, driver, 7000.0, 7000.0))

    // break mode (press b button)
    val bButton2: JoystickButton = JoystickButton(codriver, XboxController.Button.kB.value)
    bButton2.whenPressed(ToggleBrakeMode(m_drivetrain))

    // edit shooter multiplier
    val rBumper2: JoystickButton = JoystickButton(codriver, XboxController.Button.kRightBumper.value)
    //rBumper2.whenPressed(ShooterMultiply(m_shooter, 0.1));
    rBumper2.whenHeld(Deploy(m_intake, -0.8))

    val lBumper2: JoystickButton = JoystickButton(codriver, XboxController.Button.kLeftBumper.value)
    //lBumper2.whenPressed(ShooterMultiply(m_shooter, -0.1));
    lBumper2.whenHeld(Deploy(m_intake, 0.8))

    //val xButton2: JoystickButton = JoystickButton(codriver, XboxController.Button.kX.value)
    val xButton2: JoystickButton = JoystickButton(codriver, XboxController.Button.kX.value)
    xButton2.whenPressed(InvertDrive(m_drivetrain))
  

  }


  // select autonomous command
  fun getAutonomousCommand(): Command {
    return autoSelector.getSelected() ?: m_baseline
  }

  fun setDefaults() {
      // call drive command by default
      m_drivetrain.setDefaultCommand(Drive(m_drivetrain, driver));
      m_climber.setDefaultCommand(Climb(m_climber, codriver));
      m_feeder.setDefaultCommand(Feed(m_feeder));
      m_indexer.setDefaultCommand(DefaultIndex(m_indexer, m_lights));
  }

  fun lightsOff() {
    m_lights.stop();
  }
}