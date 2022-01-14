package frc.robot.auto
import frc.robot.commands.RamseteAction
import edu.wpi.first.math.geometry.Pose2d
import edu.wpi.first.math.geometry.Rotation2d

import frc.robot.subsystems.Drivetrain

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

class TestDrive(m_drivetrain: Drivetrain) : SequentialCommandGroup() {
    val drivetrain: Drivetrain = m_drivetrain
    init {
        addCommands(
            RamseteAction(drivetrain, listOf(
                Pose2d(0.0, 0.0, Rotation2d(0.0)), 
                Pose2d(1.0, 0.0, Rotation2d(0.0))
            ))
        )
    }
}