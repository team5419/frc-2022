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
class FourBallAuto(_subsystems: SubsystemHolder, m_driver: XboxController) : SequentialCommandGroup() {
    val subsystems: SubsystemHolder = _subsystems
    val driver: XboxController = m_driver
    
    init {
        addCommands(
            // run intake and move to first shoot position
            ParallelRaceGroup(
                RunIntake(subsystems, 0.0),
                //Feed(feeder),
                SequentialCommandGroup(
                    StartFeeding(subsystems),
                    ParallelRaceGroup(
                        RamseteAction(subsystems, listOf(
                            Pose2d(0.0, 0.0, Rotation2d(0.0)), 
                            Pose2d(-0.3, 0.0, Rotation2d(0.0))
                        ), false)
                    ),
                    ParallelRaceGroup(
                        SpinUp(subsystems, 15500.0, 15500.0),
                        RamseteAction(subsystems, listOf(
                            Pose2d(-0.3, 0.0, Rotation2d(0.0)),
                            Pose2d(-1.0, 0.0, Rotation2d(0.0))
                        ), false)
                    ),
                    // autoalign and index/shoot first 2 balls
                    AutoAlign(subsystems, 0.75, false),
                    Shoot(subsystems, driver, 15500.0, 15500.0, 1.0),
                    // run intake and move to second shoot position
                    RamseteAction(subsystems, listOf(
                        Pose2d(-1.0, 0.0, Rotation2d(0.0)), 
                        Pose2d(-4.0, -1.8, Rotation2d.fromDegrees(0.0))
                    ), false),
                    Wait(0.25),
                    // intake 2 balls from the human player station
                    // moves to new shot location
                    ParallelRaceGroup(
                        RamseteAction(subsystems, listOf(
                            Pose2d(-4.0, -1.8, Rotation2d.fromDegrees(0.0)), 
                            Pose2d(-0.3, 0.0, Rotation2d.fromDegrees(0.0))
                        ), true),
                        SpinUp(subsystems, 15250.0, 15250.0)
                    ),
                    // shoots 2 balls
                    AutoAlign(subsystems, 0.5, false),
                    Shoot(subsystems, driver, 15250.0, 15250.0, 1.0)
                )
            )
            
        )
    }
}