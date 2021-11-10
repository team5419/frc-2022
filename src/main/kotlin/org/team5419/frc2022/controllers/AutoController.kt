package org.team5419.frc2022.controllers

import org.team5419.frc2022.auto.routines
import org.team5419.frc2022.auto.*
import org.team5419.frc2022.fault.auto.SerialAction
import org.team5419.frc2022.subsystems.*
import org.team5419.frc2022.DriveConstants
import org.team5419.frc2022.auto.actions.*
import org.team5419.frc2022.tab
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser
import edu.wpi.first.networktables.NetworkTableEntry
import edu.wpi.first.networktables.EntryListenerFlags
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets
import org.team5419.frc2022.fault.Controller
import org.team5419.frc2022.fault.auto.Action
import org.team5419.frc2022.fault.auto.NothingAction
import org.team5419.frc2022.fault.auto.Routine
import org.team5419.frc2022.fault.math.geometry.Pose2d
import org.team5419.frc2022.fault.math.units.*
import org.team5419.frc2022.fault.math.units.derived.*
import org.team5419.frc2022.fault.math.geometry.Vector2
import org.team5419.frc2022.fault.util.time.WPITimer

public class AutoController(val baseline: Routine = Routine("Baseline", Pose2d(), NothingAction())) : Controller {
    private val autoSelector = SendableChooser<Routine>()
    private val timer = WPITimer()

    private var routine: Routine = baseline
    private var prevTime = timer.get()
    private var time: SIUnit<Second> = 0.0.seconds

    init {
        tab.add("Auto Selector", autoSelector)
        autoSelector.setDefaultOption("Baseline", baseline)

        routines.iterator().forEach({
            autoSelector.addOption(it.name, it)
        })
    }


    override fun start() {

        routine = autoSelector.getSelected() ?: baseline

        println("starting rotine ${routine.name}")
        Drivetrain.brakeMode = true

        routine.start()

        timer.stop()
        timer.reset()
        timer.start()
    }

    override fun update() {
        time = timer.get()
        routine.update(time - prevTime)

        if (routine.next()) {
            routine.finish()
            println("done with action ${routine.name}")

            // test the routine so that we dont do anything
            routine = Routine("Baseline", Pose2d(), NothingAction())
        }

        prevTime = time

    }


    override fun reset() {
        Drivetrain.brakeMode = false
    }
}
