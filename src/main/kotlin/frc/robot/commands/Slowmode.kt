package frc.robot.commands;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.*;

class Slowmode(_drivetrain: Drivetrain) : CommandBase() {
    val drivetrain: Drivetrain = _drivetrain;

    init {
        addRequirements(_drivetrain);
    }

    public override fun initialize() {
        drivetrain.mod = 0.5;
    }

    public override fun execute() {
        
    }

    public override fun end(interrupted: Boolean) {
        drivetrain.mod = 1.0;
    }

    public override fun isFinished(): Boolean {
        return false;
    }
}