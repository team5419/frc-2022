package org.team5419.frc2022.fault.trajectory.constraints

import org.team5419.frc2022.fault.math.geometry.Pose2dWithCurvature
import org.team5419.frc2022.fault.math.geometry.Rectangle2d
import org.team5419.frc2022.fault.math.units.SIUnit
import org.team5419.frc2022.fault.math.units.derived.LinearVelocity

public class VelocityLimitRegionConstraint constructor(
    private val region: Rectangle2d,
    private val velocityLimit: SIUnit<LinearVelocity>
) : TimingConstraint<Pose2dWithCurvature> {

    override fun getMaxVelocity(state: Pose2dWithCurvature) =
        if (state.pose.translation in region) velocityLimit.value else Double.POSITIVE_INFINITY

    override fun getMinMaxAcceleration(state: Pose2dWithCurvature, velocity: Double) =
        TimingConstraint.MinMaxAcceleration.noLimits
}
