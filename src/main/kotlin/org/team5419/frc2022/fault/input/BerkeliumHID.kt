package org.team5419.frc2022.fault.input

import edu.wpi.first.wpilibj.GenericHID
import org.team5419.frc2022.fault.util.BooleanSource
import org.team5419.frc2022.fault.util.DoubleSource

fun <T : GenericHID> T.mapControls(
    block: BerkeleiumHIDBuilder<T>.() -> Unit
) = BerkeleiumHIDBuilder(this).also(block).build()

class BerkeleiumHIDBuilder<T : GenericHID>(private val genericHID: T) {
    private val controlBuilders = mutableListOf<HIDControlBuilder>()
    private val stateControlBuilders = mutableMapOf<BooleanSource, BerkeleiumHIDBuilder<T>>()

    fun button(
        buttonId: Int,
        block: HIDButtonBuilder.() -> Unit = {}
    ) = button(HIDButtonSource(genericHID, buttonId), block = block)

    fun axisButton(
        axisId: Int,
        threshold: Double = HIDButton.kDefaultThreshold,
        block: HIDButtonBuilder.() -> Unit = {}
    ) = button(HIDAxisSource(genericHID, axisId), threshold, block = block)

    fun pov(angle: Int, block: HIDButtonBuilder.() -> Unit = {}) = pov(0, angle, block)
    fun pov(
        povId: Int,
        angle: Int,
        block: HIDButtonBuilder.() -> Unit = {}
    ) = button(HIDPOVSource(genericHID, povId, angle), block = block)

    fun button(
        source: HIDSource,
        threshold: Double = HIDButton.kDefaultThreshold,
        block: HIDButtonBuilder.() -> Unit = {}
    ): HIDButtonBuilder {
        val builder = HIDButtonBuilder(source, threshold)
        controlBuilders.add(builder)
        block(builder)
        return builder
    }

    fun state(state: BooleanSource, block: BerkeleiumHIDBuilder<T>.() -> Unit) =
            stateControlBuilders.compute(state) { _, _ -> BerkeleiumHIDBuilder(genericHID).also(block) }

    fun build(): BerkeleiumHID<T> {
        return BerkeleiumHID(
                genericHID,
                controlBuilders.map { it.build() },
                stateControlBuilders.mapValues { it.value.build() }
        )
    }
}

class BerkeleiumHID<T : GenericHID>(
    private val genericHID: T,
    private val controls: List<HIDControl>,
    private val stateControls: Map<BooleanSource, BerkeleiumHID<T>>
) {

    fun getRawAxis(axisId: Int): DoubleSource = HIDAxisSource(genericHID, axisId)
    fun getRawButton(buttonId: Int): BooleanSource = HIDButtonSource(
            genericHID,
            buttonId
    ).booleanSource

    fun update() {
        controls.forEach { it.update() }
        for ((state, controller) in stateControls) {
            if (state()) controller.update()
        }
    }
}
