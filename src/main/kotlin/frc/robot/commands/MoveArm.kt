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


  var throttle: Double = 1.0; //???

  val f: Double = 0.5;

  init {

    addRequirements(_climber);

    if (movingDown) {
      throttle = -throttle;
    }
  }

  override fun initialize() {
    
  }

  override fun execute() {

    if (arm.equals("left")) {
        climber.leftArm.set(ControlMode.PercentOutput, -throttle * f);
    }
    if (arm.equals("right")) {
        climber.rightArm.set(ControlMode.PercentOutput, -throttle * f);
    } 
   
  }

  override fun end(interrupted: Boolean) {}

  override fun isFinished(): Boolean {

    var actualPos: Double = if (arm.equals("left"))
      climber.leftArm.getSelectedSensorPosition(0)
    else
      climber.rightArm.getSelectedSensorPosition(0)
    
    
    return movingDown == (actualPos < wantedPos);

  }
}