package org.team5419.frc2022.auto.actions

import org.team5419.frc2022.subsystems.Intake.IntakeMode
import org.team5419.frc2022.subsystems.Intake
import org.team5419.frc2022.fault.auto.Action

public class IntakeAction(): ConfigIntakeAction(IntakeMode.INTAKE)
public class OuttakeAction(): ConfigIntakeAction(IntakeMode.OUTTAKE)
public class DisableIntakeAction(): ConfigIntakeAction(IntakeMode.OFF)

open class ConfigIntakeAction(val intake: IntakeMode): Action() {
    override fun start() {
        println("intake action")
        Intake.intakeMode = intake
    }

    override fun next() = true

}
