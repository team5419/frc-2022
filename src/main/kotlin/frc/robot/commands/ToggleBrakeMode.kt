package frc.robot.commands; 

import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Indexer;
import frc.robot.subsystems.Drivetrain;
import frc.robot.FeederConstants;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.subsystems.Lights;
import frc.robot.classes.RGB;
import com.ctre.phoenix.motorcontrol.can.TalonFX
import com.ctre.phoenix.motorcontrol.can.TalonSRX
import com.ctre.phoenix.motorcontrol.*


class ToggleBrakeMode(_drivetrain: Drivetrain) : CommandBase() {
  private val drivetrain: Drivetrain = _drivetrain;

  init {
  }

  override fun initialize() {
    drivetrain.brakeMode = false
    println(drivetrain.brakeMode)
  }

  override fun execute() {
  }

  override fun isFinished(): Boolean {
    return true;
  }

  override fun end(interrupted: Boolean) {
  }
}
