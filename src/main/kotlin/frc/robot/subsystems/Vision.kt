package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import kotlin.math.*
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab
import edu.wpi.first.networktables.NetworkTableInstance
import frc.robot.subsystems.Drivetrain
import edu.wpi.first.math.geometry.Rotation2d
import edu.wpi.first.math.geometry.Pose2d
import edu.wpi.first.math.controller.PIDController
import frc.robot.VisionConstants
import frc.robot.Lookup
import frc.robot.LookupEntry
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout
import edu.wpi.first.networktables.NetworkTableEntry
import edu.wpi.first.networktables.EntryListenerFlags
import edu.wpi.first.networktables.EntryNotification
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets

class Vision(tab: ShuffleboardTab, drivetrain: Drivetrain) : SubsystemBase() {
    val m_drivetrain: Drivetrain = drivetrain
    private val mLimelight = NetworkTableInstance.getDefault().getTable("limelight")
    private val layout: ShuffleboardLayout = tab.getLayout("Vision", BuiltInLayouts.kList).withPosition(3, 0).withSize(2, 4);
    private val mTargetHeight: Double = VisionConstants.targetHeight
    private val mCameraHeight: Double = VisionConstants.cameraHeight
    private val mCameraAngle: Double = VisionConstants.cameraAngle
    public val maxSpeed = VisionConstants.maxAutoAlignSpeed
    public var adjustPosition: Boolean = false;

    fun getHorizontalOffset(): Double {
        return mLimelight.getEntry("tx").getDouble(0.0)
    }

    fun getVerticalOffset(): Double {
        return mLimelight.getEntry("ty").getDouble(0.0)
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
        layout.addBoolean("Sees target", { isTargetFound() })
    }

    // check if the limelight is picking up on the target
    public fun isTargetFound(): Boolean {
        return mLimelight.getEntry("tv").getDouble(0.0) > 0.0 && getVerticalOffset() != 0.0
    }

    public fun turnAligned(): Boolean {
        return isTargetFound() && turnController.atSetpoint()/* Math.abs(getHorizontalOffset()) < 0.3*/ && m_drivetrain.getAverageSpeed() < 0.1
    }

    public fun throttleAligned(distance : Double): Boolean {
        return isTargetFound() && Math.abs(getHorizontalDistance() - distance) < 0.05
    }

    public fun autoAlignTurn() : Double {
        // get the pid loop output
        var output =/*  if (getHorizontalOffset() > 0.0) 0.5 else -0.5; */turnController.calculate(getHorizontalOffset() + VisionConstants.targetOffset)
        println("turn aligned: ${turnAligned()}, target found: ${isTargetFound()}")
        // do we need to align / can we align?
        if(!turnAligned() && isTargetFound()) {
            return -output
        }

        return 0.0
    }

    public fun autoAlignThrottle(distance : Double) : Double {
        var output = throttleController.calculate(getHorizontalDistance() - distance)

        if(!throttleAligned(distance) && isTargetFound())
        {
            return -output
        }

        return 0.0
    }

    enum class LightMode { On, Off, Blink }

    var lightMode: LightMode = LightMode.Off
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

    public override fun periodic() {
        if(adjustPosition && isTargetFound()) {
            val angle: Rotation2d = Rotation2d.fromDegrees(m_drivetrain.angle);
            val dist: Double = getHorizontalDistance();
            val newY: Double = Math.sin(angle.getRadians()) * dist;
            val newX: Double = Math.cos(angle.getRadians()) * dist;
            //m_drivetrain.resetOdometry(Pose2d(newX, newY, angle), angle)
            //println("x: ${newX}, y: ${newY}, dist: ${dist}, angle: ${angle}")
        }
    }
}