@file:Suppress("ConstructorParameterNaming")
package org.team5419.frc2022.fault.trajectory.followers

import org.team5419.frc2022.fault.math.geometry.Pose2d
import org.team5419.frc2022.fault.math.geometry.Pose2dWithCurvature
import org.team5419.frc2022.fault.math.physics.DifferentialDrive
import org.team5419.frc2022.fault.math.units.SIUnit
import org.team5419.frc2022.fault.math.units.Second
import org.team5419.frc2022.fault.math.units.derived.AngularVelocity
import org.team5419.frc2022.fault.math.units.derived.LinearVelocity
import org.team5419.frc2022.fault.math.units.derived.LinearAcceleration
import org.team5419.frc2022.fault.math.units.derived.AngularAcceleration
import org.team5419.frc2022.fault.math.units.derived.acceleration
import org.team5419.frc2022.fault.math.units.derived.radians
import org.team5419.frc2022.fault.math.units.meters
import org.team5419.frc2022.fault.math.units.milliseconds
import org.team5419.frc2022.fault.math.units.operations.div
import org.team5419.frc2022.fault.trajectory.TrajectoryIterator
import org.team5419.frc2022.fault.trajectory.types.TimedEntry
import org.team5419.frc2022.fault.trajectory.types.Trajectory
import org.team5419.frc2022.fault.util.time.DeltaTime

abstract class TrajectoryFollower {

    private var trajectoryIterator: TrajectoryIterator<SIUnit<Second>, TimedEntry<Pose2dWithCurvature>>? = null
    private var deltaTimeController = DeltaTime()
    private var previousVelocity: TrajectoryFollowerVelocityOutput? = null

    val referencePoint get() = trajectoryIterator?.currentState
    val isFinished get() = trajectoryIterator?.isDone ?: true

    fun reset(trajectory: Trajectory<SIUnit<Second>, TimedEntry<Pose2dWithCurvature>>) {
        trajectoryIterator = trajectory.iterator()
        deltaTimeController.reset()
        previousVelocity = null
    }

    fun nextState(
        currentRobotPose: Pose2d,
        currentTime: SIUnit<Second> = System.currentTimeMillis().toDouble().milliseconds
    ): TrajectoryFollowerOutput {
        val iterator = trajectoryIterator
        require(iterator != null) {
            "You cannot get the next state from the TrajectoryTracker without a" +
                    "trajectory! Call TrajectoryTracker#reset first!"
        }
        val deltaTime = deltaTimeController.updateTime(currentTime)
        iterator.advance(deltaTime)

        val velocity = calculateState(iterator, currentRobotPose)
        val previousVelocity = this.previousVelocity
        this.previousVelocity = velocity

        // Calculate Acceleration (useful for drive dynamics)
        return if (previousVelocity == null || deltaTime.value <= 0) {
            TrajectoryFollowerOutput(
                    linearVelocity = velocity.linearVelocity,
                    linearAcceleration = 0.0.meters.acceleration,
                    angularVelocity = velocity.angularVelocity,
                    angularAcceleration = 0.0.radians.acceleration
            )
        } else {
            TrajectoryFollowerOutput(
                    linearVelocity = velocity.linearVelocity,
                    linearAcceleration = (velocity.linearVelocity - previousVelocity.linearVelocity) / deltaTime,
                    angularVelocity = velocity.angularVelocity,
                    angularAcceleration = (velocity.angularVelocity - previousVelocity.angularVelocity) / deltaTime
            )
        }
    }

    protected abstract fun calculateState(
        iterator: TrajectoryIterator<SIUnit<Second>, TimedEntry<Pose2dWithCurvature>>,
        robotPose: Pose2d
    ): TrajectoryFollowerVelocityOutput

    protected data class TrajectoryFollowerVelocityOutput constructor(
        val linearVelocity: SIUnit<LinearVelocity>,
        val angularVelocity: SIUnit<AngularVelocity>
    )
}

data class TrajectoryFollowerOutput constructor(
    val linearVelocity: SIUnit<LinearVelocity>,
    val linearAcceleration: SIUnit<LinearAcceleration>,
    val angularVelocity: SIUnit<AngularVelocity>,
    val angularAcceleration: SIUnit<AngularAcceleration>
) {

    val differentialDriveVelocity
        get() = DifferentialDrive.ChassisState(
                linearVelocity.value,
                angularVelocity.value
        )

    val differentialDriveAcceleration
        get() = DifferentialDrive.ChassisState(
                linearAcceleration.value,
                angularAcceleration.value
        )
}
