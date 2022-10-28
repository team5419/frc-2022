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

class SampleAutoDrive(_subsystems: SubsystemHolder, _driver: XboxController) : SequentialCommandGroup() {
    val subsystems: SubsystemHolder = _subsystems
    val driver: XboxController = _driver
    init {
        addCommands(
            // x = forward/back, y = sideways
            // +x = back
            // +y = right
            // +theta = counterclockwise
            ResetGyro(subsystems.drivetrain, 0.0), // zero gyro
            AutoDrive(subsystems, -1.0, 0.0, 0.0)//, // move y only (should be forward-backward)
            // AutoDrive(subsystems, 1.0, 1.0, 0.0), // move x only (should be left-right)
            // AutoDrive(subsystems, 0.0, 0.0, 0.0), // move both x and y 
            // AutoDrive(subsystems, 0.0, 0.0, 90.0), // turn only
            // AutoDrive(subsystems, 1.0, 1.0, 45.0) // turn and move at the same time
        )
    }
}