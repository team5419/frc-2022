package org.team5419.frc2022.fault.subsystems.drivetrain

import org.team5419.frc2022.fault.util.CSVWritable

data class CharacterizationData(
    val voltage: Double, // volts
    val velocity: Double, // rads / s
    val dt: Double // s
) : CSVWritable {

    override fun toCSV() = "$voltage,$velocity,$dt"
}
