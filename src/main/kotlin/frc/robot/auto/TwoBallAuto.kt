package frc.robot.auto

import edu.wpi.first.math.geometry.Pose2d
import edu.wpi.first.math.geometry.Rotation2d

import edu.wpi.first.wpilibj.XboxController;
import frc.robot.commands.ResetOdometry;
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
                ParallelRaceGroup(
                    RunIntake(subsystems, 0.0),
                    SequentialCommandGroup(
                        DriveRobotRelative(subsystems, 0.5, 0.0, 0.1),
                        Wait(1.0),
                        DriveRobotRelative(subsystems, 1.5, 0.0, 0.95),
                        AutoAlign(subsystems, driver, 0.75, false),
                        Shoot(subsystems, driver, 15500.0, 15500.0, 5.0))
            )
        )
    }
}