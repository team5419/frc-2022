package frc.robot.commands; 

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.Timer

import frc.robot.subsystems.Drivetrain


class AutoDrive(_drivetrain: Drivetrain, _time: Double) : CommandBase() {
    private val drivetrain : Drivetrain = _drivetrain;
    private val time : Double = _time;
    private val timer : Timer = Timer();

    init {
        addRequirements(_drivetrain);
      }
    
    override fun initialize() {
        timer.reset()
        timer.start()
    }
    
    override fun execute() {
        drivetrain.drive(-0.5, -0.5, false);
      }
  
    override fun end(interrupted: Boolean) {}
    
    override fun isFinished(): Boolean {
        return (time != 0.0 && timer.get() >= time)
      }
}
