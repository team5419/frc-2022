package org.team5419.frc2022.fault.simulation

import org.team5419.frc2022.fault.math.geometry.Pose2d
import org.team5419.frc2022.fault.math.geometry.Twist2d
import org.team5419.frc2022.fault.math.physics.DifferentialDrive
import org.team5419.frc2022.fault.math.units.SIUnit
import org.team5419.frc2022.fault.math.units.Second
import org.team5419.frc2022.fault.math.units.derived.radians
import org.team5419.frc2022.fault.math.units.meters
import org.team5419.frc2022.fault.subsystems.drivetrain.IDifferentialFollowerDrive
import org.team5419.frc2022.fault.trajectory.followers.TrajectoryFollower

class SimulatedDifferentialDrive(
    override val differentialDrive: DifferentialDrive,
    override val leftMasterMotor: LinearSimulatedBerkeleiumMotor,
    override val rightMasterMotor: LinearSimulatedBerkeleiumMotor,
    override val trajectoryFollower: TrajectoryFollower,
    private val angularFactor: Double = 1.0

) : IDifferentialFollowerDrive {

    override var robotPosition = Pose2d()

    fun update(deltaTime: SIUnit<Second>) {
        val wheelState = DifferentialDrive.WheelState(
                (leftMasterMotor.velocity * deltaTime.value / differentialDrive.wheelRadius).value,
                (rightMasterMotor.velocity * deltaTime.value / differentialDrive.wheelRadius).value
        )

        val forwardKinematics = differentialDrive.solveForwardKinematics(wheelState)

        robotPosition += Twist2d(
                forwardKinematics.linear.meters,
                0.meters,
                (forwardKinematics.angular * angularFactor).radians
        ).asPose
    }
}
