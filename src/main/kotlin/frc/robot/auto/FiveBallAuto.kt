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
        val backwards1: Double = -0.7;
        val backwards2: Double = 0.9;
        val angle: Double = 60.0
        val x1: Double = 0.3 - backwards1 * Math.cos(Math.toRadians(angle));
        val y1: Double = 1.3 + backwards1 * Math.sin(Math.toRadians(angle));
        val x2: Double = x1 - backwards2 * Math.cos(Math.toRadians(angle));
        val y2: Double = y1 + backwards2 * Math.sin(Math.toRadians(angle));
        println("x2: " + x2)
        println("y2: " + y2)

        addCommands(
            // run intake and move to first shoot position
        
            ParallelRaceGroup(
                RunIntake(intake, deploy, feeder, 0.0),
                ToggleCurrent(drivetrain),
                Feed(feeder, 0.6),
                //DefaultDeploy(deploy),
                SequentialCommandGroup(
                    StartFeeding(feeder),
                    ParallelRaceGroup(
                        SpinUp(shooter, 15250.0, 15250.0),
                        RamseteAction(drivetrain, listOf(
                            Pose2d(0.0, 0.0, Rotation2d(0.0)),
                            Pose2d(-0.9, 0.0, Rotation2d(0.0))
                        ), false)
                    ),
                    // autoalign and index/shoot first 2 balls
                    AutoAlign(vision, drivetrain, shooter, lights, 0.5, false),
                    Shoot(vision, drivetrain, shooter, indexer, feeder, lights, driver, 15250.0, 15250.0, 1.0),
                    // run intake and move to second shoot position
                    RamseteAction(drivetrain, listOf(
                        Pose2d(-0.9, 0.0, Rotation2d(0.0)), 
                        Pose2d(-4.0, -0.7, Rotation2d.fromDegrees(0.0))
                    ), false),
                    Wait(0.0),
                    // intake 2 balls from the human player station
                    ParallelRaceGroup(
                        RamseteAction(drivetrain, listOf(
                            Pose2d(-4.0, -0.7, Rotation2d.fromDegrees(0.0)),
                            Pose2d(x1, y1, Rotation2d.fromDegrees(-angle))
                        ), true),
                        SpinUp(shooter, 14500.0, 13500.0),
                        DefaultIndex(indexer, lights)
                    ),
                    // shoots 2 balls
                    AutoAlign(vision, drivetrain, shooter, lights, 0.5, false),
                    Shoot(vision, drivetrain, shooter, indexer, feeder, lights, driver, 14500.0, 13500.0, 1.0),
                    // move backward and pick up last ball
                    ParallelRaceGroup(
                        RamseteAction(drivetrain, listOf(
                            Pose2d(x1, y1, Rotation2d.fromDegrees(-angle)),
                            Pose2d(x2, y2, Rotation2d.fromDegrees(-angle))
                        )),
                        SpinUp(shooter, 15500.0, 15000.0)
                    ),
                    // shoots 1 ball
                    AutoAlign(vision, drivetrain, shooter, lights, 0.5, false),
                    Shoot(vision, drivetrain, shooter, indexer, feeder, lights, driver, 15500.0, 15000.0, 1.0)
                )
            ) 
        )
    }
}