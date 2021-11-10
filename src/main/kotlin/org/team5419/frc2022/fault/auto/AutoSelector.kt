package org.team5419.frc2022.fault.auto

import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser

abstract class AutoSelector(tab: ShuffleboardTab, routines: Array<Routine>, name: String = "Auto Selector") {

    constructor(tab: ShuffleboardTab, name: String = "Auto Selector"): this(tab, arrayOf<Routine>(), name)

    protected val selector: SendableChooser<Routine>

    init {
        selector = SendableChooser<Routine>()
        setRoutines(routines)
        tab.add(name, selector)
    }

    public fun getSelectedRoutine(): Routine = selector.getSelected()

    public fun setRoutines(routines: Array<Routine>) {
        routines.forEach({ routine -> selector.addOption(routine.name, routine) })
    }
}
