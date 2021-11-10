package org.team5419.frc2022.auto.actions

import org.team5419.frc2022.tab

import org.team5419.frc2022.subsystems.Intake
import org.team5419.frc2022.fault.math.units.seconds
import org.team5419.frc2022.fault.math.units.Second
import org.team5419.frc2022.fault.math.units.SIUnit
import org.team5419.frc2022.fault.auto.Action

public class ReleaseIntakeAction(timeout: SIUnit<Second> = 3.seconds) : Action() {
    init {
        withTimeout(timeout)
    }

}
