package frc.robot.auto;

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
import java.util.function.Consumer;
import java.util.function.Supplier;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class RamseteAction(
      _trajectory: Trajectory,
      _pose: Supplier<Pose2d>,
      _kinematics: SwerveDriveKinematics,
      _xController: PIDController,
      _yController: PIDController,
      _thetaController: ProfiledPIDController,
      _outStates: Consumer<Array<SwerveModuleState>>,
      _desiredRotation: Supplier<Rotation2d>? = null
) : CommandBase() {
  private val timer: Timer = Timer();
  private val trajectory: Trajectory = _trajectory;
  private val pose: Supplier<Pose2d> = _pose;;
  private val kinematics: SwerveDriveKinematics = _kinematics;
  private val controller: HolonomicDriveController = HolonomicDriveController(_xController, _yController, _thetaController);
  private val outStates: Consumer<Array<SwerveModuleState>> = _outStates;
  private val desiredRotation = if(_desiredRotation == null) (() -> trajectory.getStates().get(trajectory.getStates().size() - 1).poseMeters.getRotation()) else _desiredRotation;

  public override fun isFinished(): Boolean {
    return timer.hasElapsed(trajectory.getTotalTimeSeconds());
  }

  public override fun initialize() {
    timer.reset();
    timer.start();
  }

  public override fun execute() {
    val curTime: Double = timer.get();
    var desiredState = m_trajectory.sample(curTime);
    Rotation2d desiredRotation = new Rotation2d();

    if (m_wantsVisionAlign.get()) {
      if (mLimelight.hasTarget()) {
        desiredRotation = Rotation2d.fromDegrees(m_pose.get().getRotation().getDegrees() - mLimelight.getOffset()[0]);
      } else {
        desiredRotation = Rotation2d.fromDegrees(Superstructure.getInstance().getRealAimingParameters().get().getVehicleToGoalRotation().getWPIRotation2d().getDegrees() + 180.0);
        System.out.println(Superstructure.getInstance().getRealAimingParameters().get().getVehicleToGoalRotation().getWPIRotation2d().getDegrees() + 180.0);
      }
    } else {
      desiredRotation = m_desiredRotation.get();
    }

    var targetChassisSpeeds =
        m_controller.calculate(m_pose.get(), desiredState, desiredRotation);
    var targetModuleStates = m_kinematics.toSwerveModuleStates(targetChassisSpeeds);

    m_outputModuleStates.accept(targetModuleStates);
  }

  public void done() {
    m_timer.stop();
    m_outputModuleStates.accept(m_kinematics.toSwerveModuleStates((ChassisSpeeds.fromFieldRelativeSpeeds(0, 0, 0, Rotation2d.fromDegrees(0)))));
  }

  // get initial pose
  public Pose2d getInitialPose() {
    return m_trajectory.getInitialPose();
  }

}