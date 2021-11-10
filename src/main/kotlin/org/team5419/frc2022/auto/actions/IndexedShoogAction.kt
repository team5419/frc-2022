package org.team5419.frc2022.auto.actions

import org.team5419.frc2022.tab
import org.team5419.frc2022.fault.auto.Action
import org.team5419.frc2022.StorageConstants
import org.team5419.frc2022.subsystems.Shooger
import org.team5419.frc2022.subsystems.ShotSetpoint
import org.team5419.frc2022.subsystems.Storage
import org.team5419.frc2022.subsystems.Storage.StorageMode
import org.team5419.frc2022.subsystems.Hood
import org.team5419.frc2022.fault.math.units.*

public class IndexedShoogAction(val balls: Int, val setpoint: ShotSetpoint = Hood.mode) : Action() {

    val sensorPos
        get() = Storage.sensorPosition

    var lastSensorPos = sensorPos
    var ballShot: Int = 0

    val ballShootSensorThreashold = StorageConstants.SensorThreshold

    init {
        finishCondition += { ballShot >= balls }

        withTimeout( (balls * 1.3).seconds )
    }

    override fun start() {
        super.start()

        ballShot = 0
    }

    override fun update(dt: SIUnit<Second>) {
        Shooger.shoog(setpoint)

        if ( sensorPos < ballShootSensorThreashold && lastSensorPos > ballShootSensorThreashold ) {
            ballShot++
            println("shot ball ${ballShot}")
        }

        lastSensorPos = sensorPos

        if ( Shooger.isHungry() && Storage.isLoadedBall ) {
            Storage.mode = StorageMode.PASSIVE
        } else if ( Storage.mode == StorageMode.LOAD && !Storage.isLoadedBall) {
            Storage.mode = StorageMode.PASSIVE
        } else {
            Storage.mode = StorageMode.PASSIVE
        }
    }

    override fun finish() {
        println("done")

        Shooger.stop()

        Storage.mode = StorageMode.OFF
    }
}
