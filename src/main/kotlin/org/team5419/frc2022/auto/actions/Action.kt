package org.team5419.frc2022.auto

import org.team5419.frc2022.fault.math.units.SIUnit
import org.team5419.frc2022.fault.math.units.Second
import org.team5419.frc2022.fault.math.units.seconds
import org.team5419.frc2022.fault.util.BooleanSource
import org.team5419.frc2022.fault.util.Source
import org.team5419.frc2022.fault.util.or
import org.team5419.frc2022.fault.util.time.ITimer
import org.team5419.frc2022.fault.util.time.WPITimer

import edu.wpi.first.wpilibj.Timer

typealias NothingAction = Action

open class Condition {
    open fun check() {}
}

open class Action {
    open val timer: Timer = Timer()
    protected var finishConditions: Array<Condition>

    protected var mTimeout = 0.0 // seconds

    open fun addCondition(condition) {
        this.finishConditions.add(Condition() {
            override open fun check() {
                return condition()
            }
        })
    }

    open fun start() {
        timer.stop()
        timer.reset()
        timer.start()
    }

    open fun update(dt: Double) {}

    //protected fun timedOut() = (timer.get() >= mTimeout)

    open fun next(): Boolean {
        for(condition in this.finishConditions) {
            if(!condition.check()) {
                return false
            }
        }
        return true
    }

    open fun finish() {}

    protected class FinishCondition(
        private var source: BooleanSource
    ) : BooleanSource {
        override fun invoke() = source()

        operator fun plusAssign(other: BooleanSource) {
            source = source or other
        }

        fun set(other: BooleanSource) {
            source = other
        }
    }

    fun getTime(): SIUnit<Second> {
        return timer.get().value.seconds
    }

    fun withExit(condition: BooleanSource) = also { finishCondition += condition }

    fun overrideExit(condition: BooleanSource) = also { finishCondition.set(condition) }

    fun withTimeout(time: SIUnit<Second>) = also {
        this.mTimeout = time
        finishCondition += { timedOut() }
    }
}

open class ConditionalAction(
    private val condition: BooleanSource,
    private val ifTrue: Action? = null,
    private val ifFalse: Action? = null
) : Action() {

    private var selectedAction: Action? = null
    private val selectedActionDone = { if (selectedAction != null) selectedAction!!.next() else true }

    init {
        finishCondition += { timedOut() }
        finishCondition += selectedActionDone
    }

    override fun start() {
        super.start()
        selectedAction = if (condition()) ifTrue else ifFalse
        if (selectedAction != null) selectedAction!!.start()
    }

    override fun update(dt: SIUnit<Second>) {
        if (selectedAction != null) selectedAction!!.update(dt)
    }

    override fun finish() {
        if (selectedAction != null) selectedAction!!.finish()
        selectedAction = null
    }
}
