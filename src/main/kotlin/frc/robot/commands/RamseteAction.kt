package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.math.controller.HolonomicDriveController;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrajectoryGenerator;
import java.util.function.Consumer;
import java.util.function.Supplier;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.DriveConstants;
import frc.robot.subsystems.Drivetrain;
import edu.wpi.first.math.trajectory.TrajectoryConfig;

public class RamseteAction(
      _drivetrain: Drivetrain,
      _poses: List<Pose2d>,
      _desiredRotation: (() -> Rotation2d)? = null
) : CommandBase() {
  private val drivetrain: Drivetrain = _drivetrain;
  private val timer: Timer = Timer();
  private val trajectory: Trajectory;
  private val controller: HolonomicDriveController = HolonomicDriveController(
    PIDController(DriveConstants.Ramsete.kpx, 0.0, 0.0), 
    PIDController(DriveConstants.Ramsete.kpy, 0.0, 0.0), 
    ProfiledPIDController(DriveConstants.Ramsete.kptheta, 0.0, 0.0, DriveConstants.Ramsete.kThetaControllerConstraints));
  private val desiredRotation: () -> Rotation2d;
  
  init {
    val config: TrajectoryConfig = TrajectoryConfig(DriveConstants.Ramsete.maxVelocity, DriveConstants.Ramsete.maxAcceleration);
    config.setKinematics(DriveConstants.kinematics);
    trajectory = TrajectoryGenerator.generateTrajectory(_poses, config);
    if (_desiredRotation == null) {
      desiredRotation = { -> trajectory.getStates().get(trajectory.getStates().size - 1).poseMeters.getRotation() };
    } else {
      desiredRotation = _desiredRotation;
    }
  }

  public override fun isFinished(): Boolean {
    return timer.hasElapsed(trajectory.getTotalTimeSeconds());
  }

  public override fun initialize() {
    timer.reset();
    timer.start();
  }

  public override fun execute() {
    val curTime: Double = timer.get();
    val desiredState: Trajectory.State = trajectory.sample(curTime);
    val desiredRotation: Rotation2d = desiredRotation();

    val targetChassisSpeeds: ChassisSpeeds =
        controller.calculate(drivetrain.pose(), desiredState, desiredRotation);
    val targetModuleStates: Array<SwerveModuleState> = DriveConstants.kinematics.toSwerveModuleStates(targetChassisSpeeds);
    drivetrain.updateMotors(targetModuleStates);
  }

  public override fun end(isInterrupted: Boolean) {
    timer.stop();
    drivetrain.updateMotors(DriveConstants.kinematics.toSwerveModuleStates((ChassisSpeeds.fromFieldRelativeSpeeds(0.0, 0.0, 0.0, Rotation2d.fromDegrees(0.0)))));
    println("over");
  }
  public fun getInitialPose(): Pose2d {
    return trajectory.getInitialPose();
  }
}