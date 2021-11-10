package org.team5419.frc2022.fault.math.geometry

import org.team5419.frc2022.fault.math.epsilonEquals
import org.team5419.frc2022.fault.math.units.Meter
import org.team5419.frc2022.fault.math.units.SIUnit
import org.team5419.frc2022.fault.math.units.meters
import org.team5419.frc2022.fault.math.units.operations.div
import org.team5419.frc2022.fault.math.units.operations.times
import org.team5419.frc2022.fault.math.units.operations.div

import kotlin.math.max
import kotlin.math.min
import org.team5419.frc2022.fault.math.units.operations.div

@Suppress("FunctionName")
fun Rectangle2d(
    one: Vector2d,
    two: Vector2d
): Rectangle2d {
    val minX = min(one.x.value, two.x.value)
    val minY = min(one.y.value, two.y.value)
    val maxX = max(one.x.value, two.x.value)
    val maxY = max(one.y.value, two.y.value)
    return Rectangle2d(
            minX.meters, minY.meters,
            (maxX - minX).meters, (maxY - minY).meters
    )
}

@Suppress("FunctionName", "UnsafeCallOnNullableType")
fun Rectangle2d(
    vararg pointsToInclude: Vector2d
): Rectangle2d {
    val minX = pointsToInclude.minBy { it.x }!!.x
    val minY = pointsToInclude.minBy { it.y }!!.y
    val maxX = pointsToInclude.maxBy { it.x }!!.x
    val maxY = pointsToInclude.maxBy { it.y }!!.y
    return Rectangle2d(
            minX, minY,
            maxX - minX, maxY - minY
    )
}

data class Rectangle2d constructor(
    val x: SIUnit<Meter>,
    val y: SIUnit<Meter>,
    val w: SIUnit<Meter>,
    val h: SIUnit<Meter>
) {

    val topLeft = Vector2(x, y + h)
    val topRight = Vector2(x + w, y + h)
    val bottomLeft = Vector2(x, y)
    val bottomRight = Vector2(x + w, y)

    val center = Vector2(x + w / 2.0, y + h / 2.0)

    val maxCorner = topRight
    val minCorner = bottomLeft

    fun isIn(r: Rectangle2d) =
            x < r.x + r.w && x + w > r.x && y < r.y + r.h && y + h > r.y

    fun isWithin(r: Rectangle2d) = r.x in x..(x + w - r.w) && r.y in y..(y + h - r.h)

    operator fun contains(p: Vector2d) = p.x in x..(x + w) && p.y in y..(y + h)

    @Suppress("ComplexMethod", "ReturnCount")
    fun doesCollide(rectangle: Rectangle2d, translation: Vector2d): Boolean {
        if (translation.x.value epsilonEquals 0.0 && translation.y.value epsilonEquals 0.0) return false
        // Check if its even in range
        val boxRect = Rectangle2d(
                rectangle.topLeft, rectangle.bottomRight,
                rectangle.topLeft + translation, rectangle.bottomRight + translation
        )
        // println(boxRect)
        if (!boxRect.isIn(this)) return false
        // AABB collision
        // Calculate distances
        val xInvEntry: SIUnit<Meter>
        val xInvExit: SIUnit<Meter>
        val yInvEntry: SIUnit<Meter>
        val yInvExit: SIUnit<Meter>
        if (translation.x.value > 0.0) {
            xInvEntry = (x - (rectangle.x + rectangle.w))
            xInvExit = ((x + w) - rectangle.x)
        } else {
            xInvEntry = ((x + w) - rectangle.x)
            xInvExit = (x - (rectangle.x + rectangle.w))
        }
        if (translation.y.value > 0.0) {
            yInvEntry = (y - (rectangle.y + rectangle.h))
            yInvExit = ((y + h) - rectangle.y)
        } else {
            yInvEntry = ((y + h) - rectangle.y)
            yInvExit = (y - (rectangle.y + rectangle.h))
        }
        // Find time of collisions
        val xEntry: Double
        val xExit: Double
        val yEntry: Double
        val yExit: Double
        if (translation.x.value epsilonEquals 0.0) {
            xEntry = Double.NEGATIVE_INFINITY
            xExit = Double.POSITIVE_INFINITY
        } else {
            xEntry = (xInvEntry / translation.x).value
            xExit = (xInvExit / translation.x).value
        }
        if (translation.y.value epsilonEquals 0.0) {
            yEntry = Double.NEGATIVE_INFINITY
            yExit = Double.POSITIVE_INFINITY
        } else {
            yEntry = (yInvEntry / translation.y).value
            yExit = (yInvExit / translation.y).value
        }
        val entryTime = max(xEntry, yEntry)
        val exitTime = min(xExit, yExit)

        return entryTime <= exitTime && (xEntry >= 0.0 || yEntry >= 0.0) && (xEntry < 1.0 || yEntry < 1.0)
    }
}
