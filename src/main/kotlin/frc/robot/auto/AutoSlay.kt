package frc.robot.auto

import edu.wpi.first.math.geometry.Pose2d
import edu.wpi.first.math.geometry.Rotation2d

import edu.wpi.first.wpilibj.XboxController;

import frc.robot.subsystems.*
import frc.robot.commands.*

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;

class AutoSlay(_drivetrain: Drivetrain, m_driver: XboxController, _catapult: Catapult, _intake: IntakeSub) : SequentialCommandGroup() {
    private val drivetrain: Drivetrain = _drivetrain
    private val driver: XboxController = m_driver
    private val catapult: Catapult =  _catapult
    private val intake: IntakeSub = _intake
    
    init {
        val backwards1: Double = -0.7; // -0.7;
        val backwards2: Double = 0.9;
        val angle: Double = 60.0
        val x1: Double = 0.3 - backwards1 * Math.cos(Math.toRadians(angle));
        val y1: Double = 1.3 + backwards1 * Math.sin(Math.toRadians(angle));
        val x2: Double = x1 - backwards2 * Math.cos(Math.toRadians(angle));
        val y2: Double = y1 + backwards2 * Math.sin(Math.toRadians(angle));
        println("x2: " + x2)
        println("y2: " + y2)

        addCommands(
            SequentialCommandGroup (
                Shoot(catapult),
                Shoot(catapult),
                ValueDrive(drivetrain, 0.0, -0.36, 0.5),
                ParallelCommandGroup (
                    ValueDrive(drivetrain, 0.2, 0.0, 2.0),
                    Deploy(intake)
                ),
                Intake(intake)

            )
                // RamseteAction(drivetrain, listOf(
                //     Pose2d(0.0, 0.0, Rotation2d(0.0)),
                //     Pose2d(0.0, 0.5, Rotation2d(0.005))
                // ), false)
                // ParallelCommandGroup (
                //     Deploy(intake),
                //     Intake(intake)
                // ),
                // ParallelCommandGroup (
                //     Deploy(intake),
                //     Intake(intake)
                // )
        )
                // RamseteAction(drivetrain, listOf(
                //     Pose2d(-0.7, 0.0, Rotation2d(0.0)), 
                //     Pose2d(-4.0, -0.6, Rotation2d.fromDegrees(45.0))
                // ), false)
                // RamseteAction(subsystems, listOf(
                //     Pose2d(-4.0, -0.6, Rotation2d.fromDegrees(0.0)),
                //     Pose2d(x1, y1, Rotation2d.fromDegrees(-angle))
                // ), true),
                // RamseteAction(subsystems, listOf(
                //     Pose2d(x1, y1, Rotation2d.fromDegrees(-angle)),
                //     Pose2d(x2, y2, Rotation2d.fromDegsrees(-angle))
                // )),
    }
}