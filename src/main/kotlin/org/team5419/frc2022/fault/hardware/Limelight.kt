package org.team5419.frc2022.fault.hardware

import edu.wpi.first.networktables.NetworkTableInstance
import org.team5419.frc2022.fault.math.geometry.Rotation2d
import org.team5419.frc2022.fault.math.units.derived.tan
import org.team5419.frc2022.fault.math.units.derived.degrees
import org.team5419.frc2022.fault.math.units.Meter
import org.team5419.frc2022.fault.math.units.inches
import org.team5419.frc2022.fault.math.units.SIUnit

import kotlin.math.PI
import kotlin.math.asin
import kotlin.math.tan

open class Limelight(
    networkTableName: String = "limelight",
    val inverted: Boolean = false,
    private val mTargetHeight: SIUnit<Meter> = 0.inches,
    private val mCameraHeight: SIUnit<Meter> = 0.inches,
    private val mCameraAngle: Rotation2d = Rotation2d() // angle below (or above) horizontal
) {

    // FEEDBACK VARIABLES
    val targetFound: Boolean
        get() = mLimelight.getEntry("tv").getDouble(0.0) > 0.0

    val horizontalOffset: Double
        get() {
            if (inverted) {
                return -mLimelight.getEntry("tx").getDouble(0.0)
            } else {
                return mLimelight.getEntry("tx").getDouble(0.0)
            }
        }

    val verticalOffset: Double
        get() {
            if (inverted) {
                return -mLimelight.getEntry("ty").getDouble(0.0)
            } else {
                return mLimelight.getEntry("ty").getDouble(0.0)
            }
        }

    val targetArea: Double
        get() = mLimelight.getEntry("ta").getDouble(0.0)

    val targetSkew: Double
        get() {
            val skew = mLimelight.getEntry("ts").getDouble(0.0)
            if (skew < -45.0) { /*>*/
                return skew + 90.0
            } else {
                return skew
            }
        }

    val horizontalLength: Double
        get() = mLimelight.getEntry("tlong").getDouble(0.0)

    val verticalLength: Double
        get() = mLimelight.getEntry("tvert").getDouble(0.0)

    val latency: Double
        get() = mLimelight.getEntry("tl").getDouble(0.0)

    // CALCULATED VARIABLES

    val horizontalDistance: SIUnit<Meter>
        get() = (mTargetHeight - mCameraHeight) / (mCameraAngle.radian + verticalOffset.degrees).tan

    val calculateTargetSkew: Double
        get() {
            var temp = (horizontalLength / verticalLength) * kAspectRatio
            if (temp < 0.0) temp = 0.0 /*>*/
            if (temp > 1.0) temp = 1.0
            return (180.0 / PI) * asin(temp)
        }

    // LIGHT MODE
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

    // CAMERA MODE
    var cameraMode: CameraMode = CameraMode.Vision
        set(value) {
            if (field == value) return
            when (value) {
                CameraMode.Driver -> mLimelight.getEntry("camMode").setNumber(1)
                CameraMode.Vision -> mLimelight.getEntry("camMode").setNumber(0)
            }
            field = value
        }

    // STREAM Mode
    var streamMode: StreamMode = StreamMode.Standard
        set(value) {
            if (field == value) return
            when (value) {
                StreamMode.Standard -> mLimelight.getEntry("stream").setNumber(0)
                StreamMode.PipMain -> mLimelight.getEntry("stream").setNumber(1)
                StreamMode.PipSecondary -> mLimelight.getEntry("stream").setNumber(2)
            }
            field = value
        }

    // SNAPSHOT MODE
    var snapshotMode: SnapshotMode = SnapshotMode.Stop
        set(value) {
            if (field == value) return
            when (value) {
                SnapshotMode.Stop -> mLimelight.getEntry("snapshot").setNumber(0)
                SnapshotMode.Start -> mLimelight.getEntry("snapshot").setNumber(1)
            }
            field = value
        }

    // PIPELINE
    fun setPipeline(newPipeline: Pipeline) {
        pipeline = (newPipeline.id)
    }

    var pipeline: Int = 0
        set(id: Int) {
            if (field == id) return
            if (id < 0 || id > 9) {
                println("Pipeline id needs to be from 0 to 9, ignoring value: $id")
                return
            }
            mLimelight.getEntry("pipeline").setNumber(id)
            field = id
        }

    private val mLimelight = NetworkTableInstance.getDefault().getTable(networkTableName)

    enum class LightMode { On, Off, Blink }
    enum class CameraMode { Vision, Driver }
    enum class StreamMode { Standard, PipMain, PipSecondary }
    enum class SnapshotMode { Stop, Start }

    data class Pipeline(val id: Int)

    companion object {
        const val kAspectRatio = 6.0 / 15.0
    }
}
