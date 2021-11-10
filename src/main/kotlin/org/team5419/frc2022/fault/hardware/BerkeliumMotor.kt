
package org.team5419.frc2022.fault.hardware

import org.team5419.frc2022.fault.math.units.Meter
import org.team5419.frc2022.fault.math.units.SIKey
import org.team5419.frc2022.fault.math.units.SIUnit
import org.team5419.frc2022.fault.math.units.derived.Acceleration
import org.team5419.frc2022.fault.math.units.derived.Radian
import org.team5419.frc2022.fault.math.units.derived.Velocity
import org.team5419.frc2022.fault.math.units.derived.Volt

typealias LinearBerkeliumMotor = BerkeliumMotor<Meter>
typealias AngularBerkeliumMotor = BerkeliumMotor<Radian>

interface BerkeliumMotor<T : SIKey> {

    /**
     * The encoder attached to the motor
     */
    val encoder: BerkeliumEncoder<T>
    /**
     * The voltage output of the motor controller in volts
     */
    val voltageOutput: SIUnit<Volt>

    /**
     * Inverts the output given to the motor
     */
    var outputInverted: Boolean

    /**
     *  When enabled, motor leads are commonized electrically to reduce motion
     */
    var brakeMode: Boolean

    /**
     * Configures the max voltage output given to the motor
     */
    var voltageCompSaturation: SIUnit<Volt>

    /**
     *  Peak target velocity that the on board motion profile generator will use
     *  Unit is [T]/s
     */
    var motionProfileCruiseVelocity: SIUnit<Velocity<T>>
    /**
     *  Acceleration that the on board motion profile generator will
     *  Unit is [T]/s/s
     */
    var motionProfileAcceleration: SIUnit<Acceleration<T>>
    /**
     * Enables the use of on board motion profiling for position mode
     */
    var useMotionProfileForPosition: Boolean

    fun follow(motor: BerkeliumMotor<*>): Boolean

    /**
     * Sets the output [voltage] in volts and [arbitraryFeedForward] in volts
     */
    fun setVoltage(voltage: SIUnit<Volt>, arbitraryFeedForward: SIUnit<Volt> = SIUnit(0.0))

    /**
     * Sets the output [percent] in percent and [arbitraryFeedForward] in volts
     */
    fun setPercent(percent: Double, arbitraryFeedForward: SIUnit<Volt> = SIUnit(0.0))

    /**
     * Sets the output [velocity] in [T]/s and [arbitraryFeedForward] in volts
     */
    fun setVelocity(velocity: SIUnit<Velocity<T>>, arbitraryFeedForward: SIUnit<Volt> = SIUnit(0.0))

    /**
     * Sets the output [position] in [T] and [arbitraryFeedForward] in volts
     */
    fun setPosition(position: SIUnit<T>, arbitraryFeedForward: SIUnit<Volt> = SIUnit(0.0))

    /**
     * Sets the output of the motor to neutral
     */
    fun setNeutral()
}
