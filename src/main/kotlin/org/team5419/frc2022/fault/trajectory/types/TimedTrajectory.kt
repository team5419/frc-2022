@file:Suppress("ConstructorParameterNaming")
package org.team5419.frc2022.fault.trajectory.types

import org.team5419.frc2022.fault.math.epsilonEquals
import org.team5419.frc2022.fault.trajectory.TrajectoryIterator
import org.team5419.frc2022.fault.math.geometry.State
import org.team5419.frc2022.fault.math.lerp
import org.team5419.frc2022.fault.math.geometry.Pose2dWithCurvature
import org.team5419.frc2022.fault.math.geometry.Pose2d
import org.team5419.frc2022.fault.math.units.SIUnit
import org.team5419.frc2022.fault.math.units.Second
import org.team5419.frc2022.fault.math.units.derived.LinearAcceleration
import org.team5419.frc2022.fault.math.units.derived.LinearVelocity
import org.team5419.frc2022.fault.math.units.operations.times

class TimedTrajectory<S : State<S>>(
    override val points: List<TimedEntry<S>>,
    override val reversed: Boolean
) : Trajectory<SIUnit<Second>, TimedEntry<S>> {

    override fun sample(interpolant: SIUnit<Second>) = sample(interpolant.value)

    fun sample(interpolant: Double) = when {
        interpolant >= lastInterpolant.value -> TrajectorySamplePoint(getPoint(points.size - 1))
        interpolant <= firstInterpolant.value -> TrajectorySamplePoint(getPoint(0))
        else -> {
            val (index, entry) = points.asSequence()
                .withIndex()
                .first { (index, entry) -> index != 0 && entry.t.value >= interpolant }
            val prevEntry = points[ index - 1 ]
            if (entry.t epsilonEquals prevEntry.t) {
                TrajectorySamplePoint(entry, index, index)
            } else {
                TrajectorySamplePoint(
                    prevEntry.interpolate(
                        entry,
                        (interpolant - prevEntry.t.value) / (entry.t.value - prevEntry.t.value)
                    ),
                    index - 1,
                    index
                )
            }
        }
    }

    override val firstState get() = points.first()
    override val lastState get() = points.last()

    override val firstInterpolant get() = firstState.t
    override val lastInterpolant get() = lastState.t

    override fun iterator() = TimedIterator(this)
}

data class TimedEntry<S : State<S>> internal constructor(
    val state: S,
    val t: SIUnit<Second> = SIUnit(0.0),
    val velocity: SIUnit<LinearVelocity> = SIUnit(0.0),
    val acceleration: SIUnit<LinearAcceleration> = SIUnit(0.0)
) : State<TimedEntry<S>> {

    override fun interpolate(other: TimedEntry<S>, t: Double): TimedEntry<S> {
        val newT = this.t.lerp(other.t, t)
        val deltaT = newT - this.t
        if (deltaT.value < 0.0) return other.interpolate(this, 1.0 - t)

        val reversing =
                this.velocity.value < 0.0 || this.velocity.value epsilonEquals 0.0 && this.acceleration.value < 0.0

        val newV = this.velocity + this.acceleration * deltaT
        val newS = (if (reversing) -1.0 else 1.0) * (this.velocity * deltaT + 0.5 * this.acceleration * deltaT * deltaT)

        return TimedEntry(
                state.interpolate(other.state, (newS / state.distance(other.state)).value),
                newT,
                newV,
                this.acceleration
        )
    }

    override fun distance(other: TimedEntry<S>) = state.distance(other.state)

    override fun toCSV() = "$t, $state, $velocity, $acceleration"
}

class TimedIterator<S : State<S>>(
    trajectory: TimedTrajectory<S>
) : TrajectoryIterator<SIUnit<Second>, TimedEntry<S>>(trajectory) {
    override fun addition(a: SIUnit<Second>, b: SIUnit<Second>) = a + b
}

fun Trajectory<SIUnit<Second>, TimedEntry<Pose2dWithCurvature>>.mirror() =
        TimedTrajectory(points.map { TimedEntry(it.state.mirror, it.t, it.velocity, it.acceleration) }, this.reversed)

fun Trajectory<SIUnit<Second>, TimedEntry<Pose2dWithCurvature>>.transform(transform: Pose2d) =
        TimedTrajectory(
                points.map { TimedEntry(it.state + transform, it.t, it.velocity, it.acceleration) },
                this.reversed
        )

val Trajectory<SIUnit<Second>, TimedEntry<Pose2dWithCurvature>>.duration get() = this.lastState.t
