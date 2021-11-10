package org.team5419.frc2022.fault.trajectory.constraints

import org.team5419.frc2022.fault.math.geometry.Pose2dWithCurvature
import org.team5419.frc2022.fault.math.units.SIUnit
import org.team5419.frc2022.fault.math.units.derived.LinearAcceleration

import kotlin.math.absoluteValue
import kotlin.math.sqrt

class CentripetalAccelerationConstraint constructor(
    private val maxCentripetalAcceleration: SIUnit<LinearAcceleration>
) : TimingConstraint<Pose2dWithCurvature> {

    override fun getMaxVelocity(state: Pose2dWithCurvature) =
        sqrt((maxCentripetalAcceleration.value / state.curvature).absoluteValue)

    override fun getMinMaxAcceleration(
        state: Pose2dWithCurvature,
        velocity: Double
    ) = TimingConstraint.MinMaxAcceleration.noLimits
}
