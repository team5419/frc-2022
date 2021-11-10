package org.team5419.frc2022
import edu.wpi.first.wpilibj.TimedRobot

public class Main {

    /**
     * Main initialization function. Do not perform any initialization here.
     *
     * <p>If you change your main robot class, change the parameter type.
     */
    companion object {
        @JvmStatic
        public fun main(args: Array<String>) {
            TimedRobot.startRobot { Robot }
        }
    }
}
