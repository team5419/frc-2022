package org.team5419.frc2022.fault.input

import org.team5419.frc2022.fault.auto.Action
import kotlin.math.absoluteValue

typealias HIDControlListener = () -> Unit

interface HIDControl {
    fun update()
}
class HIDButton(
    private val source: HIDSource,
    private val threshold: Double,
    private val whileOn: List<HIDControlListener>,
    private val whileOff: List<HIDControlListener>,
    private val changeToOn: List<HIDControlListener>,
    private val changeToOff: List<HIDControlListener>
) : HIDControl {

    private var lastValue = source().absoluteValue >= threshold

    override fun update() {
        val currentValue = source().absoluteValue >= threshold
        when {
            // Value has changed
            lastValue != currentValue -> when {
                currentValue -> changeToOn
                else -> changeToOff
            }
            // Value stayed the same
            else -> when {
                currentValue -> whileOn
                else -> whileOff
            }
        }.forEach { it() }
        lastValue = currentValue
    }

    companion object {
        const val kDefaultThreshold = 0.5
    }
}

abstract class HIDControlBuilder(val source: HIDSource) {
    abstract fun build(): HIDControl
}
class HIDButtonBuilder(source: HIDSource, private val threshold: Double) : HIDControlBuilder(source) {

    private val whileOff = mutableListOf<HIDControlListener>()
    private val whileOn = mutableListOf<HIDControlListener>()
    private val changeToOn = mutableListOf<HIDControlListener>()
    private val changeToOff = mutableListOf<HIDControlListener>()

    fun changeToOn(action: Action) = changeToOn { action.start() }
    fun changeToOff(action: Action) = changeToOff { action.start() }

    fun whileOff(block: HIDControlListener) = also { whileOff.add(block) }
    fun whileOn(block: HIDControlListener) = also { whileOn.add(block) }
    fun changeToOn(block: HIDControlListener) = also { changeToOn.add(block) }
    fun changeToOff(block: HIDControlListener) = also { changeToOff.add(block) }

    override fun build() = HIDButton(source, threshold, whileOn, whileOff, changeToOn, changeToOff)
}
