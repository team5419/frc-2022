package frc.robot.commands; 

import frc.robot.subsystems.Climber;
import frc.robot.subsystems.Indexer;
import frc.robot.subsystems.Feeder;
import frc.robot.ClimberConstants;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.Timer
import frc.robot.commands.WaitForShooter
import frc.robot.commands.Index
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

class ClimbButton(_climber: Climber) : CommandBase() {
  private val climber: Climber = _climber

  init {
    addRequirements(
        _climber
    );
  }

  override fun initialize() {

  }

  override fun execute() {
    climber.setPairPIDAdjust(0, 0.0, 0.0)
    climber.setPairPIDAdjust(1, 0.0, 0.0)
  }

  override fun isFinished(): Boolean {
      return false;
  }

  override fun end(interrupted: Boolean) {
     climber.stop()
  }
}