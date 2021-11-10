package org.team5419.frc2022.fault.math.localization

import org.team5419.frc2022.fault.math.geometry.Pose2d
import org.team5419.frc2022.fault.math.geometry.Rotation2d
import org.team5419.frc2022.fault.math.geometry.Twist2d
import org.team5419.frc2022.fault.math.units.meters
import org.team5419.frc2022.fault.util.Source

class TankPositionTracker(
    headingSource: Source<Rotation2d>,
    val leftDistanceSource: Source<Double>,
    val rightDistanceSource: Source<Double>,
    buffer: TimeInterpolatableBuffer<Pose2d> = TimeInterpolatableBuffer()
) : PositionTracker(headingSource, buffer) {

    private var lastLeftDistance = 0.0
    private var lastRightDistance = 0.0

    override fun resetInternal(pose: Pose2d) {
        lastLeftDistance = leftDistanceSource()
        lastRightDistance = rightDistanceSource()
        super.resetInternal(pose)
    }

    override fun update(deltaHeading: Rotation2d): Pose2d {
        val currentLeftDistance = leftDistanceSource()
        val currentRightDistance = rightDistanceSource()
        val leftDelta = currentLeftDistance - lastLeftDistance
        val rightDelta = currentRightDistance - lastRightDistance
        val dx = (leftDelta + rightDelta) / 2.0
        return Twist2d(dx.meters, 0.0.meters, deltaHeading.radian).asPose
    }
}
