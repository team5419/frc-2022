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
  val driver = XboxController(0);
  val codriver = XboxController(1);
  public val drivetrain_ : Drivetrain = Drivetrain();
  public val climber_: Climber = Climber(tab);

  init {

       // creates a tab in shuffleboard to select autonomous routine
      val autoSelector = SendableChooser<SequentialCommandGroup>()

      // create and add autonomous routines to selector in shuffleboard
      //Shuffleboard.getTab("Limelight").add("Limelight link", "10.54.19.88:5801/");
      tab.add("Auto Selector", autoSelector).withPosition(8, 3).withSize(2, 1);
      autoSelector.setDefaultOption("sLAY", AutoSlay(drivetrain_, driver))
      //autoSelector.add("sLAY2", AutoSlay2(drivetrain_, driver))


    setDefaults();
    
  }

  fun setDefaults(){
    drivetrain_.setDefaultCommand(Drive(driver, drivetrain_))
    climber_.setDefaultCommand(Climb(climber_, codriver))
  }


  // select autonomous command
  fun getAutonomousCommand(): Command {
    val gotten: Command? =  autoSelector.getSelected();
    println(gotten);
    return if (gotten == null) Baseline() else gotten
  }
}