package org.team5419.frc2022.fault.subsystems.drivetrain

import org.team5419.frc2022.fault.hardware.LinearBerkeliumMotor
import org.team5419.frc2022.fault.math.geometry.Pose2d
import org.team5419.frc2022.fault.trajectory.followers.TrajectoryFollower
import org.team5419.frc2022.fault.trajectory.followers.TrajectoryFollowerOutput

interface ITrajectoryFollowingDrive {

    val leftMasterMotor: LinearBerkeliumMotor
    val rightMasterMotor: LinearBerkeliumMotor

    val trajectoryFollower: TrajectoryFollower

    val robotPosition: Pose2d

    fun setOutput(output: TrajectoryFollowerOutput)

    fun zeroOutputs() {
        leftMasterMotor.setNeutral()
        rightMasterMotor.setNeutral()
    }
}
