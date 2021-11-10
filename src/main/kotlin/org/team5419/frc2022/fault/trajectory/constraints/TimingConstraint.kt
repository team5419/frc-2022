package org.team5419.frc2022.fault.trajectory.constraints

interface TimingConstraint<S> {
    fun getMaxVelocity(state: S): Double

    fun getMinMaxAcceleration(state: S, velocity: Double): MinMaxAcceleration

    data class MinMaxAcceleration(
        val minAcceleration: Double,
        val maxAcceleration: Double
    ) {
        constructor(): this(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY)

        val valid = minAcceleration <= maxAcceleration

        companion object {
            val noLimits = MinMaxAcceleration()
        }
    }
}
