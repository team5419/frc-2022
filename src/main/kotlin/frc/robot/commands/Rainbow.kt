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
  private var current: Int = 0;

  init {
  }

  override fun initialize() {
      current = 0;
  }

  fun hsvToRgb(h: Int, s: Int, v: Int): RGB {
    val c: Int = Math.round((v.toDouble() / 100) * (s / 100)).toInt();
    val x: Int = Math.round(c * (1 - Math.abs(((h.toDouble() / 60) % 2) - 1))).toInt();
    val m: Int = Math.round((v.toDouble() / 100) - c).toInt();
    
    var r: Int = 0;
    var g: Int = 0;
    var b: Int = 0;
    val hmod: Int = Math.floor(h.toDouble() / 60).toInt();
    when(hmod) {
      0 -> {
        r = c;
        g = x;
      }
      1 -> {
        r = x;
        g = c;
      }
      2 -> {
        g = c;
        b = x;
      }
      3 -> {
        g = x;
        b = c;
      }
      4 -> {
        r = x;
        b = c;
      }
      5 -> {
        r = c;
        b = x;
      }
    }
    r = (r + m) * 255;
    g = (g + m) * 255;
    b = (b + m) * 255;
    return RGB(r, g, b);
  }

  override fun execute() {
      current++;
      if(current > 359) {
        current = 0;
      }
      subsystems.lights.setColor(hsvToRgb(current, 100, 100));
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
    subsystems.lights.setColor(RGB(0, 0, 0))
  }
}