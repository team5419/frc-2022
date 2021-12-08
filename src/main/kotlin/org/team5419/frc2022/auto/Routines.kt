package org.team5419.frc2022.auto

import org.team5419.frc2022.fault.auto.*
import org.team5419.frc2022.fault.math.geometry.Pose2d
import org.team5419.frc2022.fault.math.geometry.Vector2
import org.team5419.frc2022.fault.math.geometry.Pose2dWithCurvature
import org.team5419.frc2022.fault.trajectory.DefaultTrajectoryGenerator
import org.team5419.frc2022.fault.trajectory.constraints.TimingConstraint
import edu.wpi.first.wpilibj.trajectory.TrajectoryUtil
import edu.wpi.first.wpilibj.trajectory.Trajectory


val routines = arrayOf<Routine>(
    Routine("test routine", Pose2d(),
        RamseteAction( arrayOf<Pose2d>(
            Pose2d(0.0.meters, 0.0.meters, 0.0.degrees),
            Pose2d(4.5.meters, 0.0.meters, 0.0.degrees)
        ) )
    )
)
