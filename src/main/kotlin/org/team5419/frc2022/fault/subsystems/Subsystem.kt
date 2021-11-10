package org.team5419.frc2022.fault.subsystems

import org.team5419.frc2022.fault.util.loops.ILooper
import java.util.concurrent.atomic.AtomicLong

object SubsystemManager {

    private val subsystems = mutableListOf<Subsystem>()

    fun addSubsystem(subsystem: Subsystem) {
        print("Adding ${subsystem.name} to subsystem manager.")
        subsystems.add(subsystem)
    }

    fun periodic() = subsystems.forEach { it.periodic() }

    fun init() = subsystems.forEach { it.init() }

    fun autoReset() = subsystems.forEach { it.autoReset() }

    fun teleopReset() = subsystems.forEach { it.teleopReset() }

    fun zeroOutputs() = subsystems.forEach { it.zeroOutputs() }
}

abstract class Subsystem(name: String? = null) {

    companion object {
        private val subsystemId = AtomicLong()
    }

    val name = name ?: "Subsystem ${subsystemId.incrementAndGet()}"

    open fun periodic() {}

    open fun init() {}

    open fun autoReset() {}

    open fun teleopReset() {}

    open fun zeroOutputs() {}

    open fun registerLoops(looper: ILooper) {}
}
