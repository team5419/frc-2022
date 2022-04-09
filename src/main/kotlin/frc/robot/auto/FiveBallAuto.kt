package frc.robot.auto

import edu.wpi.first.math.geometry.Pose2d
import edu.wpi.first.math.geometry.Rotation2d

import edu.wpi.first.wpilibj.XboxController;

import frc.robot.subsystems.*
import frc.robot.commands.*

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import frc.robot.classes.SubsystemHolder
class FiveBallAuto(_subsystems: SubsystemHolder, m_driver: XboxController) : SequentialCommandGroup() {
    val subsystems: SubsystemHolder = _subsystems
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
                RunIntake(subsystems, 0.0),
                ToggleCurrent(subsystems, 60.0),
                Feed(subsystems, 0.6),
                //DefaultDeploy(deploy),
                SequentialCommandGroup(
                    StartFeeding(subsystems),
                    ParallelRaceGroup(
                        SpinUp(subsystems, 15250.0, 15250.0),
                        RamseteAction(subsystems, listOf(
                            Pose2d(0.0, 0.0, Rotation2d(0.0)),
                            Pose2d(-0.9, 0.0, Rotation2d(0.0))
                        ), false)
                    ),
                    // autoalign and index/shoot first 2 balls
                    AutoAlign(subsystems, 0.5, false),
                    Shoot(subsystems, driver, 15250.0, 15250.0, 1.25),
                    // run intake and move to second shoot position
                    RamseteAction(subsystems, listOf(
                        Pose2d(-0.9, 0.0, Rotation2d(0.0)), 
                        Pose2d(-4.0, -1.2, Rotation2d.fromDegrees(0.0))
                    ), false),
                    Wait(0.0),
                    // intake 2 balls from the human player station
                    ParallelRaceGroup(
                        RamseteAction(subsystems, listOf(
                            Pose2d(-4.0, -1.2, Rotation2d.fromDegrees(0.0)),
                            Pose2d(x1, y1, Rotation2d.fromDegrees(-angle))
                        ), true),
                        SpinUp(subsystems, 14500.0, 13500.0),
                        DefaultIndex(subsystems)
                    ),
                    // shoots 2 balls
                    AutoAlign(subsystems, 0.5, false),
                    Shoot(subsystems, driver, 14500.0, 13500.0, 1.25),
                    // move backward and pick up last ball
                    ParallelRaceGroup(
                        RamseteAction(subsystems, listOf(
                            Pose2d(x1, y1, Rotation2d.fromDegrees(-angle)),
                            Pose2d(x2, y2, Rotation2d.fromDegrees(-angle))
                        )),
                        SpinUp(subsystems, 15500.0, 15000.0)
                    ),
                    // shoots 1 ball
                    AutoAlign(subsystems, 0.5, false),
                    Shoot(subsystems, driver, 15500.0, 15000.0, 1.0)
                )
            ) 
        )
    }
}