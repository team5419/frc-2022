package org.team5419.frc2022.auto.actions

import org.team5419.frc2022.tab

import org.team5419.frc2022.subsystems.Shooger
import org.team5419.frc2022.subsystems.Hood
import org.team5419.frc2022.ShoogerConstants.ShoogTime
import org.team5419.frc2022.fault.math.units.Second
import org.team5419.frc2022.fault.math.units.SIUnit
import org.team5419.frc2022.fault.auto.Action

public class SpinUpAction(/*timeout: SIUnit<Second> = ShoogTime*/) : Action() {
    init {
       // withTimeout(timeout)
    }
    override fun start() {
        timer.stop()
        timer.reset()
        timer.start()
        Shooger.spinUp(Hood.mode)
    }

    override fun next() = true
}
