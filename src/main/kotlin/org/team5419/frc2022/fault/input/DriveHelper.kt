package org.team5419.frc2022.fault.input

import org.team5419.frc2022.fault.util.BooleanSource
import org.team5419.frc2022.fault.util.DoubleSource
import kotlin.math.absoluteValue

import kotlin.math.max

abstract class DriveHelper : DriveSignalSource {

    abstract fun output(): DriveSignal

    override fun invoke() = output()

    protected fun handleDeadband(value: Double, deadband: Double) = when {
        value.absoluteValue < deadband.absoluteValue -> 0.0
        else -> value
    }
}

class TankHelper(
    private val left: DoubleSource,
    private val right: DoubleSource,
    private val slow: BooleanSource = { false },
    private val deadband: Double = kDefaultDeaband,
    private val slowMultiplier: Double = kDefaultSlowMultiplier
) : DriveHelper() {

    override fun output(): DriveSignal {
        val currentSlow = if (slow()) slowMultiplier else 1.0
        var currentLeft = left()
        var currentRight = right()
        currentLeft = handleDeadband(currentLeft, deadband)
        currentRight = handleDeadband(currentRight, deadband)
        return DriveSignal(currentLeft * currentSlow, currentRight)
    }

    companion object {
        const val kDefaultDeaband = 0.02
        const val kDefaultSlowMultiplier = 0.4
    }
}

class SpaceDriveHelper(
    private val throttle: DoubleSource,
    private val wheel: DoubleSource,
    private val quickTurn: BooleanSource,
    private val slow: BooleanSource = { false },
    private val superSlow: BooleanSource = { false },
    private val deadband: Double = kDefaultDeadband,
    private val quickTurnMultiplier: Double = kDefaultQuickTurnMultiplier,
    private val slowMultiplier: Double = kDefaultSlowMultiplier,
    private val superSlowMultiplier: Double = kDefaultSuperSlowMultiplier
) : DriveHelper() {

    override fun output(): DriveSignal {
        var currentThrottle = throttle()
        var currentTurn = wheel() * if (quickTurn()) 1.0 else quickTurnMultiplier
        currentThrottle = handleDeadband(currentThrottle, deadband)
        currentTurn = handleDeadband(currentTurn, deadband)
        var currentSlow = 1.0
        if(slow()) {
            currentSlow = slowMultiplier
        }
        if(superSlow()) {
            currentSlow = superSlowMultiplier
        }
        val howFarOver = max(0.0, currentThrottle + currentTurn - 1)
        val left = currentSlow * (currentThrottle - currentTurn - howFarOver)
        val right = currentSlow * (currentThrottle + currentTurn - howFarOver)
        return DriveSignal(left, right)
        /*var currentThrottle = throttle()
        var currentTurn = wheel() * if (quickTurn()) 1.0 else quickTurnMultiplier
        currentThrottle = handleDeadband(currentThrottle, deadband)
        currentTurn = handleDeadband(currentTurn, deadband)
        val currentSlow = if (slow()) slowMultiplier else 1.0
        val left = currentSlow * (currentThrottle - currentTurn)
        val right = currentSlow * (currentThrottle + currentTurn)
        return DriveSignal(left, right)*/
    }

    companion object {
        const val kDefaultDeadband = 0.02
        const val kDefaultQuickTurnMultiplier = 0.4
        const val kDefaultSlowMultiplier = 0.4
        const val kDefaultSuperSlowMultiplier = 0.01
    }
}

class CheesyDriveHelper(
    private val throttle: DoubleSource,
    private val wheel: DoubleSource,
    private val quickTurn: BooleanSource
) : DriveHelper() {

    override fun output(): DriveSignal {
        return DriveSignal()
    }
}

class BerkeleyDriveHelper(
    private val throttle: DoubleSource,
    private val wheel: DoubleSource,
    private val quickTurn: BooleanSource,
    private val slow: BooleanSource = { false },
    private val deadband: Double = kDefaultDeadband,
    private val quickTurnMultiplier: Double = kDefaultQuickTurnMultiplier,
    private val slowMultiplier: Double = kDefaultSlowMultiplier
) : DriveHelper() {

    private fun normalizeDeadband(value: Double, deadband: Double): Double {
        return if (value.absoluteValue >= deadband) (1 + deadband) * value - deadband else 0.0
    }

    override fun output(): DriveSignal {
        var currentThrottle = throttle()
        var currentTurn = wheel() * if (quickTurn()) 1.0 else quickTurnMultiplier
        currentThrottle = normalizeDeadband(currentThrottle, deadband)
        currentTurn = normalizeDeadband(currentTurn, deadband)
        val currentSlow = if (slow()) slowMultiplier else 1.0
        val left = currentSlow * (currentThrottle - currentTurn)
        val right = currentSlow * (currentThrottle + currentTurn)
        return DriveSignal(left, right)
    }

    companion object {
        const val kDefaultDeadband = 0.02
        const val kDefaultQuickTurnMultiplier = 0.4
        const val kDefaultSlowMultiplier = 0.4
    }
}
