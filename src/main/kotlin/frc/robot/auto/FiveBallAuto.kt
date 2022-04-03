package frc.robot.auto

import edu.wpi.first.math.geometry.Pose2d
import edu.wpi.first.math.geometry.Rotation2d

import edu.wpi.first.wpilibj.XboxController;

import frc.robot.subsystems.*
import frc.robot.commands.*

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;

class FiveBallAuto(m_drivetrain: Drivetrain, m_shooter: Shooter, m_vision: Vision, m_indexer: Indexer, m_feeder: Feeder, m_intake: Intake, m_deploy: DeploySubsystem, m_lights: Lights, m_driver: XboxController) : SequentialCommandGroup() {
    val drivetrain: Drivetrain = m_drivetrain
    val shooter: Shooter = m_shooter
    val vision: Vision = m_vision
    val indexer: Indexer = m_indexer
    val feeder: Feeder = m_feeder
    val intake: Intake = m_intake
    val deploy: DeploySubsystem = m_deploy
    val lights: Lights = m_lights
    val driver: XboxController = m_driver
    
    init {
        val backwards1: Double = 1.0;
        val backwards2: Double = 1.0;
        val x1: Double = 1.2 - backwards1 * Math.cos(Math.toRadians(65.0));
        val y1: Double = 0.5 + backwards1 * Math.sin(Math.toRadians(65.0));
        val x2: Double = x1 - backwards2 * Math.cos(Math.toRadians(65.0));
        val y2: Double = y1 + backwards2 * Math.sin(Math.toRadians(65.0));
        addCommands(
            // run intake and move to first shoot position
            ParallelRaceGroup(
                ToggleCurrent(drivetrain),
                RunIntake(intake, deploy, feeder, 0.0),
                SequentialCommandGroup(
                    StartFeeding(feeder),
                    SpinUp(shooter, 15500.0, 15500.0),
                    ParallelRaceGroup(
                        RamseteAction(drivetrain, listOf(
                            Pose2d(0.0, 0.0, Rotation2d(0.0)), 
                            Pose2d(-1.0, 0.0, Rotation2d(0.0))
                        ), false)
                    ),
                    // autoalign and index/shoot first 2 balls
                    AutoAlign(vision, drivetrain, shooter, lights, 0.5, false),
                    Shoot(vision, drivetrain, shooter, indexer, feeder, lights, driver, 15500.0, 15500.0),
                    // run intake and move to second shoot position
                    RamseteAction(drivetrain, listOf(
                        Pose2d(-1.0, 0.0, Rotation2d(0.0)), 
                        Pose2d(-4.0, -0.7, Rotation2d.fromDegrees(0.0))
                    ), false),
                    Wait(0.2),
                    // intake 2 balls from the human player station
                    // moves to new shot location
                    ParallelRaceGroup(
                        RamseteAction(drivetrain, listOf(
                            Pose2d(-4.0, -0.7, Rotation2d.fromDegrees(0.0)), 
                            Pose2d(1.2, 0.5, Rotation2d.fromDegrees(-65.0)) // negative low, positive high
                        ), true),
                        SpinUp(shooter, 15000.0, 14000.0),
                        DefaultIndex(indexer, lights)
                    ),
                    ParallelRaceGroup(
                        RamseteAction(drivetrain, listOf(
                            Pose2d(1.2, 0.5, Rotation2d.fromDegrees(-65.0)),
                            Pose2d(x1, y1, Rotation2d.fromDegrees(-65.0))
                        )),
                        SpinUp(shooter, 15000.0, 14000.0),
                        DefaultIndex(indexer, lights)
                    ),
                    // shoots 2 balls
                    AutoAlign(vision, drivetrain, shooter, lights, 0.5, false),
                    Shoot(vision, drivetrain, shooter, indexer, feeder, lights, driver, 15000.0, 14000.0),
                    // move backward and pick up last ball
                    ParallelRaceGroup(
                        RamseteAction(drivetrain, listOf(
                            Pose2d(x1, y1, Rotation2d.fromDegrees(-65.0)),
                            Pose2d(x2, y2, Rotation2d.fromDegrees(-65.0))
                        )),
                        SpinUp(shooter, 15500.0, 15500.0)
                    ),
                    // shoots 1 ball
                    AutoAlign(vision, drivetrain, shooter, lights, 0.5, false),
                    Shoot(vision, drivetrain, shooter, indexer, feeder, lights, driver, 15500.0, 15500.0)
                )
            ) 
        )
    }
}