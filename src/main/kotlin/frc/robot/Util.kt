package frc.robot;
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
import edu.wpi.first.math.kinematics.SwerveModuleState;
import frc.robot.subsystems.Drivetrain;
import edu.wpi.first.math.trajectory.constraint.SwerveDriveKinematicsConstraint;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.XboxController;
import frc.robot.DriveConstants;
import frc.robot.commands.Drive;
import frc.robot.commands.StopDrivetrain;
import edu.wpi.first.wpilibj2.command.Command;

object Util {
    public fun radiansToNativeUnits(units: Double): Double {
        return (units / (2 * Math.PI)) * (DriveConstants.ticksPerRotation)
    }
    public fun nativeUnitsToRadians(units: Double): Double {
        return (2 * Math.PI * units) / DriveConstants.ticksPerRotation;
    }
    public fun nativeUnitsToMetersPerSecond(units: Double): Double {
        return (units / DriveConstants.ticksPerRotation) * DriveConstants.wheelCircumference * 10
    }
    public fun metersPerSecondToNativeUnits(units: Double): Double {
        return ((units / DriveConstants.wheelCircumference) * DriveConstants.ticksPerRotation) / 10
    }
    public fun withDeadband(input: Double): Double {
        return if (Math.abs(input) <= DriveConstants.controllerDeadband) 0.0 else input; 
    }
    public fun degreesToRadians(input: Double): Double {
        return input * Math.PI / 180.0;
    }
    public fun radiansToDegrees(input: Double): Double {
        return input * 180.0 / Math.PI;
    }


    public fun generateRamsete(drivetrain: Drivetrain, poses: List<Pose2d>): Command {
        // Create config for trajectory
        val constraint: SwerveDriveKinematicsConstraint = SwerveDriveKinematicsConstraint(DriveConstants.kinematics, DriveConstants.SwerveRamsete.maxVelocity);
        val config: TrajectoryConfig =
            TrajectoryConfig(
                    DriveConstants.SwerveRamsete.maxVelocity,
                    DriveConstants.SwerveRamsete.maxAcceleration)
                // Add kinematics to ensure max speed is actually obeyed
                .setKinematics(DriveConstants.kinematics)
                .addConstraint(constraint);
    
        // An example trajectory to follow.  All units in meters.
        val exampleTrajectory: Trajectory =
            TrajectoryGenerator.generateTrajectory(
                // Start at the origin facing the +X direction
                poses,
                config);
        val kxcontroller: PIDController = PIDController(DriveConstants.SwerveRamsete.kpx, 0.0, 0.0);
        val kycontroller: PIDController = PIDController(DriveConstants.SwerveRamsete.kpy, 0.0, 0.0);
        val thetacontroller: ProfiledPIDController = ProfiledPIDController(DriveConstants.SwerveRamsete.kptheta, 0.0, 0.0, DriveConstants.SwerveRamsete.kThetaControllerConstraints);
        val swerveControllerCommand: SwerveControllerCommand =
            SwerveControllerCommand(
                exampleTrajectory,
                drivetrain::pose,
                DriveConstants.kinematics,
                kxcontroller,
                kycontroller,
                thetacontroller,
                { input: Array<SwerveModuleState> -> drivetrain.updateMotors(input) },
                arrayOf(drivetrain));
    
        // Reset odometry to the starting pose of the trajectory.
        //drivetrain.resetOdometry(exampleTrajectory.getInitialPose());
        return swerveControllerCommand.andThen(StopDrivetrain(drivetrain));
      }
}