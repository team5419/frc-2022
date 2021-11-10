package org.team5419.frc2022.fault.util.time

import edu.wpi.first.wpilibj.Timer
import org.team5419.frc2022.fault.math.units.seconds

public class WPITimer(timer: Timer = Timer()) : ITimer {

    private val mTimer: Timer

    init {
        mTimer = timer
    }

    public override fun get() = mTimer.get().seconds

    public override fun start() = mTimer.start()

    public override fun stop() = mTimer.stop()

    public override fun reset() = mTimer.reset()
}
