package frc.robot.commands; 

import frc.robot.subsystems.Vision;
import frc.robot.subsystems.Drivetrain
import frc.robot.subsystems.Indexer
import frc.robot.subsystems.Feeder
import frc.robot.subsystems.Shooter
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.XboxController;
import frc.robot.LookupEntry;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab
import edu.wpi.first.wpilibj.Timer
import frc.robot.classes.RGB;
import frc.robot.subsystems.Lights;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import frc.robot.classes.SubsystemHolder


class AutoAlignAndShoot(_subsystems: SubsystemHolder, _main: Double = -1.0, _kicker: Double = -1.0, _driver: XboxController, _throttling: Boolean = false) : SequentialCommandGroup() {
  private val subsystems: SubsystemHolder = _subsystems
  private val main: Double = _main;
  private val kicker: Double = _kicker;
  private val throttling : Boolean = _throttling;
  private val driver : XboxController = _driver;


  init {
    addCommands(
        SequentialCommandGroup(
            ParallelRaceGroup(
                SpinUp(subsystems),
                AutoAlign(subsystems, 1.5, throttling)
            ),
            Shoot(subsystems, driver, main, kicker)
        )
    )
  }

  override fun end(interrupted: Boolean) {
    subsystems.drivetrain.stop();
  }
}