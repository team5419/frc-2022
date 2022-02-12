package frc.robot.commands; 

import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Indexer;
import frc.robot.subsystems.Feeder;
import frc.robot.FeederConstants;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.Timer
import frc.robot.commands.Feed
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

class Shoot(_shooter: Shooter, _indexer: Indexer, _feeder: Feeder, _main: Double = -1.0, _kicker: Double = -1.0) : SequentialCommandGroup() {
  private val shooter: Shooter = _shooter;
  private val indexer: Indexer = _indexer;
  private val feeder: Feeder = _feeder;
  private val main: Double = _main;
  private val kicker: Double = _kicker;

  init {
    addRequirements(_shooter, _indexer, _feeder)
  }

  override fun initialize() {
    addCommands(
      Wait(shooter, 2.0), 
      Index(indexer)
    )
  }

  override fun execute() {
    shooter.shoot(main, kicker);
    feeder.feed(FeederConstants.activePercent);
  }
}
