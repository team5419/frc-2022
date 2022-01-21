package frc.robot.auto
import frc.robot.commands.RamseteAction
import edu.wpi.first.math.geometry.Pose2d
import edu.wpi.first.math.geometry.Rotation2d

import frc.robot.subsystems.Drivetrain
import frc.robot.subsystems.Shooter

import frc.robot.commands.Shoot

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

class TestDrive(m_drivetrain: Drivetrain, m_shooter: Shooter) : SequentialCommandGroup() {
    val drivetrain: Drivetrain = m_drivetrain
    val shooter: Shooter = m_shooter
    init {
        addCommands(
            RamseteAction(drivetrain, listOf(
                Pose2d(0.0, 0.0, Rotation2d(0.0)), 
                Pose2d(0.0, 0.5, Rotation2d(0.0))
            ), false)
            // Shoot(shooter, 1000.0, 5.0),
            // RamseteAction(drivetrain, listOf(
            //     Pose2d(0.75, 0.0, Rotation2d(0.0)), 
            //     Pose2d(0.0, -1.0, Rotation2d.fromDegrees(45.0))
            // ))
        )
    }
}