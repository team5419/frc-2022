package org.team5419.frc2022.auto.actions

import org.team5419.frc2022.subsystems.Vision
import org.team5419.frc2022.subsystems.Drivetrain
import org.team5419.frc2022.subsystems.Shooger
import org.team5419.frc2022.subsystems.Hood
import org.team5419.frc2022.subsystems.Storage
import org.team5419.frc2022.subsystems.Storage.StorageMode
import org.team5419.frc2022.ShoogerConstants.ShoogTime
import org.team5419.frc2022.fault.math.units.Second
import org.team5419.frc2022.fault.math.units.SIUnit
import org.team5419.frc2022.fault.auto.Action

import org.team5419.frc2022.fault.math.units.seconds

import org.team5419.frc2022.StorageConstants

public class AlignShootAction(timeout: SIUnit<Second> = ShoogTime) : Action() {
    init {
        this.withTimeout(timeout)
    }

    override public fun start() {
        timer.stop()
        timer.reset()
        timer.start()
        Vision.on()
        Drivetrain.brakeMode = true
    }

    override public fun update(dt: SIUnit<Second>) {
        Drivetrain.setPercent(Vision.autoAlign())
        Shooger.shoog(Hood.mode)

        if ( Shooger.isHungry() && Storage.isLoadedBall && Vision.aligned()) {
            //println("load (is aligned)")
            Storage.mode = StorageMode.LOAD
        } else if ( Storage.mode == StorageMode.LOAD && !Storage.isLoadedBall) {
            //println("done shooting")
            Storage.mode = StorageMode.PASSIVE
        } else {
            //println("waiting for ir sensor")
            if(timer.get().value.seconds.rem(StorageConstants.LoopTime) < StorageConstants.OffTime) {
                Storage.mode = StorageMode.REVERSE // off cycle
            } else {
                Storage.mode = StorageMode.PASSIVE
            }
        }
    }

    override public fun finish() {
        println("done shooting")
        Shooger.stop()
        Vision.off()
        // put the drive train back in coast mode
        Drivetrain.brakeMode = false

        // make sure the drive train has stoped moving
        Drivetrain.setPercent(0.0, 0.0)
    }
}
