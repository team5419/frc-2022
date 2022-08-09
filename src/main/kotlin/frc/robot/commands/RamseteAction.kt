package frc.robot.commands; 
import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrajectoryGenerator;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj2.command.SwerveControllerCommand;
import edu.wpi.first.math.trajectory.TrapezoidProfile.Constraints;
import edu.wpi.first.math.controller.ProfiledPIDController;
import frc.robot.subsystems.Drivetrain;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.XboxController;
import frc.robot.DriveConstants;
//import frc.robot.classes.SubsystemHolder

class RamseteAction(_drivetrain: Drivetrain) : CommandBase() {
  private val drivetrain: Drivetrain = _drivetrain

  init {
    addRequirements(_drivetrain);
  }

  override fun initialize() {
    val config: TrajectoryConfig =
        TrajectoryConfig(
                DriveConstants.Ramsete.maxVelocity,
                DriveConstants.Ramsete.maxAcceleration)
            // Add kinematics to ensure max speed is actually obeyed
            .setKinematics(drivetrain.m_kinematics);

    // An example trajectory to follow.  All units in meters.
    val exampleTrajectory: Trajectory =
        TrajectoryGenerator.generateTrajectory(
            // Start at the origin facing the +X direction
            Pose2d(0.0, 0.0, Rotation2d(0.0)),
            // Pass through these two interior waypoints, making an 's' curve path
            listOf(Translation2d(1.0, 1.0), Translation2d(2.0, -1.0)),
            // End 3 meters straight ahead of where we started, facing forward
            Pose2d(3.0, 0.0, Rotation2d(0.0)),
            config);

    val thetaController: ProfiledPIDController =
        ProfiledPIDController(
            DriveConstants.PID.P, DriveConstants.PID.I, DriveConstants.PID.D, Constraints(DriveConstants.Ramsete.maxVelocity, DriveConstants.Ramsete.maxAcceleration));
    thetaController.enableContinuousInput(-Math.PI, Math.PI);

    val swerveControllerCommand: SwerveControllerCommand =
        SwerveControllerCommand(
            exampleTrajectory,
            drivetrain::pose, // Functional interface to feed supplier
            drivetrain.m_kinematics,

            // Position controllers
            PIDController(DriveConstants.Ramsete.kpx, 0.0, 0.0),
            PIDController(DriveConstants.Ramsete.kpy, 0.0, 0.0),
            thetaController,
            drivetrain::updateMotors,
            drivetrain);

    // Reset odometry to the starting pose of the trajectory.
    drivetrain.resetOdometry(exampleTrajectory.getInitialPose());

    // Run path following command, then stop at the end.
    return swerveControllerCommand.andThen(() -> m_robotDrive.drive(0, 0, 0, false));
  }

  override fun execute() {
    //drivetrain.drive(driver.getLeftY().toDouble(), driver.getRightX().toDouble(), 0.0);
  }

  override fun end(interrupted: Boolean) {}

  override fun isFinished(): Boolean {
    return false;
  }
}
