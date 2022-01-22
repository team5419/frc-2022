package frc.robot.commands

import kotlin.math.PI
import edu.wpi.first.math.trajectory.constraint.DifferentialDriveVoltageConstraint
import edu.wpi.first.math.trajectory.constraint.DifferentialDriveKinematicsConstraint
import edu.wpi.first.math.trajectory.TrajectoryGenerator
import edu.wpi.first.math.trajectory.TrajectoryConfig
import edu.wpi.first.math.trajectory.constraint.CentripetalAccelerationConstraint
import edu.wpi.first.math.kinematics.DifferentialDriveWheelSpeeds
import edu.wpi.first.math.kinematics.DifferentialDriveKinematics
import edu.wpi.first.math.geometry.Translation2d
import edu.wpi.first.math.geometry.Rotation2d
import edu.wpi.first.math.geometry.Pose2d
import edu.wpi.first.math.controller.SimpleMotorFeedforward
import edu.wpi.first.math.controller.RamseteController
import frc.robot.subsystems.Drivetrain
import frc.robot.DriveConstants
import edu.wpi.first.math.spline.SplineHelper

import edu.wpi.first.wpilibj.Timer

import edu.wpi.first.wpilibj2.command.CommandBase;

// refrences:
// https://github.com/wpilibsuite/allwpilib/blob/master/wpilibNewCommands/src/main/java/edu/wpi/first/wpilibj2/command/RamseteCommand.java
// https://docs.wpilib.org/en/latest/docs/software/examples-tutorials/trajectory-tutorial/index.html

class RamseteAction(m_subsystem: Drivetrain, m_poses: List<Pose2d>, m_reversed: Boolean = false) : CommandBase() {
    private val drivetrain: Drivetrain = m_subsystem
    private val poses: List<Pose2d> = m_poses
    private val reversed: Boolean = m_reversed
    private val maxVelocity: Double = DriveConstants.Ramsete.maxVelocity
    private val maxAcceleration: Double = DriveConstants.Ramsete.maxAcceleration
    private val maxVoltage: Double = 12.0 // volts
    private val trackWidth: Double = DriveConstants.Ramsete.trackWidth
    private val beta: Double = DriveConstants.Ramsete.beta
    private val zeta: Double = DriveConstants.Ramsete.zeta
    private val kS: Double = DriveConstants.Ramsete.ks
    private val kV: Double = DriveConstants.Ramsete.kv
    private val kA: Double = DriveConstants.Ramsete.ka
        
    val driveKinematics = DifferentialDriveKinematics(trackWidth)

    val feedforward = SimpleMotorFeedforward(kS, kV, kA)

    val voltageConstraint = DifferentialDriveVoltageConstraint(
        feedforward,
        driveKinematics,
        maxVoltage
    )

    val driveKinematicsConstraint = DifferentialDriveKinematicsConstraint(
        driveKinematics,
        maxVelocity
    )

    val config = TrajectoryConfig(
        maxVelocity,
        maxAcceleration
    ).apply {
        setKinematics(driveKinematics)
        setReversed(!reversed) // our motors are weird
        addConstraint(voltageConstraint)
        addConstraint(driveKinematicsConstraint)
        addConstraint(CentripetalAccelerationConstraint(
            DriveConstants.Ramsete.maxCentripetalAcceleration
        ))
    }

    val trajectory = TrajectoryGenerator.generateTrajectory(poses, config)

    val controller = RamseteController(beta, zeta)

    var prevSpeed = DifferentialDriveWheelSpeeds(0.0, 0.0)
    var prevTime: Double = 0.0
    var timer: Timer = Timer()
    

    init {
        addRequirements(m_subsystem);
    }

    // Called when the command is initially scheduled.
    override fun initialize() {

        timer.reset()
        timer.start()
    
    }

  // Called every time the scheduler runs while the command is scheduled.
    override fun execute() {
        val time = timer.get()
        val chassisSpeed = controller.calculate(
            drivetrain.pose,
            trajectory.sample(time)
        )

        val setSpeed = driveKinematics.toWheelSpeeds(chassisSpeed)

        val leftFeedForward = feedforward.calculate(
            setSpeed.leftMetersPerSecond,
            (setSpeed.leftMetersPerSecond - prevSpeed.leftMetersPerSecond) / (time - prevTime)
        )

        val rightFeedForward = feedforward.calculate(
            setSpeed.rightMetersPerSecond,
            (setSpeed.rightMetersPerSecond - prevSpeed.rightMetersPerSecond) / (time - prevTime)
        )

        prevSpeed = setSpeed
        prevTime = time

        drivetrain.setVelocity(
            setSpeed.leftMetersPerSecond * 0.4,
            setSpeed.rightMetersPerSecond * 0.4,
            leftFeedForward,
            rightFeedForward
        )
    }

  // Called once the command ends or is interrupted.
    override fun end(interrupted: Boolean) {
        println("Done driving")
        drivetrain.drive(0.0, 0.0, false)
    }

  // Returns true when the command should end.
    override fun isFinished(): Boolean {
        return timer.get() >= trajectory.getTotalTimeSeconds()
    }
}

