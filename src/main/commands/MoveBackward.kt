package frc.robot.commands; 

import frc.robot.subsystems.Vision;
import frc.robot.subsystems.Drivetrain
import frc.robot.subsystems.Indexer
import frc.robot.subsystems.Feeder
import frc.robot.subsystems.Shooter
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.XboxController;
import frc.robot.classes.DriveSignal;
import frc.robot.LookupEntry;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab
import edu.wpi.first.wpilibj.Timer
import frc.robot.classes.RGB;
import frc.robot.subsystems.Lights;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;

import frc.robot.classes.SubsystemHolder;

import frc.robot.subsystems.ExampleDrivetrain;

class MoveForward() : CommandBase() {

  init {
    println("this is run when kotlin makes the command")
  }

  override fun initialize() {

      println("this is run when the command starts")
  }

  override fun execute() {
      println("this is run every frame when the command is running")

      // idk which motors spin forwards vs backwards
      ExampleDrivetrain.followerLeft.set(-0.75); // the % output of the motor, between -1 and 1
      ExampleDrivetrain.motorRightFront.set(-0.75); // the % output of the motor, between -1 and 1
      ExampleDrivetrain.motorLeftBack.set(-0.75); // the % output of the motor, between -1 and 1
      ExampleDrivetrain.motorRightBack.set(-0.75); // the % output of the motor, between -1 and 1
      
  }

  override fun isFinished(): Boolean {
      println("this is run to check whether the command should finish")
      return false
  }

  override fun end(interrupted: Boolean) {
    println("this is run when the command ends")
  }
}

