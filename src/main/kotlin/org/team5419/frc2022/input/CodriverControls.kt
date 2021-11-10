package org.team5419.frc2022.input

@Suppress("TooManyFunctions")
public interface CodriverControls {
    // shooger functions

    public fun shoog() : Boolean

    public fun spinUp() : Boolean

    // intake functions

    public fun intake(): Boolean

    public fun outtake(): Boolean

    public fun storeIntake(): Boolean

    // storage functions

    public fun reverseStorage(): Boolean

    // hood functions

    public fun deployHoodAuto(): Boolean

    public fun deployHoodClose(): Boolean

    public fun deployHoodTruss() : Boolean

    public fun retractHood() : Boolean

    // climb

    public fun climb(): Boolean

    public fun unclimb(): Boolean

    public fun winch(): Boolean

    public fun unwinch(): Boolean
}
