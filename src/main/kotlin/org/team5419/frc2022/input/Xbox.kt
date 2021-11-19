package org.team5419.frc2022.input
import edu.wpi.first.wpilibj.XboxController

open class Xbox {
    open var controller: XboxController

    constructor(_port: Int) {
        this.controller = XboxController(_port)
    }
}
