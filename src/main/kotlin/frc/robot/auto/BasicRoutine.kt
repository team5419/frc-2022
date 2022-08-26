package frc.robot.auto;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup
import frc.robot.subsystems.Drivetrain;
import frc.robot.Util;
import edu.wpi.first.math.geometry.Pose2d;
class BasicRoutine(_drivetrain: Drivetrain): SequentialCommandGroup() {
    private val drivetrain: Drivetrain = _drivetrain;
    init {
        addRequirements(_drivetrain);
        addCommands(
            Util.generateRamsete(drivetrain, listOf(

            ))
        )
    }
}