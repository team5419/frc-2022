package org.team5419.frc2022.auto

import edu.wpi.first.wpilibj.Timer

typealias NothingAction = Action

open class Action {
    open var timer: Timer = Timer()
    protected var finishConditions: List<() -> Boolean> = MutableList<() -> Boolean>()

    open fun addCondition(condition: () -> Boolean) {
        this.finishConditions.add(condition)
    }

    open fun start() {
        timer.stop()
        timer.reset()
        timer.start()
    }

    open fun update(dt: Double) {}

    open fun next(): Boolean {
        for(condition in this.finishConditions) {
            if(!condition()) {
                return false
            }
        }
        return true
    }

    open fun finish() {
    }

    fun withTimeout(time: Double) {
        this.addCondition({ this.timer.get() >= time });
    }
}
