package org.team5419.frc2022.fault.math.localization

import edu.wpi.first.wpilibj.Timer
import org.team5419.frc2022.fault.math.units.SIUnit
import org.team5419.frc2022.fault.math.units.Second
import org.team5419.frc2022.fault.math.units.seconds
import org.team5419.frc2022.fault.util.Interpolable
import org.team5419.frc2022.fault.util.Source
import org.team5419.frc2022.fault.math.units.unitlessValue
import org.team5419.frc2022.fault.math.units.operations.times
import org.team5419.frc2022.fault.math.units.operations.div

import java.util.TreeMap

class TimeInterpolatableBuffer<T : Interpolable<T>>(
    private val historySpan: SIUnit<Second> = SIUnit(1.0),
    private val timeSource: Source<SIUnit<Second>> = { Timer.getFPGATimestamp().seconds }
) {

    private val bufferMap = TreeMap<SIUnit<Second>, T>()

    operator fun set(time: SIUnit<Second>, value: T): T? {
        cleanUp()
        return bufferMap.put(time, value)
    }

    private fun cleanUp() {
        val currentTime = timeSource()

        while (bufferMap.isNotEmpty()) {
            val entry = bufferMap.lastEntry()
            if (currentTime - entry.key >= historySpan) {
                bufferMap.remove(entry.key)
            } else {
                return
            }
        }
    }

    fun clear() {
        bufferMap.clear()
    }

    @Suppress("ReturnCount")
    operator fun get(time: SIUnit<Second>): T? {
        if (bufferMap.isEmpty()) return null

        bufferMap[time]?.let { return it }

        val topBound = bufferMap.ceilingEntry(time)
        val bottomBound = bufferMap.floorEntry(time)

        return when {
            topBound == null -> bottomBound.value
            bottomBound == null -> topBound.value
            else -> bottomBound.value.interpolate(
                    topBound.value,
                    ((time - bottomBound.key) / (topBound.key - bottomBound.key)).unitlessValue
            )
        }
    }
}
