package frc.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;

import frc.robot.auto.*;
import frc.robot.commands.*;
import frc.robot.subsystems.*;
import frc.robot.classes.SubsystemHolder;

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
  public val m_drivetrain = Drivetrain(tab);
  // private val m_shooter = Shooter(tab);
  // private val m_vision = Vision(tab, m_drivetrain);
  // private val m_indexer = Indexer(tab);
  // private val m_climber = Climber(tab);
  // private val m_feeder = Feeder(tab);
  // private val m_intake = Intake(tab);
  // private val m_lights = Lights(tab);
  // private val m_deploy = DeploySubsystem(tab);
  public val m_subsystems = SubsystemHolder(m_drivetrain, Shooter(tab), Intake(tab), DeploySubsystem(tab), Lights(tab), Vision(tab, m_drivetrain), Climber(tab), Feeder(tab), Indexer(tab))

  // creates a tab in shuffleboard to select autonomous routine
  val autoSelector = SendableChooser<SequentialCommandGroup>()
  val driver = XboxController(0);
  val codriver = XboxController(1);

  init {

    configureButtonBindings(driver, codriver);
    setDefaults();
    
    // create and add autonomous routines to selector in shuffleboard
    Shuffleboard.getTab("Limelight").add("Limelight link", "10.54.19.88:5801/");
    tab.add("Auto Selector", autoSelector).withPosition(8, 3).withSize(2, 1);
    autoSelector.setDefaultOption("Baseline", Baseline())
    autoSelector.addOption("Baseline", Baseline())
    autoSelector.addOption("Two Ball Auto", TwoBallAuto(m_subsystems, driver))
    autoSelector.addOption("Four Ball Auto", FourBallAuto(m_subsystems, driver))
    autoSelector.addOption("Five Ball Auto", FiveBallAuto(m_subsystems, driver))
    //autoSelector.addOption("Pre-Match Check", PreMatchCheck(m_climber, m_drivetrain, m_feeder, m_indexer, m_intake, m_shooter))

  }
  
  fun configureButtonBindings(driver: XboxController, codriver: XboxController) {

    // DRIVER CONTROLS ---------------------------------------------------------------->

    // auto-align (hold A)
    val aButtonDriver: JoystickButton = JoystickButton(driver, XboxController.Button.kA.value)
    aButtonDriver.whenHeld(AlignSpin(m_subsystems))

    // shoot (hold right bumper)
    val rBumperDriver: JoystickButton = JoystickButton(driver, XboxController.Button.kRightBumper.value)
    rBumperDriver.whenHeld(Shoot(m_subsystems, driver));

    // intake (hold X)
    val xButtonDriver: JoystickButton = JoystickButton(driver, XboxController.Button.kX.value)
    xButtonDriver.toggleWhenPressed(RunIntake(m_subsystems))

    // drivetrain slow mode (hold left bumper)
    val lBumperDriver : JoystickButton = JoystickButton(driver, XboxController.Button.kLeftBumper.value)
    lBumperDriver.whenHeld(Drive(m_subsystems, driver, true))

    // defense mode (hold B) 
    val bButtonDriver: JoystickButton = JoystickButton(driver, XboxController.Button.kB.value)
    bButtonDriver.whenHeld(ToggleCurrent(m_subsystems));

    // safe zone shot (hold Y)
    val yButtonDriver: JoystickButton = JoystickButton(driver, XboxController.Button.kY.value)
    yButtonDriver.whenHeld(AutoAlignAndShoot(m_subsystems, 20000.0, 20000.0, driver))

    // CO-DRIVER CONTROLS ---------------------------------------------------------------->
    
    // outtake (hold Y)
    val yButtonCodriver: JoystickButton = JoystickButton(codriver, XboxController.Button.kY.value)
    yButtonCodriver.whenHeld(Outtake(m_subsystems))

    // mini shoot (hold A)
    val aButtonCodriver: JoystickButton = JoystickButton(codriver, XboxController.Button.kA.value)
    aButtonCodriver.whenHeld(Shoot(m_subsystems, driver, 7000.0, 7000.0))

    // break mode (press B)
    val bButtonCodriver: JoystickButton = JoystickButton(codriver, XboxController.Button.kB.value)
    bButtonCodriver.toggleWhenPressed(Rainbow(m_subsystems))
    //bButtonCodriver.whenPressed(ToggleBrakeMode(m_subsystems))

    // raise intake (hold right bumper)
    val rBumperCodriver: JoystickButton = JoystickButton(codriver, XboxController.Button.kRightBumper.value)
    rBumperCodriver.whenHeld(Deploy(m_subsystems, -0.8))

    // lower intake (hold left bumper)
    val lBumperCodriver: JoystickButton = JoystickButton(codriver, XboxController.Button.kLeftBumper.value)
    lBumperCodriver.whenHeld(Deploy(m_subsystems, 0.8))

    // invert drivetrain (press X)
    
    val xButtonCodriver: JoystickButton = JoystickButton(codriver, XboxController.Button.kX.value)
    
    xButtonCodriver.whenPressed(InvertDrive(m_subsystems))
    //xButtonCodriver.whenHeld(ClimbButton(m_climber))
  }


  // select autonomous command
  fun getAutonomousCommand(): Command {
    return autoSelector.getSelected() ?: Baseline()
  }

  fun setDefaults() {
      // set default commands
      m_subsystems.drivetrain.setDefaultCommand(Drive(m_subsystems, driver, false));
      m_subsystems.climber.setDefaultCommand(Climb(m_subsystems, codriver));
      //m_feeder.setDefaultCommand(Feed(m_feeder));
      m_subsystems.indexer.setDefaultCommand(DefaultIndex(m_subsystems));
      //m_deploy.setDefaultCommand(DefaultDeploy(m_deploy));
  }

  fun lightsOff() {
    m_subsystems.lights.stop();
  }
}