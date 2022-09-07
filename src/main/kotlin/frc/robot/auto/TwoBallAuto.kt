package frc.robot.auto

import edu.wpi.first.math.geometry.Pose2d
import edu.wpi.first.math.geometry.Rotation2d

import edu.wpi.first.wpilibj.XboxController;

import frc.robot.subsystems.*
import frc.robot.commands.*
import frc.robot.classes.SubsystemHolder
import frc.robot.Util;
import frc.robot.commands.Shoot
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;

class TwoBallAuto(_subsystems: SubsystemHolder, _driver: XboxController) : SequentialCommandGroup() {
    val subsystems: SubsystemHolder = _subsystems
    val driver: XboxController = _driver
    init {

            addCommands(
                // run intake and move to first shoot position
                ParallelRaceGroup(
                    RunIntake(subsystems, 0.0),
                    //Feed(feeder),
                    SequentialCommandGroup(
                        StartFeeding(subsystems),
                        ParallelRaceGroup(
                            Util.generateRamsete(subsystems.drivetrain, listOf(
                                Pose2d(0.0, 0.0, Rotation2d(0.0)), 
                                Pose2d(-0.3, 0.0, Rotation2d(0.0))
                            ))
                        ),
                        ParallelRaceGroup(
                            SpinUp(subsystems, 15500.0, 15500.0),
                            Util.generateRamsete(subsystems.drivetrain, listOf(
                                Pose2d(-0.3, 0.0, Rotation2d(0.0)),
                                Pose2d(-1.0, 0.0, Rotation2d(0.0))
                            ))
                        ),
                        // autoalign and index/shoot first 2 balls
                        AutoAlign(subsystems, 1.0, false),
                        Shoot(subsystems, driver, 15500.0, 15500.0, 5.0))
            )
        )
    }
}