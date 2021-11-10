package org.team5419.frc2022.fault.trajectory.types

import org.team5419.frc2022.fault.math.geometry.State
import org.team5419.frc2022.fault.trajectory.TrajectoryIterator

interface Trajectory<U : Comparable<U>, S : State<S>> {
    val points: List<S>
    val reversed get() = false

    fun getPoint(index: Int) = TrajectoryPoint(index, points[index])

    fun sample(interpolant: U): TrajectorySamplePoint<S>

    val firstInterpolant: U
    val lastInterpolant: U

    val firstState: S
    val lastState: S

    operator fun iterator(): TrajectoryIterator<U, S>
}

data class TrajectoryPoint<S>(
    val index: Int,
    val state: S
)

data class TrajectorySamplePoint<S> (
    val state: S,
    val floorIndex: Int,
    val ceilIndex: Int
) {
    constructor(point: TrajectoryPoint<S>): this(point.state, point.index, point.index)
}
