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


class ToggleCurrent(_drivetrain: Drivetrain, _current: Double = 40.0) : CommandBase() {
  private val drivetrain: Drivetrain = _drivetrain;
  private val current: Double = _current;

  init {
  }

  override fun initialize() {
    drivetrain.currentLimit = current
    // drivetrain.leftLeader.configSupplyCurrentLimit(SupplyCurrentLimitConfiguration(true, 60.0, 0.0, 0.0), 100);
    // drivetrain.leftFollower.configSupplyCurrentLimit(SupplyCurrentLimitConfiguration(true, 60.0, 0.0, 0.0), 100);
    // drivetrain.rightLeader.configSupplyCurrentLimit(SupplyCurrentLimitConfiguration(true, 60.0, 0.0, 0.0), 100);
    // drivetrain.rightFollower.configSupplyCurrentLimit(SupplyCurrentLimitConfiguration(true, 60.0, 0.0, 0.0), 100);
  }

  override fun execute() {
  }

  override fun isFinished(): Boolean {
    return false;
  }

  override fun end(interrupted: Boolean) {
    drivetrain.currentLimit = 20.0
    // drivetrain.leftLeader.configSupplyCurrentLimit(SupplyCurrentLimitConfiguration(true, 20.0, 0.0, 0.0), 100);
    // drivetrain.leftFollower.configSupplyCurrentLimit(SupplyCurrentLimitConfiguration(true, 20.0, 0.0, 0.0), 100);
    // drivetrain.rightLeader.configSupplyCurrentLimit(SupplyCurrentLimitConfiguration(true, 20.0, 0.0, 0.0), 100);
    // drivetrain.rightFollower.configSupplyCurrentLimit(SupplyCurrentLimitConfiguration(true, 20.0, 0.0, 0.0), 100);
  }
}
