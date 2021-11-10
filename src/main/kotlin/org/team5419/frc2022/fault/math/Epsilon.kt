package org.team5419.frc2022.fault.math

import kotlin.math.absoluteValue

@Suppress("TopLevelPropertyNaming")
const val kEpsilon = 1E-9

infix fun Double.epsilonEquals(other: Double) = minus(other).absoluteValue < kEpsilon
