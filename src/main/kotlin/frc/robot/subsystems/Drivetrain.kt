package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.math.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.math.kinematics.SwerveDriveOdometry;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.math.kinematics.ChassisSpeeds
import edu.wpi.first.math.geometry.Rotation2d;
import com.ctre.phoenix.sensors.PigeonIMU
import com.ctre.phoenix.sensors.BasePigeonSimCollection
import com.ctre.phoenix.motorcontrol.can.TalonFX
import com.ctre.phoenix.motorcontrol.can.TalonSRX
import com.ctre.phoenix.motorcontrol.*
import kotlin.math.*
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout
import edu.wpi.first.wpilibj.motorcontrol.Talon
import edu.wpi.first.math.geometry.Pose2d
import edu.wpi.first.math.geometry.Translation2d
import edu.wpi.first.wpilibj.smartdashboard.Field2d
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
class Drivetrain() : SubsystemBase() {
    private val motor1: TalonFX;
    private val motor2 : TalonFX;
    // configure the motors and add to shuffleboard
    init {
        motor1 = TalonFX(0);
        motor2 = TalonFX(1);
    }
    fun drive(throttle: Double, turn : Double) {
        motor1.set(ControlMode.PercentOutput, throttle - turn);
        motor2.set(ControlMode.PercentOutput, throttle + turn);
    }



    override fun periodic() {
        //motor1.set(ControlMode.PercentOutput, 1.0);
    }

    override fun simulationPeriodic() {
    }
}