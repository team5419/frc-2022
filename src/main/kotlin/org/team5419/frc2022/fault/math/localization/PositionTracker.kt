package org.team5419.frc2022.fault.math.localization

import edu.wpi.first.wpilibj.Timer
import org.team5419.frc2022.fault.math.geometry.Pose2d
import org.team5419.frc2022.fault.math.geometry.Rotation2d
import org.team5419.frc2022.fault.math.units.SIUnit
import org.team5419.frc2022.fault.math.units.Second
import org.team5419.frc2022.fault.math.units.seconds
import org.team5419.frc2022.fault.util.CSVWritable
import org.team5419.frc2022.fault.util.Source
import kotlin.reflect.KProperty

abstract class PositionTracker(
    val heading: Source<Rotation2d>,
    private val buffer: TimeInterpolatableBuffer<Pose2d> = TimeInterpolatableBuffer()
) : Source<Pose2d>, CSVWritable {

    var robotPosition = Pose2d()
        private set

    private var lastHeading = Rotation2d()
    private var headingOffset = Rotation2d()

    override fun invoke() = robotPosition

    @Synchronized fun reset(pose: Pose2d = Pose2d()) = resetInternal(pose)

    protected open fun resetInternal(pose: Pose2d) {
        robotPosition = pose
        val newHeading = heading()
        lastHeading = newHeading
        headingOffset = -newHeading + pose.rotation
        buffer.clear()
    }

    @Synchronized
    fun update() {
        val newHeading = heading()
        val headingDelta = newHeading - lastHeading
        val newRobotPosition = robotPosition + update(headingDelta)
        robotPosition = Pose2d(
                newRobotPosition.translation,
                newHeading + headingOffset // check this
        )
        lastHeading = newHeading

        buffer[Timer.getFPGATimestamp().seconds] = robotPosition
    }

    protected abstract fun update(deltaHeading: Rotation2d): Pose2d

    operator fun get(timestamp: SIUnit<Second>) = buffer[timestamp] ?: Pose2d()

    operator fun getValue(thisRef: Any?, property: KProperty<*>): Pose2d = robotPosition
    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: Pose2d) = reset(value)

    override fun toCSV() = robotPosition.toCSV()
}
