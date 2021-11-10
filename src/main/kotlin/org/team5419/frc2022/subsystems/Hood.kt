package org.team5419.frc2022.subsystems

import org.team5419.frc2022.tab
import org.team5419.frc2022.HoodConstants
import org.team5419.frc2022.fault.subsystems.Subsystem
import org.team5419.frc2022.fault.math.units.native.NativeUnitRotationModel
import org.team5419.frc2022.fault.math.units.native.*
import org.team5419.frc2022.fault.math.units.derived.*
import org.team5419.frc2022.fault.math.units.*
import org.team5419.frc2022.fault.hardware.ctre.BerkeliumSRX
import kotlin.math.PI
import edu.wpi.first.wpilibj.shuffleboard.*
import edu.wpi.first.wpilibj.Timer
import edu.wpi.first.networktables.EntryListenerFlags
import edu.wpi.first.networktables.EntryNotification
import com.ctre.phoenix.motorcontrol.can.TalonSRX
import com.ctre.phoenix.motorcontrol.NeutralMode
import com.ctre.phoenix.motorcontrol.FeedbackDevice
import com.ctre.phoenix.motorcontrol.ControlMode

import org.team5419.frc2022.subsystems.Intake.intakeMotor

object Hood : Subsystem("Hood") {
    // motor

    private val hoodMotor = TalonSRX(HoodConstants.HoodPort)
        .apply {
            // reset
            configFactoryDefault(100)

            // config PID constants
            config_kP(0, HoodConstants.PID.P, 100)
            config_kI(0, HoodConstants.PID.I, 100)
            config_kD(0, HoodConstants.PID.D, 100)

            // make sure it dosent go to fast


            // limit the current to not brown out
            configPeakCurrentLimit(20, 100)

            // config the sensor and direction
            configSelectedFeedbackSensor(FeedbackDevice.Analog, 0, 100)
            setSensorPhase(false)
            setInverted(true)

            setNeutralMode(NeutralMode.Brake)

            // config the soft limits
            configForwardSoftLimitThreshold( angleToNativeUnits( HoodConstants.MaxAngle ).toInt(), 100)
            configForwardSoftLimitEnable(true, 100)
            configReverseSoftLimitThreshold( angleToNativeUnits( 0.0 ).toInt(), 100)
            configReverseSoftLimitEnable(true, 100)

            configClosedLoopPeakOutput(0, HoodConstants.MaxSpeed, 100)

            // reset the sensor
            setSelectedSensorPosition(0, 0, 100)
        }

    private var previousReading: Double = 0.0
    // hood positions

    public enum class HoodPosititions(override val angle: Double, override val velocity: Double) : ShotSetpoint {
        FAR(HoodConstants.Far.angle/*+HoodConstants.Far.adjustment*/, HoodConstants.Far.velocity),
        TRUSS(HoodConstants.Truss.angle/*+HoodConstants.Far.adjustment*/, HoodConstants.Truss.velocity),
        CLOSE(HoodConstants.Close.angle/*+HoodConstants.Close.adjustment*/, HoodConstants.Close.velocity),
        AUTO(HoodConstants.Auto.angle, HoodConstants.Auto.velocity),
        RETRACT(0.0, 0.0), // no need to edit this one
        RESET(-1.0, -1.0) // used if the robot has reset so that you can move hood (especially in auto)
    }

    var mode: ShotSetpoint = HoodPosititions.RESET
        set(value: ShotSetpoint) {
            if (value == field) return
            if (field.angle == value.angle && field.velocity == value.velocity) return

            field = value

            if(value.angle >= 0.0 && value.velocity >= 0.0) { // dont move if you're just resetting
                goto(value.angle)
            }
        }

    init {
        // tab.add("Set Shooger Velocity", 0.0)
        //     .withWidget(BuiltInWidgets.kNumberSlider)
        //     .getEntry()
        //     .addListener({
        //         value: EntryNotification -> goto(value.value.getDouble())
        //     }, EntryListenerFlags.kUpdate)
        tab.addNumber("hood angle", {hoodAngle()})
        tab.addNumber("hood ticks", {hoodMotor.getSelectedSensorPosition(0).toDouble()})
        tab.addNumber("hood error", {hoodMotor.getClosedLoopError(0).toDouble()})
    }

    // public api

    val hoodMinPos = 0.0
    val hoodMaxPos = 714.0
    val hoodDeltaPos = hoodMaxPos - hoodMinPos

    fun angleToNativeUnits(angle: Double) =
        angle / HoodConstants.MaxAngle * hoodDeltaPos + hoodMinPos

    fun hoodAngle() = // from ticks to angle!!
        (hoodMotor.getSelectedSensorPosition(0) - hoodMinPos) / hoodDeltaPos * HoodConstants.MaxAngle

    fun goto(angle: ShotSetpoint) {
        mode = angle
    }

    fun goto(angle: Double) {
        println("hood goto ${angle}")
        if (angle < 0.0 || angle > HoodConstants.MaxAngle) {
            println("angle out of range")

            return goto(angle.coerceIn(0.0, HoodConstants.MaxAngle))
        }

        val ticks = angleToNativeUnits(angle)
        println("ticks: ${ticks}")

        hoodMotor.set(ControlMode.Position, ticks)
    }

    // subsystem functions

    fun checkAndReset() {
        val gotten: Double = hoodMotor.getSelectedSensorPosition(0).toDouble()
        if(gotten < 0.0 || gotten > hoodMaxPos) {
            println("Out of range. Ticks: " + gotten + ". Resetting to " + previousReading.toInt())
            hoodMotor.setSelectedSensorPosition(previousReading.toInt() /* because our pot only reads ints */, 0, 0)
            return
        }
        previousReading = gotten
        return
    }

    fun reset() {
        println("hood reset")
        hoodMotor.set(ControlMode.PercentOutput, 0.0)
        mode = HoodPosititions.RESET
    }

    override fun autoReset() = reset()
    override fun teleopReset() = reset()
}
