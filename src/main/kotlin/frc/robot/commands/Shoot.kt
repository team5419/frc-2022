package frc.robot.commands; 

import frc.robot.subsystems.Vision;
import frc.robot.subsystems.Drivetrain
import frc.robot.subsystems.Indexer
import frc.robot.subsystems.Feeder
import frc.robot.subsystems.Shooter
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.XboxController;
import frc.robot.classes.DriveSignal;
import frc.robot.LookupEntry;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab
import edu.wpi.first.wpilibj.Timer
import frc.robot.classes.RGB;
import frc.robot.subsystems.Lights;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import frc.robot.FeederConstants
import frc.robot.classes.SubsystemHolder
import frc.robot.commands.Turn


import kotlin.math.sin
import kotlin.random.Random

class Shoot(_subsystems: SubsystemHolder, _driver: XboxController, _main: Double = -1.0, _kicker: Double = -1.0, _time: Double = 0.0) : SequentialCommandGroup() {
  private val subsystems: SubsystemHolder = _subsystems;
  private val driver: XboxController = _driver;
  private val main: Double = _main;
  private val kicker: Double = _kicker;
  private val time : Double = _time;

  init {
    addCommands(
      Turn(subsystems),
      ParallelRaceGroup(
        ShootAndFeed(subsystems, driver, Random.nextDouble(14000.0, 20000.0), Random.nextDouble(14000.0, 20000.0), 5.0),
        CycleIndexer(subsystems, 50)
      ),
      TurnBack(subsystems)
    )
  }

  override fun end(interrupted: Boolean) {
    subsystems.shooter.stop();
    subsystems.feeder.currentVel = FeederConstants.idlePercent
    subsystems.lights.setColor(RGB(0, 0, 0))
  }
}