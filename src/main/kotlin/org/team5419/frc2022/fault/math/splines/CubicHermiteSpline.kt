package org.team5419.frc2022.fault.math.splines

import org.team5419.frc2022.fault.math.geometry.Vector2d
import org.team5419.frc2022.fault.math.geometry.Rotation2d
import org.team5419.frc2022.fault.math.geometry.Pose2d
import org.team5419.frc2022.fault.math.units.meters

@SuppressWarnings("MagicNumber")
class CubicHermiteSpline(p0: Vector2d, h0: Rotation2d, p1: Vector2d, h1: Rotation2d) : Spline() {

    private val ax: Double
    private val bx: Double
    private val cx: Double
    private val dx: Double

    private val ay: Double
    private val by: Double
    private val cy: Double
    private val dy: Double

    init {
        val scale = 2.0 * p0.distance(p1)

        val x0 = p0.x.value
        val x1 = p1.x.value
        val dx0 = h0.cos * scale
        val dx1 = h1.cos * scale

        val y0 = p0.y.value
        val y1 = p1.y.value
        val dy0 = h0.sin * scale
        val dy1 = h1.sin * scale

        ax = dx0 + dx1 + 2.0 * x0 - 2.0 * x1
        bx = -2.0 * dx0 - dx1 - 3.0 * x0 + 3.0 * x1
        cx = dx0
        dx = x0

        ay = dy0 + dy1 + 2.0 * y0 - 2.0 * y1
        by = -2.0 * dy0 - dy1 - 3.0 * y0 + 3.0
        cy = dy0
        dy = y0
    }

    constructor(p0: Pose2d, p1: Pose2d): this(p0.translation, p0.rotation, p1.translation, p1.rotation)
    constructor(): this(Pose2d(), Pose2d())

    override fun getPoint(t: Double): Vector2d {
        val x = t * t * t * ax + t * t * bx + t * cx + dx
        val y = t * t * t * ay + t * t * by + t * cy + dy
        return Vector2d(x.meters, y.meters)
    }

    override fun getHeading(t: Double): Rotation2d {
        val dx = 3.0 * t * t * ax + 2.0 * t * bx + cx
        val dy = 3.0 * t * t * ay + 2.0 * t * by + cx
        return Rotation2d(dy, dx, true)
    }

    override fun getCurvature(t: Double): Double {
        val dx = 3.0 * t * t * ax + 2.0 * t * bx + cx
        val dy = 3.0 * t * t * ay + 2.0 * t * by + cy
        val ddx = 6.0 * t * ax + 2.0 * bx
        val ddy = 6.0 * t * ay + 2.0 * by
        return (dx * ddy - dy * ddx) / ((dx * dx + dy * dy) * Math.sqrt(dx * dx + dy * dy))
    }

    override fun getDCurvature(t: Double) = 0.0

    override fun getVelocity(t: Double) = 0.0
}
