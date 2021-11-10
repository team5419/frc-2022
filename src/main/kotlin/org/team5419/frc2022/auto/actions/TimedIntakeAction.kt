package org.team5419.frc2022.auto.actions

import org.team5419.frc2022.tab

import org.team5419.frc2022.subsystems.Intake
import org.team5419.frc2022.fault.math.units.Second
import org.team5419.frc2022.fault.math.units.SIUnit
import org.team5419.frc2022.fault.auto.Action

public class TimedIntakeAction(timeout: SIUnit<Second>) : Action() {

    init {
        withTimeout(timeout)
    }

    override fun update(dt: SIUnit<Second>) = Intake.intake()
    override fun finish() = Intake.stopIntake()
}
