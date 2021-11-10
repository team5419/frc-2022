package org.team5419.frc2022.fault.hardware

import org.team5419.frc2022.fault.math.units.SIKey

abstract class AbstractBerkeliumMotor<T : SIKey> : BerkeliumMotor<T> {

    override var useMotionProfileForPosition = false

    override fun follow(motor: BerkeliumMotor<*>): Boolean {
        println("Cross brand following not implemented yet!")
        return false
    }
}
