package frc.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;

import frc.robot.commands.*;

import frc.robot.subsystems.*;
import frc.robot.auto.*;

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

  // creates a tab in shuffleboard to select autonomous routine
  val autoSelector = SendableChooser<SequentialCommandGroup>()
  val autoSelector2 = SendableChooser<SequentialCommandGroup>()
  val driver = XboxController(0);
  val codriver = XboxController(1);
  public val drivetrain_: Drivetrain = Drivetrain();
  public val climber_: Climber = Climber(tab);
  public val intake_: IntakeSub = IntakeSub(tab);
  public val catapult_: Catapult = Catapult(tab);

  init {
    // creates a tab in shuffleboard to select autonomous routine
    // create and add autonomous routines to selector in shuffleboard
    //Shuffleboard.getTab("Limelight").add("Limelight link", "10.54.19.88:5801/");
    tab.add("Auto Selector", autoSelector).withPosition(8, 3).withSize(2, 1);
    autoSelector.setDefaultOption("sLAY", AutoSlay(drivetrain_, driver, catapult_, intake_))
    autoSelector.addOption("System Check", SystemCheck(drivetrain_, catapult_, intake_, climber_, codriver))
    autoSelector.addOption("One Ball Auto", OneBall(drivetrain_, catapult_, intake_))
    autoSelector.addOption("Taxi", Taxi(drivetrain_, catapult_, intake_))

    tab.add("Climb Selector", autoSelector2).withPosition(8, 3).withSize(2, 1);
    // autoSelector2.addOption("Mid Bar Auto", MidBarAuto(climber_, codriver))



    setDefaults();
  }

  fun setDefaults(){
    drivetrain_.setDefaultCommand(Drive(driver, drivetrain_, false))
    climber_.setDefaultCommand(Climb(climber_, codriver))
    //intake_.setDefaultCommand(Feed(intake_, driver))

    val lBumper: JoystickButton = JoystickButton(driver, XboxController.Button.kLeftBumper.value)
    lBumper.whenHeld(Drive(driver, drivetrain_, true))

    // val xButton: JoystickButton = JoystickButton(driver, XboxController.Button.kX.value)
    // xButton.toggleWhenPressed(Feed(intake_, driver))

    

    // val xButton: JoystickButton = JoystickButton(driver, XboxController.Button.kX.value)
    // xButton.toggleWhenPressed(Shoot(catapult_))

    // val yButton: JoystickButton = JoystickButton(codriver, XboxController.Button.kY.value)
    // yButton.toggleWhenPressed(MidBarAuto(climber_, codriver))

    // val xButton: JoystickButton = JoystickButton(codriver, XboxController.Button.kX.value)
    // xButton.whenHeld(Feed(intake_))

    // val bButton: JoystickButton = JoystickButton(codriver, XboxController.Button.kB.value)
    // bButton.whenHeld(Feed(intake_, true))

    val aButton: JoystickButton = JoystickButton(driver, XboxController.Button.kA.value)
    aButton.toggleWhenPressed(Intake(intake_))

    val bButton: JoystickButton = JoystickButton(driver, XboxController.Button.kB.value)
    bButton.toggleWhenPressed(Deploy(intake_))

  }


  // select autonomous command
  fun getAutonomousCommand(): Command {
    println(autoSelector.getSelected())
    return autoSelector.getSelected()?: Baseline();
  }
}