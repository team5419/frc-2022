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

import edu.wpi.first.math.geometry.Pose2d
import edu.wpi.first.math.geometry.Rotation2d
import edu.wpi.first.wpilibj.smartdashboard.Field2d

import frc.robot.ShooterConstants

// robot structure declared here (subsystems, commands, button mappings)
class RobotContainer(tab: ShuffleboardTab) {

  // subsystems
  public val m_drivetrain = Drivetrain(false);
  public val m_feeder = Feeder(tab);
  public val m_subsystems = SubsystemHolder(m_drivetrain, Shooter(tab), Intake(tab), DeploySubsystem(tab), Lights(tab), Vision(tab, m_drivetrain), Climber(tab), m_feeder, Indexer(tab))

  // creates a tab in shuffleboard to select autonomous routine
  val autoSelector = SendableChooser<SequentialCommandGroup>()
  val driver = XboxController(0);
  val codriver = XboxController(1);

  init {

    configureButtonBindings(driver, codriver);
    setDefaults();
    
    tab.add("Auto Selector", autoSelector).withPosition(8, 3).withSize(2, 1);
    autoSelector.setDefaultOption("Baseline", Baseline())
    autoSelector.addOption("Baseline", Baseline())
    autoSelector.addOption("Five Ball", FiveBall(m_subsystems, driver))
    autoSelector.addOption("Two Ball", TwoBallAuto(m_subsystems, driver))
    autoSelector.addOption("Go To Zero", GoToZero(m_subsystems, driver, 24.3))
  }
  
  fun configureButtonBindings(driver: XboxController, codriver: XboxController) {

    // DRIVER CONTROLS ---------------------------------------------------------------->
val bButtonDriver: JoystickButton = JoystickButton(driver, XboxController.Button.kB.value)
    bButtonDriver.whenHeld(Brake(m_subsystems.drivetrain))
    // auto-align (hold A)
    val aButtonDriver: JoystickButton = JoystickButton(driver, XboxController.Button.kA.value)
    aButtonDriver.whenHeld(AlignSpin(m_subsystems, driver, -1.0, -1.0, false))

    // shoot (hold right bumper)
    val rBumperDriver: JoystickButton = JoystickButton(driver, XboxController.Button.kRightBumper.value)
    rBumperDriver.whenHeld(Shoot(m_subsystems, driver, ShooterConstants.mainVelocity, ShooterConstants.kickerVelocity));

    // intake (hold X)
    val xButtonDriver: JoystickButton = JoystickButton(driver, XboxController.Button.kX.value)
    xButtonDriver.toggleWhenPressed(RunIntake(m_subsystems))

    // drivetrain slow mode (hold left bumper)
    val lBumperDriver : JoystickButton = JoystickButton(driver, XboxController.Button.kLeftBumper.value)
    lBumperDriver.whenHeld(SlowMode(m_subsystems.drivetrain))

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
    bButtonCodriver.whenPressed(ReverseIntake(m_subsystems))
    //bButtonCodriver.whenPressed(ToggleBrakeMode(m_subsystems))

    val xButtonCodriver: JoystickButton = JoystickButton(codriver, XboxController.Button.kX.value)
    xButtonCodriver.whenPressed(ResetGyro(m_subsystems.drivetrain))

    // raise intake (hold right bumper)
    val rBumperCodriver: JoystickButton = JoystickButton(codriver, XboxController.Button.kRightBumper.value)
    rBumperCodriver.whenHeld(Deploy(m_subsystems, -0.8))

    // lower intake (hold left bumper)
    val lBumperCodriver: JoystickButton = JoystickButton(codriver, XboxController.Button.kLeftBumper.value)
    lBumperCodriver.whenHeld(Deploy(m_subsystems, 0.8))

    /*val backButtonCodriver: JoystickButton = JoystickButton(codriver, XboxController.Button.kBackButton.value)
    backButtonCodriver.whenPressed*/
  }


  // select autonomous command
  fun getAutonomousCommand(): Command {
    return autoSelector.getSelected() ?: Baseline()
  }

  fun setDefaults() {
      // set default commands
      m_subsystems.drivetrain.setDefaultCommand(Drive(m_subsystems, driver, false));
      m_subsystems.climber.setDefaultCommand(Climb(m_subsystems, codriver));
      m_subsystems.indexer.setDefaultCommand(DefaultIndex(m_subsystems));
      //m_deploy.setDefaultCommand(DefaultDeploy(m_deploy));
  }

  fun lightsOff() {
    m_subsystems.lights.stop();
  }
}