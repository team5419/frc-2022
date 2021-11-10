package org.team5419.frc2022.fault.auto

enum class GroupType { Parallel, Serial }

interface IActionGroupBuilder {

    fun build(): ActionGroup
}

class ActionGroupBuilder(private val type: GroupType) : IActionGroupBuilder {
    private val actions = mutableListOf<Action>()

    operator fun Action.unaryPlus() = actions.add(this)

    override fun build() = when (type) {
        GroupType.Serial -> SerialAction(actions)
        GroupType.Parallel -> ParallelAction(actions)
    }
}

fun serial(block: ActionGroupBuilder.() -> Unit) = commandGroup(GroupType.Serial, block)

fun parallel(block: ActionGroupBuilder.() -> Unit) = commandGroup(GroupType.Parallel, block)

private fun commandGroup(type: GroupType, block: ActionGroupBuilder.() -> Unit) =
        ActionGroupBuilder(type).apply(block).build()
