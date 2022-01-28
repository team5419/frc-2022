package frc.robot.classes

import edu.wpi.first.math.geometry.Pose2d
import edu.wpi.first.math.geometry.Rotation2d
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

open class Routine() {
    public var commandgroup: SequentialCommandGroup = SequentialCommandGroup();
    public var startingpose: Pose2d = Pose2d(0.0, 0.0, Rotation2d(0.0));
}