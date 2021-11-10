package org.team5419.frc2022.fault.util.time

import org.team5419.frc2022.fault.math.units.SIUnit
import org.team5419.frc2022.fault.math.units.Second

class DeltaTime internal constructor(startTime: SIUnit<Second> = SIUnit(0.0)) {

    @Suppress("VariableNaming")
    internal var _currentTime = startTime
        private set
    @Suppress("VariableNaming")
    internal var _deltaTime = SIUnit<Second>(0.0)
        private set

    val deltaTime get() = _deltaTime
    val currentTime get() = _currentTime

    fun updateTime(newTime: SIUnit<Second>): SIUnit<Second> {
        _deltaTime = if (_currentTime < 0.0) {
            SIUnit<Second>(0.0)
        } else {
            newTime - _currentTime
        }
        _currentTime = newTime
        return _deltaTime
    }

    fun reset() {
        _currentTime = SIUnit<Second>(-1.0)
    }
}
