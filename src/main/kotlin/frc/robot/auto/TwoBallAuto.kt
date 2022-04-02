package frc.robot.auto

import edu.wpi.first.math.geometry.Pose2d
import edu.wpi.first.math.geometry.Rotation2d

import edu.wpi.first.wpilibj.XboxController;

import frc.robot.subsystems.*
import frc.robot.commands.*

import frc.robot.commands.Shoot
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;

class TwoBallAuto(m_drivetrain: Drivetrain, m_shooter: Shooter, m_vision: Vision, m_indexer: Indexer, m_feeder: Feeder, m_intake: Intake, m_deploy: DeploySubsystem, m_lights: Lights, m_driver: XboxController) : SequentialCommandGroup() {
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

            addCommands(
                // run intake and move to first shoot position
                ParallelRaceGroup(
                    RunIntake(intake, deploy, feeder, 0.0),
                    //Feed(feeder),
                    SequentialCommandGroup(
                        StartFeeding(feeder),
                        ParallelRaceGroup(
                            RamseteAction(drivetrain, listOf(
                                Pose2d(0.0, 0.0, Rotation2d(0.0)), 
                                Pose2d(-0.3, 0.0, Rotation2d(0.0))
                            ), false)
                        ),
                        ParallelRaceGroup(
                            SpinUp(shooter, 15500.0, 15500.0),
                            RamseteAction(drivetrain, listOf(
                                Pose2d(-0.3, 0.0, Rotation2d(0.0)),
                                Pose2d(-1.0, 0.0, Rotation2d(0.0))
                            ), false)
                        ),
                        // autoalign and index/shoot first 2 balls
                        AutoAlign(vision, drivetrain, shooter, lights, 1.0, false),
                        Shoot(vision, drivetrain, shooter, indexer, feeder, lights, driver, 15500.0, 15500.0, 5.0))
            )
        )
    }
}