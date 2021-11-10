package org.team5419.frc2022.fault.math.pid

import org.team5419.frc2022.fault.math.kEpsilon
import org.team5419.frc2022.fault.util.DoubleSource
import kotlin.math.absoluteValue

// adapted from:
// https://github.com/Team1323/2019DeepSpace/blob/13d5d4ac56c2da11d112ab9ab84241c8b6878018/src/main/java/com/team1323/lib/util/SynchronousPIDF.java
class PIDF(
    var kP: Double = 0.0,
    var kI: Double = 0.0,
    var kD: Double = 0.0,
    var kF: Double = 0.0,
    private val feedbackVariable: DoubleSource
) {

    constructor(
        kP: Double = 0.0,
        kI: Double = 0.0,
        kD: Double = 0.0,
        feedbackVariable: DoubleSource
    ): this(kP, kI, kD, 0.0, feedbackVariable)

    constructor(
        kP: Double = 0.0,
        kD: Double = 0.0,
        feedbackVariable: DoubleSource
    ): this(kP, 0.0, kD, 0.0, feedbackVariable)

    var setpoint = 0.0
        set(value) {
            if (value == field) return
            field = if (maximumInput > minimumInput) {
                if (value > maximumInput) maximumInput
                else if (value < minimumInput) minimumInput
                else value
            } else value
        }

    var maximumOutput = 1.0
        private set
    var minimumOutput = -1.0
        private set

    var maximumInput = 0.0
        private set
    var minimumInput = 0.0
        private set

    var deadband = 0.0

    var continuous = false
    var inverted = false

    var error = 0.0
        private set
    var output = 0.0
        private set

    private var lastError = 0.0
    private var lastInput = Double.NaN
    private var accumulator = 0.0

    @Suppress("ComplexMethod")
    fun calculate(dt: Double): Double {
        var newDt = dt
        if (newDt < kEpsilon) newDt = kEpsilon
        val input = feedbackVariable()
        lastInput = input
        error = setpoint - input
        if (continuous) {
            if (error.absoluteValue > (maximumInput - minimumInput) / 2.0) {
                if (error > 0) {
                    error = error - maximumInput + minimumInput
                } else {
                    error = error + maximumInput - minimumInput
                }
            }
        }
        if ((error * kP < maximumOutput) && (error * kP > minimumOutput)) {
            accumulator += error * newDt
        } else {
            accumulator = 0.0
        }

        val propError = if (error.absoluteValue < deadband) 0.0 else error

        output = kP * propError + kI * accumulator + kD * (error - lastError) / dt + kF * setpoint
        lastError = error

        if (output > maximumOutput) {
            output = maximumOutput
        } else if (output < minimumOutput) {
            output = minimumOutput
        }

        return output * if (inverted) -1.0 else 1.0
    }

    fun setPID(kP: Double, kI: Double, kD: Double) = setPID(kP, kI, kD, 0.0)
    fun setPID(kP: Double, kI: Double, kD: Double, kF: Double) {
        this.kP = kP
        this.kI = kI
        this.kD = kD
        this.kF = kF
    }

    fun setInputRange(min: Double, max: Double) {
        if (min > max) {
            println("ERROR: tried to set min input to greater than max input in PID")
            return
        }
        minimumInput = min
        maximumInput = max
    }

    fun setOutputRange(min: Double, max: Double) {
        if (min > max) {
            println("ERROR: tried to set min output to greater than max output in PID")
            return
        }
        minimumOutput = min
        maximumOutput = max
    }

    fun reset() {
        lastInput = Double.NaN
        lastError = 0.0
        output = 0.0
        setpoint = 0.0
        resetAccumulator()
    }

    fun resetAccumulator() {
        accumulator = 0.0
    }
}
