package frc.robot.commands; 

import frc.robot.subsystems.Climber;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.XboxController;

import com.ctre.phoenix.motorcontrol.*

class MoveArm(_climber: Climber, _codriver: XboxController, _arm: String, _movingDown: Boolean, _wantedPos: Double) : CommandBase() {
  private val climber: Climber = _climber;
  private val codriver: XboxController = _codriver;
  private val arm: String = _arm;
  private var movingDown: Boolean = _movingDown;
  private var wantedPos: Double = _wantedPos;
 
  var actualPos: Double;
  var difference: Double;

  val throttle: Double = 1.0; //???

  val f: Double = 0.5;

  val highPosMid: Double;
  val finalPosMid: Double;

  init {
    highPosMid = 0.0;
    finalPosMid = 0.0;

    actualPos = 0.0;
    difference = 0.0;

    addRequirements(_climber);
  }

  override fun initialize() {
    
  }

  override fun execute() {
    if (arm.equals("left")) {
        actualPos = climber.leftArm.getSelectedSensorPosition(0)
    }
    if (arm.equals("right")) {
        actualPos = climber.rightArm.getSelectedSensorPosition(0)
    } 
    difference = wantedPos - actualPos;
    
    if (movingDown) {
        climber.leftArm.setInverted(true)
        climber.rightArm.setInverted(true)
    }

    if (difference >= 50) {
        if (arm.equals("left")) {
            climber.leftArm.set(ControlMode.PercentOutput, -throttle * f);
        }
        if (arm.equals("right")) {
            climber.rightArm.set(ControlMode.PercentOutput, -throttle * f);
        } 
    }
  }

  override fun end(interrupted: Boolean) {}

  override fun isFinished(): Boolean {
    return false;
  }
}