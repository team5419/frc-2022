package org.team5419.frc2022.auto.actions

import org.team5419.frc2022.subsystems.Drivetrain
import org.team5419.frc2022.fault.math.units.derived.*
import org.team5419.frc2022.fault.math.units.*
import org.team5419.frc2022.fault.math.geometry.Vector2
import org.team5419.frc2022.fault.math.geometry.Pose2d
import org.team5419.frc2022.fault.auto.Action
import kotlin.math.PI
import edu.wpi.first.wpilibj.trajectory.constraint.DifferentialDriveVoltageConstraint
import edu.wpi.first.wpilibj.trajectory.constraint.DifferentialDriveKinematicsConstraint
import edu.wpi.first.wpilibj.trajectory.TrajectoryGenerator
import edu.wpi.first.wpilibj.trajectory.TrajectoryConfig
import edu.wpi.first.wpilibj.trajectory.constraint.CentripetalAccelerationConstraint
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveWheelSpeeds
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveKinematics
import edu.wpi.first.wpilibj.geometry.Translation2d as WPILibTranslation2d
import edu.wpi.first.wpilibj.geometry.Rotation2d as WPILibRotation2d
import edu.wpi.first.wpilibj.geometry.Pose2d as WPILibPose2d
import edu.wpi.first.wpilibj.controller.SimpleMotorFeedforward
import edu.wpi.first.wpilibj.controller.RamseteController
import org.team5419.frc2022.DriveConstants

// refrences:
// https://github.com/wpilibsuite/allwpilib/blob/master/wpilibNewCommands/src/main/java/edu/wpi/first/wpilibj2/command/RamseteCommand.java
// https://docs.wpilib.org/en/latest/docs/software/examples-tutorials/trajectory-tutorial/index.html

private fun pose2dToWPILibPose2d(pose: Pose2d): WPILibPose2d {
    return WPILibPose2d(
        pose.translation.x.inMeters(),
        pose.translation.y.inMeters(),
        WPILibRotation2d(
            pose.rotation.radian.value
        )
    )
}

public class RamseteAction(
    val poses: Array<Pose2d>,

    // default settings
    val reversed: Boolean = false,
    val maxVelocity: SIUnit<LinearVelocity> = DriveConstants.MaxVelocity,
    val maxAcceleration: SIUnit<LinearAcceleration> = DriveConstants.MaxAcceleration,
    val maxVoltage: SIUnit<Volt> = 12.volts,
    val trackWidth: SIUnit<Meter> = DriveConstants.TrackWidth,
    val beta: Double = DriveConstants.Beta,
    val zeta: Double = DriveConstants.Zeta,
    val kS: Double = DriveConstants.DriveKs,
    val kV: Double = DriveConstants.DriveKv,
    val kA: Double = DriveConstants.DriveKa
) : Action() {

    val driveKinematics = DifferentialDriveKinematics(trackWidth.inMeters())

    val feedforward = SimpleMotorFeedforward(kS, kV, kA)

    val voltageConstraint = DifferentialDriveVoltageConstraint(
        feedforward,
        driveKinematics,
        maxVoltage.value
    )

    val driveKinematicsConstraint = DifferentialDriveKinematicsConstraint(
        driveKinematics,
        maxVelocity.value
    )

    val config = TrajectoryConfig(
        maxVelocity.value,
        maxAcceleration.value
    ).apply {
        setKinematics(driveKinematics)
        setReversed(reversed)
        addConstraint(voltageConstraint)
        addConstraint(driveKinematicsConstraint)
        addConstraint(CentripetalAccelerationConstraint(
            DriveConstants.MaxCentripetalAcceleration.value
        ))
    }

    val trajectory = TrajectoryGenerator.generateTrajectory(

        // list of points
        poses.map({ pose2dToWPILibPose2d(it) }),

        // the trajectory configuration
        config
    )

    val controller = RamseteController(beta, zeta)

    var prevTime = 0.0.seconds
    var prevSpeed = DifferentialDriveWheelSpeeds(0.0, 0.0)

    init {
        finishCondition += { getTime() > trajectory.getTotalTimeSeconds() }
    }

    override public fun start() {
        timer.stop()
        timer.reset()
        timer.start()
        println("started motion")
    }

    override fun update(dt: SIUnit<Second>) {
        //println("moving")
        val time = getTime()

        val chassisSpeed = controller.calculate(
            Drivetrain.pose,

            trajectory.sample(time.inSeconds())
        )

        val setSpeed = driveKinematics.toWheelSpeeds(chassisSpeed)

        val leftFeedForward = feedforward.calculate(
            setSpeed.leftMetersPerSecond,
            (setSpeed.leftMetersPerSecond - prevSpeed.leftMetersPerSecond) / dt.value
        )

        val rightFeedForward = feedforward.calculate(
            setSpeed.rightMetersPerSecond,
            (setSpeed.rightMetersPerSecond - prevSpeed.rightMetersPerSecond) / dt.value
        )

        prevSpeed = setSpeed

        Drivetrain.setVelocity(
            setSpeed.leftMetersPerSecond.meters.velocity * 2.0,
            setSpeed.rightMetersPerSecond.meters.velocity * 2.0,
            leftFeedForward.volts,
            rightFeedForward.volts
        )
    }


    override public fun finish() {
        println("done driving")
    }
}
