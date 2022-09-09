package frc.robot.commands;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drivetrain;
class Brake(_drivetrain: Drivetrain) : CommandBase() {
    private val drivetrain: Drivetrain = _drivetrain;
    init {
        addRequirements(drivetrain)
    }
    public override fun initialize() {
        
    }
    public override fun execute() {
        drivetrain.brake();
    }
    public override fun isFinished(): Boolean {
        return false;
    }
    public override fun end(interrupted: Boolean) {
        drivetrain.stop();
    }
}