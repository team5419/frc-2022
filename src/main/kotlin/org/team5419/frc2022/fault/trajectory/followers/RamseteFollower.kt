package org.team5419.frc2022.fault.trajectory.followers

import org.team5419.frc2022.fault.trajectory.TrajectoryIterator
import org.team5419.frc2022.fault.trajectory.types.TimedEntry
import org.team5419.frc2022.fault.math.geometry.Pose2dWithCurvature
import org.team5419.frc2022.fault.math.geometry.Pose2d

import org.team5419.frc2022.fault.math.epsilonEquals
import org.team5419.frc2022.fault.math.units.SIUnit
import org.team5419.frc2022.fault.math.units.Second
import org.team5419.frc2022.fault.math.units.operations.times
import org.team5419.frc2022.fault.math.units.operations.div

import kotlin.math.sqrt
import kotlin.math.sin

class RamseteFollower(
    private val kBeta: Double,
    private val kZeta: Double
) : TrajectoryFollower() {

    override fun calculateState(
        iterator: TrajectoryIterator<SIUnit<Second>, TimedEntry<Pose2dWithCurvature>>,
        robotPose: Pose2d
    ): TrajectoryFollower.TrajectoryFollowerVelocityOutput {
        val referenceState = iterator.currentState.state

        // Calculate goal in robot's coordinates
        val error = referenceState.state.pose inFrameOfReferenceOf robotPose

        // Get reference linear and angular velocities
        val vd = referenceState.velocity.value
        val wd = vd * referenceState.state.curvature

        // Compute gain
        val k1 = 2 * kZeta * sqrt(wd * wd + kBeta * vd * vd)

        // Get angular error in bounded radians
        val angleError = error.rotation.radian

        return TrajectoryFollower.TrajectoryFollowerVelocityOutput(
            linearVelocity = SIUnit(vd * error.rotation.cos + k1 * error.translation.x.value),
            angularVelocity = SIUnit(wd + kBeta * vd * sinc(angleError.value) *
                    error.translation.y.value + k1 * angleError.value)
        )
    }

    companion object {
        private fun sinc(theta: Double) =
            if (theta epsilonEquals 0.0) {
                1.0 - 1.0 / 6.0 * theta * theta
            } else sin(theta) / theta
    }
}
