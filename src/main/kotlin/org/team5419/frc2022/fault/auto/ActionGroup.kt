package org.team5419.frc2022.fault.auto

import org.team5419.frc2022.fault.math.units.Second
import org.team5419.frc2022.fault.math.units.SIUnit

abstract class ActionGroup internal constructor() : Action() {
    internal abstract val actions: List<Action>
}

open class ParallelAction(actions: List<Action>) : ActionGroup() {

    override val actions: List<Action> = actions

    protected val actionMap = mutableMapOf<Action, Boolean>()

    constructor(vararg actions: Action) : this(actions.toList())

    init {
        finishCondition += {
            var allDone = true
            actions.forEach { allDone = allDone && it.next() }
            allDone
        }

        actions.forEach { actionMap.put(it, false) }
    }

    override fun start() {
        super.start()
        actionMap.keys.forEach {
            actionMap.set(it, false)
            it.start()
        }
    }

    override fun update(dt: SIUnit<Second>) {
        val iterator = actionMap.iterator()

        while (iterator.hasNext()) {
            val (action, finished) = iterator.next()
            if (finished) continue
            if (!finished && action.next()) {
                action.finish()
                actionMap.set(action, true)
            } else {
                action.update(dt)
            }
        }
    }

    override fun finish() {
        actionMap.forEach {
            if (!it.value) it.key.finish()
        }
    }
}

open class SerialAction(actions: MutableList<Action>) : ActionGroup() {

    constructor(vararg actions: Action) : this(actions.toMutableList())

    private var index = 0
    private val isLastAction = { index == (actions.size - 1) }
    private val isLastActionDone = { actions.last().next() }

    override val actions: List<Action>

    init {
        finishCondition += { isLastAction() && isLastActionDone() }
        this.actions = actions
        // assert(this.actions.size < 1) { "No actions added to command group" }
        index = 0
    }

    override fun start() {
        super.start()
        index = 0
        actions[index].start()
    }

    override fun update(dt: SIUnit<Second>) {
        if (isLastAction() && isLastActionDone()) return

        if (actions[index].next() && !isLastAction()) {
            actions[index].finish()
            index++
            actions[index].start()
        }

        actions[index].update(dt)
    }

    override fun finish() {
        super.finish()
        actions.last().finish()
    }
}
