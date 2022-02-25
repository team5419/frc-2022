package org.team5419.frc2022.auto

import edu.wpi.first.wpilibj.geometry.Rotation2d
import edu.wpi.first.wpilibj.geometry.Pose2d
import edu.wpi.first.wpilibj.trajectory.TrajectoryUtil
import edu.wpi.first.wpilibj.trajectory.Trajectory

import org.team5419.frc2022.auto.actions


val routines = arrayOf<Routine>(
    Routine("test routine",
        RamseteAction( arrayOf<Pose2d>(
            Pose2d(0.0, 0.0, Rotation2d(0.0)),
            Pose2d(4.5, 0.0, Rotation2d(0.0))
        ) )
    )
)
