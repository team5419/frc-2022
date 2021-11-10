package org.team5419.frc2022.fault.auto

import org.team5419.frc2022.fault.math.geometry.Pose2d
import org.team5419.frc2022.fault.math.geometry.Vector2
import org.team5419.frc2022.fault.math.units.derived.degrees
import org.team5419.frc2022.fault.math.units.meters
import java.util.concurrent.atomic.AtomicLong

class Routine(
    name: String? = null,
    val startPose: Pose2d = Pose2d(Vector2(0.0.meters, 0.0.meters), 0.degrees),
    override val actions: MutableList<Action>
) : SerialAction(actions) {

    constructor(
        name: String? = null,
        startPose: Pose2d = Pose2d(Vector2(0.0.meters, 0.0.meters), 0.degrees),
        vararg actions: Action
    ) : this(name, startPose, actions.toMutableList())

    val name = name ?: "Routine ${routineId.incrementAndGet()}"

    companion object {
        private val routineId = AtomicLong()
    }
}

fun <T : ActionGroup> T.toRoutine(name: String? = null, startPose: Pose2d = Pose2d()) = Routine(
        name,
        startPose,
        this.actions.toMutableList()
)
