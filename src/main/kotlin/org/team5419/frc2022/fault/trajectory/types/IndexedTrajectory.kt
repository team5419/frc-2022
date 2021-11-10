package org.team5419.frc2022.fault.trajectory.types

import org.team5419.frc2022.fault.trajectory.TrajectoryIterator
import org.team5419.frc2022.fault.math.geometry.State

class IndexedTrajectory<S : State<S>>(
    override val points: List<S>
) : Trajectory<Double, S> {
    override fun sample(interpolant: Double) = when {
        points.isEmpty() -> throw IndexOutOfBoundsException("Trajectory is empty!")
        interpolant <= 0.0 -> TrajectorySamplePoint(getPoint(0))
        interpolant >= points.size - 1 -> TrajectorySamplePoint(getPoint(points.size - 1))
        else -> {
            val index = Math.floor(interpolant).toInt()
            val percent = interpolant - index
            when {
                percent <= Double.MIN_VALUE -> TrajectorySamplePoint(getPoint(index))
                percent >= 1.0 - Double.MIN_VALUE -> TrajectorySamplePoint(getPoint(index + 1))
                else -> TrajectorySamplePoint(
                    points[index].interpolate(points[index], percent),
                    index,
                    index + 1
                )
            }
        }
    }

    override val firstState get() = points.first()
    override val lastState get() = points.last()

    override val firstInterpolant get() = 0.0
    override val lastInterpolant get() = (points.size - 1.0).coerceAtLeast(0.0)

    override fun iterator() = IndexedIterator(this)
}

class IndexedIterator<S : State<S>>(
    trajectory: IndexedTrajectory<S>
) : TrajectoryIterator<Double, S>(trajectory) {
    override fun addition(a: Double, b: Double) = a + b
}
