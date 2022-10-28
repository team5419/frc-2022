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

import frc.robot.classes.SubsystemHolder;

class AlignSpin(_subsystems: SubsystemHolder, _driver: XboxController, _main: Double = -1.0, _kicker: Double = -1.0, _throttling: Boolean = true) : SequentialCommandGroup() {
  private val subsystems: SubsystemHolder = _subsystems
  private val driver: XboxController = _driver;
  private val main: Double = _main;
  private val kicker: Double = _kicker;
  private val throttling : Boolean = _throttling;

  init {
    addCommands(
      ParallelRaceGroup(
        SpinUp(subsystems),
        NewAutoAlign(subsystems, driver, 0.0)
      )
    )
  }

  override fun end(interrupted: Boolean) {
    subsystems.drivetrain.stop()
    subsystems.lights.setColor(RGB(0, 0, 0));
  }
}