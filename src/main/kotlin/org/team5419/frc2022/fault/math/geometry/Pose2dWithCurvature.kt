package org.team5419.frc2022.fault.math.geometry

import org.team5419.frc2022.fault.math.epsilonEquals
import org.team5419.frc2022.fault.math.interpolate
import org.team5419.frc2022.fault.math.lerp

data class Pose2dWithCurvature(
    val pose: Pose2d,
    val curvature: Double,
    val dkds: Double
) : State<Pose2dWithCurvature> {

    val mirror get() = Pose2dWithCurvature(pose.mirror, -curvature, -dkds)

    override fun interpolate(endValue: Pose2dWithCurvature, t: Double) =
            Pose2dWithCurvature(
                    pose.interpolate(endValue.pose, t),
                    curvature.lerp(endValue.curvature, t),
                    dkds.lerp(endValue.dkds, t)
            )

    override fun distance(other: Pose2dWithCurvature) = pose.distance(other.pose)

    operator fun plus(other: Pose2d) = Pose2dWithCurvature(this.pose + other, curvature, dkds)

    override fun toCSV() = "${pose.toCSV()}, $curvature, $dkds"

    override fun toString() = toCSV()

    override fun hashCode(): Int {
        return super.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (other is Pose2dWithCurvature) {
            return this.pose.equals(other.pose) && this.curvature epsilonEquals other.curvature &&
                    this.dkds epsilonEquals other.dkds
        }
        return false
    }
}
