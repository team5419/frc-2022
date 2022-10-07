package frc.robot.commands;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drivetrain;
import edu.wpi.first.math.geometry.Pose2d
class ResetOdometry(_drivetrain: Drivetrain, _pose: Pose2d) : CommandBase() {
    private val drivetrain: Drivetrain = _drivetrain;
    private val pose: Pose2d = _pose;
    init {
    }
    public override fun initialize() {
        drivetrain.resetOdometry(pose)
    }
    public override fun execute() {
    }
    public override fun isFinished(): Boolean {
        return true;
    }
    public override fun end(interrupted: Boolean) {
    }
}