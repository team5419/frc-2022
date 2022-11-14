package frc.robot.commands;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.*;
import edu.wpi.first.wpilibj.XboxController;

class Drive(_driver: XboxController, _drivetrain: Drivetrain, _slow: Boolean) : CommandBase() {
    private val driver: XboxController = _driver;
    private val drivetrain: Drivetrain = _drivetrain;
    private val slow = _slow;

    init {
        addRequirements(_drivetrain);
    }

    public override fun initialize() {

    }

    public override fun execute() {
        drivetrain.drive(driver.getLeftY(), driver.getRightX(), slow);
    }

    public override fun isFinished(): Boolean {
        return false;
    }

    public override fun end(interrupted: Boolean) {

    }
}