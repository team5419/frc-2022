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




class Turn(_subsystems: SubsystemHolder) : CommandBase() {
  private val subsystems: SubsystemHolder = _subsystems
  private var angle : Double = 0.0
  private var targetAngle : Double = 0.0


  init {
  }

  override fun initialize() {
    subsystems.drivetrain.brakeMode = true;

    angle = subsystems.drivetrain.angle

    subsystems.drivetrain.originalAngle = angle

    targetAngle = angle + Random.nextDouble(-45.0, 45.0)
  }


  override fun execute() {

    angle = subsystems.drivetrain.angle

    println("Current angle: " + angle)
    println("Target angle: " + targetAngle)

    if (angle != targetAngle){  

        //subsystems.drivetrain.setPercent((targetAngle - angle)/500 + .1, -(targetAngle - angle)/500 + .1)
        subsystems.drivetrain.setPercent(-sign(targetAngle - angle)*.1, sign(targetAngle - angle)*.1)
    }
    
  }

  override fun isFinished() : Boolean {
    //return abs(subsystems.drivetrain.angle - targetAngle) <= 10
    println("original: " + subsystems.drivetrain.originalAngle)
    if (subsystems.drivetrain.originalAngle > targetAngle){
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
    println("Ended turn")
    subsystems.drivetrain.brakeMode = false;
  }
}
