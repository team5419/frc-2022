package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import kotlin.math.*
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab

import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout
import edu.wpi.first.networktables.NetworkTableInstance
import frc.robot.subsystems.Drivetrain
import edu.wpi.first.math.geometry.Rotation2d
import edu.wpi.first.math.controller.PIDController
import frc.robot.classes.DriveSignal
import frc.robot.VisionConstants
import frc.robot.Lookup
import frc.robot.LookupEntry
class Vision(tab: ShuffleboardTab, drivetrain: Drivetrain) : SubsystemBase() {
    val m_drivetrain: Drivetrain = drivetrain
    private val mLimelight = NetworkTableInstance.getDefault().getTable("limelight")

    val inverted: Boolean = false
    private val mTargetHeight: Double = VisionConstants.targetHeight
    private val mCameraHeight: Double = VisionConstants.cameraHeight
    private val mCameraAngle: Double = VisionConstants.cameraAngle
    public val maxSpeed = VisionConstants.maxAutoAlignSpeed
    private val layout: ShuffleboardLayout = tab.getLayout("Vision", BuiltInLayouts.kList);
    fun getHorizontalOffset(): Double {
        if (inverted) {
            return -mLimelight.getEntry("tx").getDouble(0.0)
        } 
        else {
            return mLimelight.getEntry("tx").getDouble(0.0)
        }
    }

    fun getVerticalOffset(): Double {
        if (inverted) {
            return -mLimelight.getEntry("ty").getDouble(0.0)
        } 
        else {
            return mLimelight.getEntry("ty").getDouble(0.0)
        }
    }

    // calculate horizontal distance based on verticle and horizontal offset
    fun getHorizontalDistance(): Double { 
        return (mTargetHeight - mCameraHeight) / Math.tan(Math.toRadians(mCameraAngle + getVerticalOffset()))
    }

    //calculate which setpoint is closest
    public fun getShotSetpoint(): LookupEntry {
        return Lookup.getClosest(getHorizontalDistance())
    }

    // PID loop controller
    public val turnController: PIDController =
        PIDController(
            VisionConstants.TurnPID.P,
            VisionConstants.TurnPID.I,
            VisionConstants.TurnPID.D
        ).apply {
            setTolerance(VisionConstants.turnTolerance)
        }

    public val throttleController: PIDController = 
        PIDController(
            VisionConstants.ThrottlePID.P, 
            VisionConstants.ThrottlePID.I, 
            VisionConstants.ThrottlePID.D
        ).apply {
            setTolerance(VisionConstants.throttleTolerance)
        }

    // add the PID controller to shuffleboard
    init {
        layout.addNumber("Offset", { getHorizontalOffset() })
        layout.addBoolean("Aligned", { turnAligned() })
        layout.addNumber("Horizontal Distance", { getHorizontalDistance() })
    }

    // check if the limelight is picking up on the target
    public fun isTargetFound(): Boolean {
        return mLimelight.getEntry("tv").getDouble(0.0) > 0.0 && getVerticalOffset() > 0.0
    }

    public fun turnAligned(): Boolean {
        return isTargetFound() && turnController.atSetpoint() && m_drivetrain.averageSpeed < 0.1
    }

    public fun throttleAligned(distance : Double): Boolean {
        return isTargetFound() && Math.abs(getHorizontalDistance() - distance) > 0.05
    }

    public fun autoAlignTurn() : DriveSignal {
        // get the pid loop output
        var output = turnController.calculate(getHorizontalOffset() + VisionConstants.targetOffset)

        // do we need to align / can we align?
        if(!turnAligned() && isTargetFound()) {
            return DriveSignal(output, -output)
        }

        return DriveSignal(0.0, 0.0)
    }

    public fun autoAlignThrottle(distance : Double) : DriveSignal {
        var output = throttleController.calculate(getHorizontalDistance() - distance)

        if(!throttleAligned(distance) && isTargetFound())
        {
            return DriveSignal(-output, -output)
        }

        return DriveSignal(0.0, 0.0)
    }

    enum class LightMode { On, Off, Blink }

    var lightMode: LightMode = LightMode.On
        set(value) {
            if (value == field) return
            when (value) {
                LightMode.On -> mLimelight.getEntry("ledMode").setNumber(3)
                LightMode.Off -> mLimelight.getEntry("ledMode").setNumber(3) // 1
                LightMode.Blink -> mLimelight.getEntry("ledMode").setNumber(2)
            }
            field = value
        }

    public fun on() {
        lightMode = LightMode.On
    }

    public fun off() {
        lightMode = LightMode.Off
    }
}