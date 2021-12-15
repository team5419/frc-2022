package org.team5419.frc2022.controllers

import kotlin.math.*

import org.team5419.frc2022.input.*
import org.team5419.frc2022.subsystems.*
import org.team5419.frc2022.auto.*
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser
import edu.wpi.first.wpilibj.Timer

object AutoController : Controller() {
    private val autoSelector = SendableChooser<Routine>()
    private val timer: Timer = Timer()
    private val baseline: Routine = Routine("Baseline", arrayOf<Action>())
    private val routine: Routine = baseline
    private var prevTime: Double = 0.0
    private var time: Double = 0.0

    init {
        tab.add("Auto Selector", autoSelector)
        autoSelector.setDefaultOption("Baseline", baseline)

        routines.iterator().forEach({
            autoSelector.addOption(it.name, it)
        })
    }

    override fun start() {
        routine = autoSelector.getSelected() ?: baseline

        println("starting routine ${routine.name}")
        Drivetrain.brakeMode = true

        routine.start()

        timer.reset()
        timer.start()
    }

    override fun update() {
        time = timer.get()
        routine.update(time - prevTime)

        if (routine.isDone()) {
            routine.finish()
            println("done with action ${routine.name}")

            // test the routine so that we dont do anything
            routine = Routine("Baseline", Pose2d(), NothingAction())
        }

        prevTime = time
    }

    override fun reset() {

    }
}
