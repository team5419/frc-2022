package org.team5419.frc2022.fault.math.splines

import org.team5419.frc2022.fault.math.geometry.Vector2d
import org.team5419.frc2022.fault.math.units.Meter
import org.team5419.frc2022.fault.math.units.Mult
import org.team5419.frc2022.fault.math.units.SIUnit
import org.team5419.frc2022.fault.math.units.operations.times
import org.team5419.frc2022.fault.math.units.operations.div

data class FunctionalQuadraticSpline(
    private val p1: Vector2d,
    private val p2: Vector2d,
    private val p3: Vector2d
) {

    private val a: SIUnit<Mult<Meter, Meter>>
        get() = p3.x * (p2.y - p1.y) + p2.x * (p1.y - p3.y) + p1.x * (p3.y - p2.y)
    private val b: SIUnit<Mult<Mult<Meter, Meter>, Meter>>
        get() = p3.x * p3.x * (p1.y - p2.y) + p2.x * p2.x * (p3.y - p1.y) + p1.x * p1.x * (p2.y - p3.y)

    val vertexXCoordinate: SIUnit<Meter> get() = -b / (2.0 * a)
}
