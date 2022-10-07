package frc.robot.commands; 

import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Indexer;
import frc.robot.subsystems.Feeder;
import frc.robot.FeederConstants;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.subsystems.Lights;
import frc.robot.classes.RGB;
import frc.robot.classes.SubsystemHolder
class SpinUp(_subsystems: SubsystemHolder, _main: Double = 13500.0, _kicker: Double = 13500.0)  : CommandBase() {
  private val subsystems: SubsystemHolder = _subsystems
  private val main: Double = _main;
  private val kicker: Double = _kicker;

  init {
    addRequirements(_subsystems.shooter)
  }

  override fun initialize() {
    subsystems.shooter.shoot(main, kicker)
  }

  override fun execute() {
  }

  override fun isFinished(): Boolean {
    return false;
  }

  override fun end(interrupted: Boolean) {
  }
}
