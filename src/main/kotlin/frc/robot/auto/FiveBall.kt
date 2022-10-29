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

class FiveBall(_subsystems: SubsystemHolder, _driver: XboxController) : SequentialCommandGroup() {
    val subsystems: SubsystemHolder = _subsystems
    val driver: XboxController = _driver
    init {
        val initialTheta: Double = 24.3;
        val firstDist: Double = 1.7;
        addCommands(
            // x = forward/back, y = sideways
            // +x = back
            // +y = right
            // +theta = counterclockwise
            ResetGyro(subsystems.drivetrain, initialTheta), // zero gyro
            ParallelRaceGroup(
                RunIntake(subsystems, 0.0),
                SequentialCommandGroup(
                    AutoDrive(subsystems, firstDist * 0.5 * Math.cos(Util.degreesToRadians(initialTheta)), firstDist * 0.5 * Math.sin(Util.degreesToRadians(initialTheta)), initialTheta),
                    ParallelRaceGroup(
                        SpinUp(subsystems, 14500.0, 14500.0),
                        AutoDriveAndAlign(subsystems, driver, firstDist * Math.cos(Util.degreesToRadians(initialTheta)), firstDist * Math.sin(Util.degreesToRadians(initialTheta)) - 0.2)
                    ),
                    ParallelCommandGroup(
                        Shoot(subsystems, driver, 14500.0, 14500.0, 2.0),
                        Deploy(subsystems, 0.27, 0.75)
                    ),
                    ParallelCommandGroup(
                        AutoDrive(subsystems, 5.8, 1.5, 0.0),
                        Deploy(subsystems, -0.15, 0.75)
                    ),
                    // AutoDrive(subsystems, 5.8, 2.0, 0.0),
                    // Wait(1.0),
                    ParallelRaceGroup(
                        SpinUp(subsystems, 14500.0, 14500.0),
                        AutoDriveAndAlign(subsystems, driver, firstDist * Math.cos(Util.degreesToRadians(initialTheta)), firstDist * Math.sin(Util.degreesToRadians(initialTheta)))
                    ),
                    ParallelCommandGroup(
                        Deploy(subsystems, 0.3, 0.75),
                        Shoot(subsystems, driver, 14500.0, 14500.0, 2.0)
                    ),
                    ParallelCommandGroup(
                        AutoDrive(subsystems, -1.3, 1.25, 90.0),
                        Deploy(subsystems, -0.2, 0.75)
                    ),
                    ParallelRaceGroup(
                        SpinUp(subsystems, 15000.0, 15000.0),
                        AutoDriveAndAlign(subsystems, driver, -1.1, 2.5)
                    ),
                    ParallelCommandGroup(
                        Shoot(subsystems, driver, 15000.0, 15000.0, 2.0),
                        Deploy(subsystems, 0.27, 0.75)
                    )
                )
            )
        )
    }
}