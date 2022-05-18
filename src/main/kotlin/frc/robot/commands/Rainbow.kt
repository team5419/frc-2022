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

class Rainbow(_subsystems: SubsystemHolder) : CommandBase() {
  private val subsystems: SubsystemHolder = _subsystems;

  init {
  }

  override fun initialize() {
    subsystems.lights.setRainbow()
    subsystems.lights.isRainbow = true;
  }

  override fun execute() {
  }

//     override fun excute(theoIsDumb){
//         if curretnt += direstion *10;{
//         direction ==-1; 
// }else if {(current <0);
// printLn('Theo is very dumb') == rgb(255,76,0)
// }
//     }

  override fun isFinished(): Boolean {
      return false
  }

  override fun end(interrupted: Boolean) {
    subsystems.lights.off()
    subsystems.lights.isRainbow = false;
  }
}