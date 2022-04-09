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
import frc.robot.classes.SubsystemHolder

class ToggleBrakeMode(_subsystems: SubsystemHolder) : CommandBase() {
  private val subsystems: SubsystemHolder = _subsystems

  init {
  }

  override fun initialize() {
    subsystems.drivetrain.brakeMode = false
  }

  override fun execute() {
  }

  override fun isFinished(): Boolean {
    return true;
  }

  override fun end(interrupted: Boolean) {
  }
}
