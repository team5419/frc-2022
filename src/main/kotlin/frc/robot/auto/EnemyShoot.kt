package frc.robot.auto

import edu.wpi.first.math.geometry.Pose2d
import edu.wpi.first.math.geometry.Rotation2d
import edu.wpi.first.wpilibj.XboxController;

import frc.robot.subsystems.*
import frc.robot.commands.*

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;

class EnemyShoot(m_drivetrain: Drivetrain, m_shooter: Shooter, m_vision: Vision, m_indexer: Indexer, m_feeder: Feeder, m_intake: Intake, m_lights: Lights, m_driver: XboxController) : SequentialCommandGroup() {
    val drivetrain: Drivetrain = m_drivetrain
    val driver: XboxController = m_driver
    val shooter: Shooter = m_shooter
    val vision: Vision = m_vision
    val indexer: Indexer = m_indexer
    val feeder: Feeder = m_feeder
    val intake: Intake = m_intake
    val lights: Lights = m_lights

    init {
        addCommands(
            // run intake and move to first shoot position
            ParallelRaceGroup(
                RunIntake(intake, feeder, 3.0),
                SequentialCommandGroup(
                    RamseteAction(drivetrain, listOf(
                        Pose2d(0.0, 0.0, Rotation2d(0.0)), 
                        Pose2d(-0.75, 0.0, Rotation2d(0.0))
                    ), false),
                    AutoAlign(vision, drivetrain, shooter, lights, 2.0, false),
                    ShootAndFeed(shooter, feeder, indexer, lights, driver, -1.0, -1.0, 4.0),
                    RamseteAction(drivetrain, listOf(
                        Pose2d(-0.75, 0.0, Rotation2d(0.0)), 
                        Pose2d(-0.75, -1.5, Rotation2d.fromDegrees(90.0))
                    ), false),
                    RamseteAction(drivetrain, listOf(
                        Pose2d(-0.75, 0.0, Rotation2d.fromDegrees(90.0)), 
                        Pose2d(-0.75, 0.0, Rotation2d.fromDegrees(270.0))
                    ), false),
                    ShootAndFeed(shooter, feeder, indexer, lights, driver, 10000.0, 10000.0, 4.0)
                )
            )
        )
    }
}