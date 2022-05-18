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

import frc.robot.RobotContainer;
import frc.robot.subsystems.*;

class ChangeMultiplier(_drivetrain: ExampleDrivetrain,_multiplier: Double) : CommandBase() {

  private val multiplier: Double = _multiplier;
  private val drivetrain: ExampleDrivetrain = _drivetrain;

  init {
    //println("this is run when kotlin makes the command")
  }

  override fun initialize() {
      drivetrain.multiplier = multiplier
  }

  override fun execute() {
      
  }

  override fun isFinished(): Boolean {
      println("this is run to check whether the command should finish")
      return true
  }

  override fun end(interrupted: Boolean) {
    
  }
}
