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
import frc.robot.subsystems.Drivetrain;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.XboxController;
import frc.robot.DriveConstants;
import frc.robot.commands.Drive
object Util {
    public fun radiansToNativeUnits(units: Double): Double {
        return (units / (2 * Math.PI)) * (DriveConstants.ticksPerRotation / 10)
    }
    public fun nativeUnitsToRadians(units: Double): Double {
        return (20 * Math.PI * units) / DriveConstants.ticksPerRotation;
    }
    public fun nativeUnitsToMeters(units: Double): Double {
        return (DriveConstants.wheelCircumference * units / DriveConstants.ticksPerRotation)
    }
    public fun nativeUnitsToMetersPerSecond(units: Double): Double {
        return units * 10.0 / DriveConstants.ticksPerRotation * DriveConstants.wheelCircumference
    }
    public fun metersPerSecondToNativeUnits(units: Double): Double {
        return (units / DriveConstants.wheelCircumference * DriveConstants.ticksPerRotation / 10)
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
}