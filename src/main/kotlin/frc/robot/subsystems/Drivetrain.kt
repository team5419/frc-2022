package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.math.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.math.geometry.Rotation2d;
import com.ctre.phoenix.sensors.PigeonIMU
import com.ctre.phoenix.motorcontrol.can.TalonFX
import com.ctre.phoenix.motorcontrol.can.TalonSRX
import com.ctre.phoenix.motorcontrol.*
import kotlin.math.*

import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout
import edu.wpi.first.math.geometry.Pose2d

class Drivetrain(tab: ShuffleboardTab) : SubsystemBase() {

    // declare motors and ports
    public val leftLeader: TalonFX = TalonFX(0)
    public val leftFollower: TalonFX = TalonFX(1)
    public val rightLeader: TalonFX = TalonFX(2)
    public val rightFollower: TalonFX = TalonFX(3)
    public val gyro: PigeonIMU = PigeonIMU(20)
    private val layout: ShuffleboardLayout = tab.getLayout("Drivetrain", BuiltInLayouts.kList).withPosition(2, 0).withSize(1, 2);
    // configure the motors and add to shuffleboard
    init {
        leftLeader.apply {
            configFactoryDefault(100)
            configSupplyCurrentLimit(SupplyCurrentLimitConfiguration(true, 20.0, 0.0, 0.0), 100)

            setSensorPhase(false)
            setInverted(false)

            setSelectedSensorPosition(0.0, 0, 100)

            configVoltageCompSaturation(12.0, 100)
            enableVoltageCompensation(true)
            setStatusFramePeriod(StatusFrame.Status_2_Feedback0, 10, 100)
            setControlFramePeriod(ControlFrame.Control_3_General, 10)

            setNeutralMode(NeutralMode.Coast)

            configClosedLoopPeakOutput(0, 0.1, 100)
        }

        rightLeader.apply {
            configFactoryDefault(100)
            configSupplyCurrentLimit(SupplyCurrentLimitConfiguration(true, 20.0, 0.0, 0.0), 100)

            setSensorPhase(true)
            setInverted(true)

            setSelectedSensorPosition(0.0, 0, 100)

            configVoltageCompSaturation(12.0, 100)
            enableVoltageCompensation(true)
            setStatusFramePeriod(StatusFrame.Status_2_Feedback0, 10, 100)
            setControlFramePeriod(ControlFrame.Control_3_General, 10)

            setNeutralMode(NeutralMode.Coast)
        }

        leftFollower.apply {
            configFactoryDefault(100)
            configSupplyCurrentLimit(SupplyCurrentLimitConfiguration(true, 20.0, 0.0, 0.0), 100)

            // follow the master
            follow(leftLeader)
            setInverted(InvertType.FollowMaster)

            configVoltageCompSaturation(12.0, 100)
            enableVoltageCompensation(true)
            
            configClosedLoopPeakOutput(0, 0.1, 100)
            setStatusFramePeriod(StatusFrame.Status_2_Feedback0, 10, 100)
            setControlFramePeriod(ControlFrame.Control_3_General, 10)
        }

        rightFollower.apply {
            configFactoryDefault(100)
            configSupplyCurrentLimit(SupplyCurrentLimitConfiguration(true, 20.0, 0.0, 0.0), 100)

            // follow the master
            follow(rightLeader)
            setInverted(InvertType.FollowMaster)

            configVoltageCompSaturation(12.0, 100)
            enableVoltageCompensation(true)

            configClosedLoopPeakOutput(0, 0.1, 100)
            setStatusFramePeriod(StatusFrame.Status_2_Feedback0, 10, 100)
            setControlFramePeriod(ControlFrame.Control_3_General, 10)
        }

         gyro.apply {
            configFactoryDefault(100)
            setFusedHeading(0.0, 100)
        }
        layout.addNumber("example", { 0.0 });
    }

    // get angle from gyro
    val angle: Double
        get() = -gyro.getFusedHeading()
        
    override fun periodic() {
    }

    override fun simulationPeriodic() {
    }
}
