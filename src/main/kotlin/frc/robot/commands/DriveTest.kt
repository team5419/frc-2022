package frc.robot.commands; 

import frc.robot.subsystems.Drivetrain;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.XboxController;
import frc.robot.Util;
import frc.robot.DriveConstants;
import frc.robot.classes.SubsystemHolder

class DriveTest(_subsystems: SubsystemHolder, _driver: XboxController, _isSlow: Boolean = false) : CommandBase() {
  private val subsystems: SubsystemHolder = _subsystems
  private val driver: XboxController = _driver;
  private val isSlow: Boolean = _isSlow;

  init {
    addRequirements(subsystems.drivetrain);
  }

  override fun initialize() {
    // for(i in 0..3) {
    //     subsystems.drivetrain.drivers[i].test();
    // }
  }

  override fun execute() {
    println("execute")
     for(i in 0..3) {
        subsystems.drivetrain.drivers[i].test();
    }
  }

  override fun end(interrupted: Boolean) {
    subsystems.drivetrain.stop();
  }

  override fun isFinished(): Boolean {
    return false;
  }
}