package frc.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;

import frc.robot.auto.Baseline
import frc.robot.auto.TestDrive
import frc.robot.auto.PathToShooter

import frc.robot.commands.Drive;
import frc.robot.commands.Shoot;
import frc.robot.commands.PrototypeSpin;
import frc.robot.commands.AutoAlignTurn;

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

import frc.robot.classes.Routine

// robot structure declared here (subsystems, commands, button mappings)
class RobotContainer(tab: ShuffleboardTab) {

  // creates a tab in shuffleboard to select autonomous routine
  val autoSelector = SendableChooser<Routine>()
  // subsystems
  public val m_drivetrain = Drivetrain(tab);
  private val m_shooter = Shooter(tab);
  private val m_protomotor = PrototypeMotor(tab);
  private val m_vision = Vision(tab, m_drivetrain);

  // default autonomous routine
  private val m_baseline = Baseline()

  init {

    // configure the button bindings
    val driver = XboxController(0);
    configureButtonBindings(driver);

    // call drive command by default
    m_drivetrain.setDefaultCommand(Drive(m_drivetrain, driver));

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

    // go to shoot position autonomously (press A)
    val aButton: JoystickButton = JoystickButton(driver, XboxController.Button.kA.value)
    aButton.whenPressed(PathToShooter(m_drivetrain).commandgroup) 

    // shoot (hold B)
    val bButton: JoystickButton = JoystickButton(driver, XboxController.Button.kB.value)
    bButton.whenHeld(Shoot(m_shooter)); 

    // spin prototype motor (hold B)
    val xButton: JoystickButton = JoystickButton(driver, XboxController.Button.kX.value)
    xButton.whenHeld(PrototypeSpin(m_protomotor));

    // auto-align on xz-plane (press Y)
    val yButton: JoystickButton = JoystickButton(driver, XboxController.Button.kY.value)
    yButton.toggleWhenPressed(AutoAlignTurn(m_vision, m_drivetrain))
  }

  // select autonomous command
  fun getAutonomousCommand(): Routine {
    return autoSelector.getSelected() ?: m_baseline
  }
}
