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



class AlignSpinShoot(_vision: Vision, _drivetrain: Drivetrain, _shooter: Shooter, _indexer: Indexer, _feeder: Feeder, _lights: Lights) : SequentialCommandGroup() {
  private val vision: Vision = _vision;
  private val drivetrain: Drivetrain = _drivetrain;
  private val shooter: Shooter = _shooter;
  private val indexer: Indexer = _indexer;
  private val lights: Lights = _lights;
  private val feeder: Feeder = _feeder;

  init {
    addCommands(
      ParallelRaceGroup(
        SpinUp(shooter),
        AutoAlign(vision, drivetrain, shooter, lights, 1.0)
      ),
      ParallelRaceGroup(
        ShootAndFeed(shooter, feeder, indexer, lights),
        CycleIndexer(indexer, shooter, 5)
      )
    )
  }

  override fun end(interrupted: Boolean) {
    shooter.stop();
    drivetrain.drive(0.0, 0.0, false);
  }
}
