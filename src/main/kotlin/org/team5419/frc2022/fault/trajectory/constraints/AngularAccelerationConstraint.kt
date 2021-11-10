package org.team5419.frc2022.fault.trajectory.constraints

import org.team5419.frc2022.fault.math.geometry.Pose2dWithCurvature
import org.team5419.frc2022.fault.math.units.SIUnit
import org.team5419.frc2022.fault.math.units.derived.AngularAcceleration

import kotlin.math.absoluteValue
import kotlin.math.sqrt
import kotlin.math.abs

class AngularAccelerationConstraint constructor(
    val maxAngularAcceleration: SIUnit<AngularAcceleration>
) : TimingConstraint<Pose2dWithCurvature> {

    init {
        require(maxAngularAcceleration > 0.0) {
            "Cannot have a negative angular acceleration"
        }
    }

    override fun getMaxVelocity(state: Pose2dWithCurvature): Double {
        return sqrt(maxAngularAcceleration.value / state.dkds.absoluteValue)
    }

    override fun getMinMaxAcceleration(
        state: Pose2dWithCurvature,
        velocity: Double
    ): TimingConstraint.MinMaxAcceleration {
        val maxAbsoluteAcceleration = abs(
            (maxAngularAcceleration.value - (velocity * velocity * state.dkds)) / state.curvature
        )
        return TimingConstraint.MinMaxAcceleration(-maxAbsoluteAcceleration, maxAbsoluteAcceleration)
    }
}
