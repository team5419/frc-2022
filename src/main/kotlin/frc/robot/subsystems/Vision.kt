// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import kotlin.math.*
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab
import edu.wpi.first.networktables.NetworkTableInstance
import frc.robot.subsystems.Drivetrain
import edu.wpi.first.math.geometry.Rotation2d
import edu.wpi.first.math.controller.PIDController
import frc.robot.classes.DriveSignal
import frc.robot.VisionConstants

class Vision(tab: ShuffleboardTab, drivetrain: Drivetrain) : SubsystemBase() {
    val m_drivetrain: Drivetrain = drivetrain
    private val mLimelight = NetworkTableInstance.getDefault().getTable("limelight")


    val inverted: Boolean = false
    private val mTargetHeight: Double = VisionConstants.targetHeight // both meters v
    private val mCameraHeight: Double = VisionConstants.cameraHeight
    private val mCameraAngle: Double = VisionConstants.cameraAngle
    public val maxSpeed = VisionConstants.maxAutoAlignSpeed

    fun getHorizontalOffset(): Double {
        if (inverted) {
            return -mLimelight.getEntry("tx").getDouble(0.0)
        } else {
            return mLimelight.getEntry("tx").getDouble(0.0)
        }
    }

    fun getVerticalOffset(): Double {
        if (inverted) {
            return -mLimelight.getEntry("ty").getDouble(0.0)
        } else {
            return mLimelight.getEntry("ty").getDouble(0.0)
        }
    }

    fun getHorizontalDistance(): Double { // meters
        return (mTargetHeight - mCameraHeight) / Math.tan(Math.toRadians(mCameraAngle + getVerticalOffset()))
    }

    // PID loop controller
    public val controller: PIDController =
        PIDController(
            VisionConstants.PID.P,
            VisionConstants.PID.I,
            VisionConstants.PID.D
        ).apply {
            setTolerance(VisionConstants.tolerance)
        }

    // add the pid controller to shuffleboard
    init {
        tab.addNumber("Offset", { getHorizontalOffset() })
        tab.addBoolean("Aligned", { aligned() })
        tab.addNumber("Horizontal Distance", { getHorizontalDistance() })
    }

    // auto alignment

    public fun isTargetFound(): Boolean {
        return mLimelight.getEntry("tv").getDouble(0.0) > 0.0 && getVerticalOffset() != 0.0
    }

    public fun aligned(): Boolean {
        return isTargetFound() && controller.atSetpoint() && m_drivetrain.averageSpeed < 0.1
    }

    public fun calculate() = controller.calculate(getHorizontalOffset() + VisionConstants.targetOffset)

    public fun autoAlign() : DriveSignal {

        // get the pid loop output
        var output = calculate()

        // can we align / do we need to allign?
        if ( !isTargetFound() || aligned() )
            return DriveSignal(0.0, 0.0)

        // limit the output
        if (output >  maxSpeed) output =  maxSpeed
        if (output < -maxSpeed) output = -maxSpeed

        return DriveSignal(output, -output)
    }

    enum class LightMode { On, Off, Blink }

    var lightMode: LightMode = LightMode.Off
        set(value) {
            if (value == field) return
            when (value) {
                LightMode.On -> mLimelight.getEntry("ledMode").setNumber(3)
                LightMode.Off -> mLimelight.getEntry("ledMode").setNumber(1)
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