package frc.robot.auto

import edu.wpi.first.math.geometry.Pose2d
import edu.wpi.first.math.geometry.Rotation2d

import frc.robot.subsystems.Drivetrain
import frc.robot.subsystems.Shooter
import frc.robot.subsystems.Vision
import frc.robot.subsystems.Indexer
import frc.robot.subsystems.Feeder

import frc.robot.commands.RamseteAction
import frc.robot.commands.AutoAlignTurn
import frc.robot.commands.Shoot

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

class TestDrive(m_drivetrain: Drivetrain, m_shooter: Shooter, m_vision: Vision, m_indexer: Indexer, m_feeder: Feeder) : SequentialCommandGroup() {
    val drivetrain: Drivetrain = m_drivetrain
    val shooter: Shooter = m_shooter
    val vision: Vision = m_vision
    val indexer: Indexer = m_indexer
    val feeder: Feeder = m_feeder
    init {
        addCommands(
            RamseteAction(drivetrain, listOf( // negative x is forward, positive x is backward, positive y is left, negative y is right
                Pose2d(0.0, 0.0, Rotation2d(0.0)), 
                Pose2d(0.8, 0.0, Rotation2d(0.0))
            ), true),
            AutoAlignTurn(vision, drivetrain, shooter),
            Shoot(shooter, indexer, feeder, 1000.0, 1000.0),
            RamseteAction(drivetrain, listOf(
                Pose2d(0.8, 0.0, Rotation2d(0.0)), 
                Pose2d(-0.2, 1.3, Rotation2d.fromDegrees(45.0))
            ), true), 
            Shoot(shooter, indexer, feeder, 1000.0, 1000.0)
        )
    }
}