package frc.robot.commands

import kotlin.math.PI
import edu.wpi.first.math.trajectory.constraint.DifferentialDriveVoltageConstraint
import edu.wpi.first.math.trajectory.constraint.DifferentialDriveKinematicsConstraint
import edu.wpi.first.math.trajectory.TrajectoryGenerator
import edu.wpi.first.math.trajectory.TrajectoryConfig
import edu.wpi.first.math.trajectory.constraint.CentripetalAccelerationConstraint
import edu.wpi.first.math.kinematics.DifferentialDriveWheelSpeeds
import edu.wpi.first.math.kinematics.DifferentialDriveKinematics

import edu.wpi.first.math.kinematics.SwerveDriveWheelSpeeds // does this exist lmfao
import edu.wpi.first.math.kinematics.SwerveDriveKinematics

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
import frc.robot.classes.SubsystemHolder

class RamseteAction(m_subsystem: SubsystemHolder, m_poses: List<Pose2d>, m_reversed: Boolean = false) : CommandBase() {
    private val drivetrain: Drivetrain = m_subsystem.drivetrain
    private val poses: List<Pose2d> = m_poses
    private val reversed: Boolean = m_reversed
    private val maxVelocity: Double = DriveConstants.Ramsete.maxVelocity
    private val maxAcceleration: Double = DriveConstants.Ramsete.maxAcceleration
    private val maxVoltage: Double = 12.0 // volts
    
    // wpilib stuff - just trust it
    //val driveKinematics = DifferentialDriveKinematics(DriveConstants.Ramsete.trackWidth) 
    val driveKinematics = SwerveDriveKinematics(Drivetrain.m_frontLeftLocation, m_frontRightLocation, m_backLeftLocation, m_backRightLocation)// changed to swerve
    val feedforward = SimpleMotorFeedforward(DriveConstants.Ramsete.ks, DriveConstants.Ramsete.kv, DriveConstants.Ramsete.ka)
    val voltageConstraint = DifferentialDriveVoltageConstraint(feedforward, driveKinematics, maxVoltage)
    val driveKinematicsConstraint = DifferentialDriveKinematicsConstraint(driveKinematics, maxVelocity)

    // again, just trust it
    val config = TrajectoryConfig(maxVelocity, maxAcceleration).apply {
        setKinematics(driveKinematics)
        setReversed(!reversed) // our motors are weird
        addConstraint(voltageConstraint)
        addConstraint(driveKinematicsConstraint)
        addConstraint(CentripetalAccelerationConstraint(
            DriveConstants.Ramsete.maxCentripetalAcceleration
        ))
    }

    // calculate a trajectory based on a list of positions (starting and ending)
    val trajectory = TrajectoryGenerator.generateTrajectory(poses, config)

    val controller = RamseteController(DriveConstants.Ramsete.beta, DriveConstants.Ramsete.zeta)

    var prevSpeed = DifferentialDriveWheelSpeeds(0.0, 0.0)
    var prevTime: Double = 0.0
    var timer: Timer = Timer()
    
    //add "checkForObsticles()" function, if returns true, split the path at the midpoint and call two seperate ramsete actions (do this recursively)

    init {
        addRequirements(m_subsystem.drivetrain);
    }

    override fun initialize() {
        timer.reset()
        timer.start()
        drivetrain.brakeMode = true;
    }

    override fun execute() {
        val time = timer.get()
        // find the speed that the robot should be at at a specific time in the path
        val chassisSpeed = controller.calculate(drivetrain.pose, trajectory.sample(time))
        val setSpeed = driveKinematics.toWheelSpeeds(chassisSpeed)
        // calculate the feedforwards of both sides
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

        // set velocity of drivetrain
        drivetrain.setVelocity(
            setSpeed.leftMetersPerSecond,
            setSpeed.rightMetersPerSecond,
            leftFeedForward,
            rightFeedForward
        )
    }

    override fun end(interrupted: Boolean) {
        //println("Done driving!")
        drivetrain.brakeMode = false;
        drivetrain.drive(0.0, 0.0, false)

    }

    override fun isFinished(): Boolean {
        // end command if enough time has passed
        return timer.get() >= trajectory.getTotalTimeSeconds()
    }
}

