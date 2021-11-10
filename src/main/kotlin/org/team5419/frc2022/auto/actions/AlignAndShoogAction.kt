package org.team5419.frc2022.auto.actions

import org.team5419.frc2022.subsystems.Shooger
import org.team5419.frc2022.ShoogerConstants
import org.team5419.frc2022.fault.auto.SerialAction

class AlignAndShoogAction : SerialAction(
    AutoAlignAction(),
    TimedShoogAction()
) {}
