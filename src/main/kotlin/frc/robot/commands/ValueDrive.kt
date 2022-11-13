package frc.robot.commands;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.*;

import edu.wpi.first.wpilibj.Timer

class ValueDrive(_drivetrain: Drivetrain, _xVal: Double, _yVal: Double, _delay: Double) : CommandBase() {
    private val drivetrain: Drivetrain = _drivetrain;
    private val xVal: Double = _xVal;
    private val yVal: Double = _yVal;
    private val delay: Double = _delay;
    private val timer: Timer = Timer();

    init {
        addRequirements(_drivetrain);
    }

    public override fun initialize() {
        timer.reset();
        timer.start();
    }

    public override fun execute() {
        drivetrain.drive(xVal, yVal, false);
    }

    public override fun isFinished(): Boolean {
        return (timer.get() >= delay);
    }

    public override fun end(interrupted: Boolean) {
        timer.stop();
    }
}