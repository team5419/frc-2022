package org.team5419.frc2022.auto.actions

import org.team5419.frc2022.tab

import org.team5419.frc2022.subsystems.Storage
import org.team5419.frc2022.subsystems.Storage.StorageMode
import org.team5419.frc2022.fault.math.units.seconds
import org.team5419.frc2022.fault.math.units.Second
import org.team5419.frc2022.fault.math.units.SIUnit
import org.team5419.frc2022.fault.auto.Action

public class UnjamAction(timeout: SIUnit<Second> = 0.1.seconds) : Action() {
    init {
        withTimeout(timeout)
    }

    override fun start() = Storage.reverse()
    override fun finish() = Storage.resetReverse()
}
