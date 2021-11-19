package org.team5419.frc2022.input
import edu.wpi.first.wpilibj.GenericHID.Hand
import org.team5419.frc2022.DriveConstants

object DriverControls: Xbox(DriveConstants.driverPort) {
    public fun getThrottle(): Double {
        return this.controller.getY( Hand.kLeft ).toDouble()
    }

    public fun getTurn(): Double {
        return this.controller.getX( Hand.kRight ).toDouble()
    }

    public fun slowMove(): Boolean {
        return this.controller.getBumper( Hand.kLeft )
    }
}
