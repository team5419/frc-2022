package frc.robot.commands; 

import frc.robot.subsystems.PrototypeMotor;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.XboxController;

class PrototypeSpin(_motor: PrototypeMotor) : CommandBase() {
  private val motor: PrototypeMotor = _motor;

  init {
    addRequirements(_motor);
  }

  override fun initialize() {}

  override fun execute() {
    motor.shoot(1000.0);
  }

  override fun end(interrupted: Boolean) {}
}
