package frc.robot.commands; 

import frc.robot.subsystems.Vision;
import frc.robot.subsystems.Drivetrain
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.XboxController;
import frc.robot.classes.DriveSignal;
import frc.robot.subsystems.Shooter;
import frc.robot.LookupEntry;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab
import edu.wpi.first.wpilibj.Timer
import frc.robot.classes.RGB;
import frc.robot.subsystems.Lights;
import frc.robot.classes.SubsystemHolder;


import kotlin.math.abs
import kotlin.math.sin
import kotlin.random.Random
import kotlin.math.sign

//im citrus service


class TurnBack(_subsystems: SubsystemHolder) : CommandBase() {
  private val subsystems: SubsystemHolder = _subsystems
  private var angle : Double = 0.0
  private var targetAngle : Double = 0.0
  private var starting: Double = 0.0

  init {
  }

  override fun initialize() {
    subsystems.drivetrain.brakeMode = true;

    angle = subsystems.drivetrain.angle

    targetAngle = subsystems.drivetrain.originalAngle
    starting = angle
  }

  override fun execute() {

    angle = subsystems.drivetrain.angle

    if (angle != targetAngle){
      //subsystems.drivetrain.setPercent((targetAngle - angle)/500 + .1, -(targetAngle - angle)/500 + .1)
        subsystems.drivetrain.setPercent(-sign(targetAngle - angle)*.1, sign(targetAngle - angle)*.1)
    }
    
  }

  override fun isFinished() : Boolean {
    println("original: " + starting)
    if (starting > targetAngle){
      println("PAst desired angle (going left) -- target angle: " + targetAngle)
      println(subsystems.drivetrain.angle < targetAngle)
      return subsystems.drivetrain.angle < targetAngle
    } else {
      println("PAst desired angle (going right) -- target angle: " + targetAngle)
      println(subsystems.drivetrain.angle > targetAngle)
      return subsystems.drivetrain.angle > targetAngle
    }
  }

  override fun end(interrupted: Boolean) {
      subsystems.drivetrain.setPercent(0.0,0.0)
      
    //subsystems.drivetrain.brakeMode = false
  }
}
