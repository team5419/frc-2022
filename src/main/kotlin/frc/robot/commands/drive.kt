package frc.robot.commands; 

// import frc.robot.subsystems.Vision;
// import frc.robot.subsystems.Indexer
// import frc.robot.subsystems.Feeder
// import frc.robot.subsystems.Shooter
import edu.wpi.first.wpilibj2.command.CommandBase;

import edu.wpi.first.wpilibj.XboxController;
import com.ctre.phoenix.motorcontrol.*

// import edu.wpi.first.wpilibj.XboxController;
// import frc.robot.classes.DriveSignal;
// import frc.robot.LookupEntry;
// import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard
// import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab
// import edu.wpi.first.wpilibj.Timer
// import frc.robot.classes.RGB;
// import frc.robot.subsystems.Lights;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;

//import frc.robot.classes.SubsystemHolder;

import frc.robot.subsystems.ExampleDrivetrain;
import frc.robot.RobotContainer;

class Drive(_drivetrain: ExampleDrivetrain, _controller: XboxController) : CommandBase() {

  private val drivetrain: ExampleDrivetrain = _drivetrain
  private val controller: XboxController = _controller

  init {
    //println("this is run when kotlin makes the command")
    addRequirements(_drivetrain)
  }

  override fun initialize() {
      //println("this is run when the command starts")
  }

  override fun execute() {
      //println("this is run every frame when the command is running") //controller.getLeftY().toDouble()
      drivetrain.leaderLeft.set(ControlMode.PercentOutput, drivetrain.multiplier*(controller.getLeftY() - 0.5*controller.getRightX())) // the % output of the motor, between -1 and 1
      drivetrain.leaderRight.set(ControlMode.PercentOutput, -drivetrain.multiplier*(controller.getLeftY() + 0.5*controller.getRightX())) // the % output of the motor, between -1 and 1
  }

  override fun isFinished(): Boolean {
      println("this is run to check whether the command should finish")
      return false
  }

  override fun end(interrupted: Boolean) {
    drivetrain.leaderLeft.set(ControlMode.PercentOutput, 0.0) // the % output of the motor, between -1 and 1
    drivetrain.leaderRight.set(ControlMode.PercentOutput, 0.0) // the % output of the motor, between -1 and 1
  }
}
