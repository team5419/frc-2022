package org.team5419.frc2020.auto.actions
import org.team5419.frc2020.subsystems.Drivetrain
import kotlin.math.PI
import edu.wpi.first.wpilibj.trajectory.constraint.DifferentialDriveVoltageConstraint
import edu.wpi.first.wpilibj.trajectory.constraint.DifferentialDriveKinematicsConstraint
import edu.wpi.first.wpilibj.trajectory.TrajectoryGenerator
import edu.wpi.first.wpilibj.trajectory.TrajectoryConfig
import edu.wpi.first.wpilibj.trajectory.constraint.CentripetalAccelerationConstraint
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveWheelSpeeds
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveKinematics
import edu.wpi.first.wpilibj.geometry.Translation2d
import edu.wpi.first.wpilibj.geometry.Rotation2d
import edu.wpi.first.wpilibj.geometry.Pose2d
import edu.wpi.first.wpilibj.controller.SimpleMotorFeedforward
import edu.wpi.first.wpilibj.controller.RamseteController
import org.team5419.frc2020.DriveConstants


open class RamseteAction: Action() {
    open var poses: Array<Pose2d>
    open var reversed: Boolean

    constructor(_poses: Array<Pose2d>, _reversed: Boolean = false) {
        this.poses = _poses
        this.reversed = _reversed
    }

    val driveKinematics = DifferentialDriveKinematics(DriveConstants.Ramsete.trackWidth)

    val feedforward = SimpleMotorFeedforward(
        DriveConstants.Ramsete.ks, DriveConstants.Ramsete.kv, DriveConstants.Ramsete.ka
    )

    val voltageConstraint = DifferentialDriveVoltageConstraint(
        feedforward,
        driveKinematics,
        DriveConstants.Ramsete.maxVoltage
    )

    val driveKinematicsConstraint = DifferentialDriveKinematicsConstraint(
        driveKinematics,
        DriveConstants.Ramsete.maxVelocity
    )

    val config = TrajectoryConfig(
        DriveConstants.Ramsete.maxVelocity,
        DriveConstants.Ramsete.maxAcceleration
    ).apply {
        setKinematics(driveKinematics)
        setReversed(this.reversed)
        addConstraint(voltageConstraint)
        addConstraint(driveKinematicsConstraint)
        addConstraint(CentripetalAccelerationConstraint(
            DriveConstants.Ramsete.maxCentripetalAcceleration
        ))
    }

    val trajectory = TrajectoryGenerator.generateTrajectory(poses, config)

    val controller = RamseteController(DriveConstants.Ramsete.beta, DriveConstants.Ramsete.zeta)

    var prevTime = 0.0 // seconds
    var prevSpeed = DifferentialDriveWheelSpeeds(0.0, 0.0)

    init { // CHANGE
        this.addCondition({ this.timer.get() > trajectory.getTotalTimeSeconds() })
    }

    override public fun start() {
        timer.stop()
        timer.reset()
        timer.start()
        println("started motion")
    }

    override fun update(dt: Double) {
        //println("moving")
        val time = this.timer.get()

        val chassisSpeed = controller.calculate(
            Drivetrain.pose,

            trajectory.sample(time)
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
            leftFeedForward,
            rightFeedForward
        )
    }


    override public fun finish() {
        println("done driving")
    }
}
