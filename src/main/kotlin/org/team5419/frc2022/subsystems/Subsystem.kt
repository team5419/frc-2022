package org.team5419.frc2022.subsystems

open class Subsystem {

    open var name: String
    open fun reset() {}

    constructor(name: String) {
        this.name = name
    }

}
