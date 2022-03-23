package frc.robot.auto

import edu.wpi.first.math.geometry.Pose2d
import edu.wpi.first.math.geometry.Rotation2d

import frc.robot.subsystems.Drivetrain
import frc.robot.subsystems.Shooter
import frc.robot.subsystems.Vision
import frc.robot.subsystems.Indexer
import frc.robot.subsystems.Feeder
import frc.robot.subsystems.Intake
import frc.robot.subsystems.Lights

import frc.robot.commands.RamseteAction
import frc.robot.commands.AutoAlign
import frc.robot.commands.RunIntake
import frc.robot.commands.ShootAndFeed
import frc.robot.commands.SpinUp
import frc.robot.commands.Wait
import frc.robot.commands.CycleIndexer

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;

class FiveBallAuto2(m_drivetrain: Drivetrain, m_shooter: Shooter, m_vision: Vision, m_indexer: Indexer, m_feeder: Feeder, m_intake: Intake, m_lights: Lights) : SequentialCommandGroup() {
    val drivetrain: Drivetrain = m_drivetrain
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
                RunIntake(intake, feeder),
                SequentialCommandGroup(
                    ParallelRaceGroup(
                        SpinUp(shooter, 15500.0, 15500.0),
                        RamseteAction(drivetrain, listOf(
                            Pose2d(0.0, 0.0, Rotation2d(0.0)), 
                            Pose2d(-0.9, 0.0, Rotation2d(0.0))
                        ), false)
                    ),
                    // autoalign and index/shoot first 2 balls
                    AutoAlign(vision, drivetrain, shooter, lights, 0.5, false),
                    ParallelRaceGroup(
                        CycleIndexer(indexer, shooter, 10),
                        ShootAndFeed(shooter, feeder, indexer, lights, 15500.0, 15500.0, 1.75)
                    ),
                    // run intake and move to pick-up position
                    RamseteAction(drivetrain, listOf(
                        Pose2d(-0.9, 0.0, Rotation2d(0.0)), 
                        Pose2d(-4.0, -0.1, Rotation2d.fromDegrees(0.0))
                    ), false),
                    Wait(1.0),
                    // intake 2 balls from the human player station
                    // moves to new shot location
                    ParallelRaceGroup(
                        RamseteAction(drivetrain, listOf(
                            Pose2d(-4.0, -0.1, Rotation2d.fromDegrees(0.0)), 
                            Pose2d(0.0, 1.0, Rotation2d.fromDegrees(-35.0))
                        ), true),
                        SpinUp(shooter, 15000.0, 15000.0)
                    ),
                    // shoots 2 balls
                    AutoAlign(vision, drivetrain, shooter, lights, 0.5, false),
                    ParallelRaceGroup(
                        CycleIndexer(indexer, shooter, 10),
                        ShootAndFeed(shooter, feeder, indexer, lights, 15750.0, 15750.0, 1.3)
                    ),
                    // move backward and pick up last ball
                    ParallelRaceGroup(
                        RamseteAction(drivetrain, listOf(
                            Pose2d(0.0, 1.0, Rotation2d.fromDegrees(-35.0)), 
                            Pose2d(-0.5, 1.5, Rotation2d.fromDegrees(-35.0))
                        ), true),
                        SpinUp(shooter, 15500.0, 15500.0)
                    ),
                    // shoots 1 ball
                    AutoAlign(vision, drivetrain, shooter, lights, 0.5, false),
                    ParallelRaceGroup(
                        CycleIndexer(indexer, shooter, 10),
                        ShootAndFeed(shooter, feeder, indexer, lights, 15500.0, 15500.0, 1.3)
                    )
                )
            )
            
        )
    }
}