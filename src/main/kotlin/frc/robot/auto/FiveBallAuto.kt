package frc.robot.auto

import edu.wpi.first.math.geometry.Pose2d
import edu.wpi.first.math.geometry.Rotation2d

import frc.robot.subsystems.Drivetrain
import frc.robot.subsystems.Shooter
import frc.robot.subsystems.Vision
import frc.robot.subsystems.Indexer
import frc.robot.subsystems.Feeder
import frc.robot.subsystems.Intake

import frc.robot.commands.RamseteAction
import frc.robot.commands.AutoAlign
import frc.robot.commands.RunIntake
import frc.robot.commands.ShootAndFeed

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;

class FiveBallAuto(m_drivetrain: Drivetrain, m_shooter: Shooter, m_vision: Vision, m_indexer: Indexer, m_feeder: Feeder, m_intake: Intake) : SequentialCommandGroup() {
    val drivetrain: Drivetrain = m_drivetrain
    val shooter: Shooter = m_shooter
    val vision: Vision = m_vision
    val indexer: Indexer = m_indexer
    val feeder: Feeder = m_feeder
    val intake: Intake = m_intake
    
    init {
        addCommands(
            // run intake and move to first shoot position
            ParallelRaceGroup(
                //RunIntake(intake, feeder, 2.0),
                RamseteAction(drivetrain, listOf(
                    Pose2d(0.0, 0.0, Rotation2d(0.0)), 
                    Pose2d(-1.0, 0.0, Rotation2d(0.0))
                ), false)
            ),
            // autoalign and index/shoot first 2 balls
            AutoAlign(vision, drivetrain, shooter, 2.0, false),
            ShootAndFeed(shooter, feeder, indexer, -1.0, -1.0, 1.5),
            // run intake and move to second shoot position
            RamseteAction(drivetrain, listOf(
                    Pose2d(-1.0, 0.0, Rotation2d(0.0)), 
                    Pose2d(0.75, -2.0, Rotation2d.fromDegrees(45.0))
            ), false),
            ParallelRaceGroup(
                //RunIntake(intake, feeder, 8.0),
                RamseteAction(drivetrain, listOf(
                    Pose2d(0.75, -2.0, Rotation2d.fromDegrees(45.0)), 
                    Pose2d(0.5, -2.25, Rotation2d.fromDegrees(45.0))
                ), false)
            ),
            ParallelRaceGroup(
                RunIntake(intake, feeder, 8.0),
                // autoalign and index/shoot second ball
                AutoAlign(vision, drivetrain, shooter, 0.0, false)
            ),
            ParallelCommandGroup(
                RunIntake(intake, feeder, 8.0),
                ShootAndFeed(shooter, feeder, indexer, 16000.0, 16000.0, 8.0)
            ),
            RamseteAction(drivetrain, listOf(
                    Pose2d(0.5, -2.25, Rotation2d.fromDegrees(45.0)), 
                    Pose2d(0.0, -2.75, Rotation2d.fromDegrees(45.0))
            ), false)
        )
    }
}