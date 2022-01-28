package frc.robot.auto
import frc.robot.classes.Routine
import edu.wpi.first.math.geometry.Pose2d
import edu.wpi.first.math.geometry.Rotation2d
import frc.robot.subsystems.Drivetrain
import frc.robot.commands.RamseteAction

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

class PathToShooter(m_drivetrain: Drivetrain) : Routine() {
    val drivetrain: Drivetrain = m_drivetrain
    
    init {
        commandgroup = SequentialCommandGroup();
        startingpose = Pose2d(0.0, 0.0, Rotation2d(0.0));
        // addCommands(
        //     RamseteAction(drivetrain, listOf(
        //         Pose2d(drivetrain.pose.getX(), drivetrain.pose.getY(), drivetrain.pose.getRotation()), 
        //         Pose2d(0.0, 0.0, Rotation2d(0.0))
        //     ), true)
        // )
    }
}