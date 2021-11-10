package org.team5419.frc2022.fault.trajectory.followers

import org.team5419.frc2022.fault.trajectory.TrajectoryIterator
import org.team5419.frc2022.fault.trajectory.types.TimedEntry

import org.team5419.frc2022.fault.math.geometry.Pose2d
import org.team5419.frc2022.fault.math.geometry.Pose2dWithCurvature
import org.team5419.frc2022.fault.math.geometry.Vector2
import org.team5419.frc2022.fault.math.units.SIUnit
import org.team5419.frc2022.fault.math.units.Second
import org.team5419.frc2022.fault.math.units.Meter
import org.team5419.frc2022.fault.math.units.inches
import org.team5419.frc2022.fault.math.units.seconds
import org.team5419.frc2022.fault.math.units.meters

import kotlin.math.pow

class PurePursuitFollower(
    private val kLat: Double,
    private val kLookaheadTime: SIUnit<Second>,
    private val kMinLookaheadDistance: SIUnit<Meter> = 18.inches // inches
) : TrajectoryFollower() {

    override fun calculateState(
        iterator: TrajectoryIterator<SIUnit<Second>, TimedEntry<Pose2dWithCurvature>>,
        robotPose: Pose2d
    ): TrajectoryFollower.TrajectoryFollowerVelocityOutput {
        val referencePoint = iterator.currentState

        // Compute the lookahead state.
        val lookaheadState: Pose2d = calculateLookaheadPose2d(iterator, robotPose)

        // Find the appropriate lookahead point.
        val lookaheadTransform = lookaheadState inFrameOfReferenceOf robotPose

        // Calculate latitude error.
        val xError = (referencePoint.state.state.pose inFrameOfReferenceOf robotPose).translation.x.value

        // Calculate the velocity at the reference point.
        val vd = referencePoint.state.velocity.value

        // Calculate the distance from the robot to the lookahead.
        val l = lookaheadTransform.translation.norm.value

        // Calculate the curvature of the arc that connects the robot and the lookahead point.
        val curvature = 2 * lookaheadTransform.translation.y.value / l.pow(2)

        // Adjust the linear velocity to compensate for the robot lagging behind.
        val adjustedLinearVelocity = vd * lookaheadTransform.rotation.cos + kLat * xError

        return TrajectoryFollower.TrajectoryFollowerVelocityOutput(
            linearVelocity = SIUnit(adjustedLinearVelocity),
            // v * curvature = omega
            angularVelocity = SIUnit(adjustedLinearVelocity * curvature)
        )
    }

    @Suppress("ReturnCount")
    private fun calculateLookaheadPose2d(
        iterator: TrajectoryIterator<SIUnit<Second>, TimedEntry<Pose2dWithCurvature>>,
        robotPose: Pose2d
    ): Pose2d {
        val lookaheadPoseByTime = iterator.preview(kLookaheadTime).state.state.pose

        // The lookahead point is farther from the robot than the minimum lookahead distance.
        // Therefore we can use this point.
        if ((lookaheadPoseByTime inFrameOfReferenceOf robotPose).translation.norm >= kMinLookaheadDistance.value) {
            return lookaheadPoseByTime
        }

        var lookaheadPoseByDistance = iterator.currentState.state.state.pose
        var previewedTime = 0.seconds

        // Run the loop until a distance that is greater than the minimum lookahead distance is found or until
        // we run out of "trajectory" to search. If this happens, we will simply extend the end of the trajectory.
        while (iterator.progress > previewedTime) {
            previewedTime += 0.02.seconds

            lookaheadPoseByDistance = iterator.preview(previewedTime).state.state.pose
            val lookaheadDistance = (lookaheadPoseByDistance inFrameOfReferenceOf robotPose).translation.norm

            if (lookaheadDistance > kMinLookaheadDistance.value) {
                return lookaheadPoseByDistance
            }
        }

        // Extend the trajectory.
        val remaining =
            kMinLookaheadDistance - (lookaheadPoseByDistance inFrameOfReferenceOf robotPose).translation.norm

        return lookaheadPoseByDistance.transformBy(
            Pose2d(
                Vector2(remaining * if (iterator.trajectory.reversed) -1 else 1, 0.0.meters)
            )
        )
    }
}
