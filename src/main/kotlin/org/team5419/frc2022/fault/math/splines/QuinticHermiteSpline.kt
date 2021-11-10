package org.team5419.frc2022.fault.math.splines

import org.team5419.frc2022.fault.math.geometry.Vector2d
import org.team5419.frc2022.fault.math.geometry.Rotation2d
import org.team5419.frc2022.fault.math.geometry.Pose2d
import org.team5419.frc2022.fault.math.units.meters
import org.team5419.frc2022.fault.math.units.operations.times
import org.team5419.frc2022.fault.math.units.operations.div

// yes external contructors are disgusting. but they work
@Suppress("FunctionNaming", "MagicNumber")
fun QuinticHermiteSpline(p0: Vector2d, p1: Vector2d, h0: Rotation2d, h1: Rotation2d): QuinticHermiteSpline {
    val scale = 1.2 * p0.distance(p1)

    val x0 = p0.x.value
    val x1 = p1.x.value
    val dx0 = h0.cos * scale
    val dx1 = h1.cos * scale
    val ddx0 = 0.0
    val ddx1 = 0.0

    // y vars
    val y0 = p0.y.value
    val y1 = p1.y.value
    val dy0 = h0.sin * scale
    val dy1 = h1.sin * scale
    val ddy0 = 0.0
    val ddy1 = 0.0

    return QuinticHermiteSpline(x0, x1, dx0, dx1, ddx0, ddx1, y0, y1, dy0, dy1, ddy0, ddy1)
}

@Suppress("FunctionNaming")
fun QuinticHermiteSpline(p0: Pose2d, p1: Pose2d): QuinticHermiteSpline {
    return QuinticHermiteSpline(p0.translation, p1.translation, p0.rotation, p1.rotation)
}

@SuppressWarnings("MagicNumber", "TooManyFunctions")
class QuinticHermiteSpline(
    x0: Double,
    x1: Double,
    dx0: Double,
    dx1: Double,
    ddx0: Double,
    ddx1: Double,
    y0: Double,
    y1: Double,
    dy0: Double,
    dy1: Double,
    ddy0: Double,
    ddy1: Double
) : Spline() {

    private var ax: Double = 0.0
    private var bx: Double = 0.0
    private var cx: Double = 0.0
    private var dx: Double = 0.0
    private var ex: Double = 0.0
    private var fx: Double = 0.0

    private var ay: Double = 0.0
    private var by: Double = 0.0
    private var cy: Double = 0.0
    private var dy: Double = 0.0
    private var ey: Double = 0.0
    private var fy: Double = 0.0

    private var x0: Double
    private var x1: Double
    private var dx0: Double
    private var dx1: Double
    private var ddx0: Double
    private var ddx1: Double

    private var y0: Double
    private var y1: Double
    private var dy0: Double
    private var dy1: Double
    private var ddy0: Double
    private var ddy1: Double

    init {
        this.x0 = x0
        this.x1 = x1
        this.dx0 = dx0
        this.dx1 = dx1
        this.ddx0 = ddx0
        this.ddx1 = ddx1

        this.y0 = y0
        this.y1 = y1
        this.dy0 = dy0
        this.dy1 = dy1
        this.ddy0 = ddy0
        this.ddy1 = ddy1

        calcCoeffs()
    }

    private fun calcCoeffs() {
        ax = -6 * x0 - 3 * dx0 - 0.5 * ddx0 + 0.5 * ddx1 - 3 * dx1 + 6 * x1
        bx = 15 * x0 + 8 * dx0 + 1.5 * ddx0 - ddx1 + 7 * dx1 - 15 * x1
        cx = -10 * x0 - 6 * dx0 - 1.5 * ddx0 + 0.5 * ddx1 - 4 * dx1 + 10 * x1
        dx = 0.5 * ddx0
        ex = dx0
        fx = x0

        ay = -6 * y0 - 3 * dy0 - 0.5 * ddy0 + 0.5 * ddy1 - 3 * dy1 + 6 * y1
        by = 15 * y0 + 8 * dy0 + 1.5 * ddy0 - ddy1 + 7 * dy1 - 15 * y1
        cy = -10 * y0 - 6 * dy0 - 1.5 * ddy0 + 0.5 * ddy1 - 4 * dy1 + 10 * y1
        dy = 0.5 * ddy0
        ey = dy0
        fy = y0
    }

    val startPose: Pose2d
        get() = Pose2d(Vector2d(x0.meters, y0.meters), Rotation2d(dx0, dy0, true))
    val endPose: Pose2d
        get() = Pose2d(Vector2d(x1.meters, y1.meters), Rotation2d(dx1, dy1, true))

    override fun getPoint(t: Double): Vector2d {
        val x = ax * t * t * t * t * t + bx * t * t * t * t + cx * t * t * t + dx * t * t + ex * t + fx
        val y = ay * t * t * t * t * t + by * t * t * t * t + cy * t * t * t + dy * t * t + ey * t + fy
        return Vector2d(x.meters, y.meters)
    }

    private fun dx(t: Double) = 5 * ax * t * t * t * t + 4 * bx * t * t * t + 3 * cx * t * t + 2 * dx * t + ex
    private fun dy(t: Double) = 5 * ay * t * t * t * t + 4 * by * t * t * t + 3 * cy * t * t + 2 * dy * t + ey

    private fun ddx(t: Double) = 20 * ax * t * t * t + 12 * bx * t * t + 6 * cx * t + 2 * dx
    private fun ddy(t: Double) = 20 * ay * t * t * t + 12 * by * t * t + 6 * cy * t + 2 * dy

    private fun dddx(t: Double) = 60 * ax * t * t + 24 * bx * t + 6 * cx
    private fun dddy(t: Double) = 60 * ay * t * t + 24 * by * t + 6 * cy

    override fun getVelocity(t: Double) = Math.hypot(dx(t), dy(t))

    override fun getCurvature(t: Double): Double {
        var temp = dx(t) * ddy(t) - ddx(t) * dy(t)
        temp /= ((dx(t) * dx(t) + dy(t) * dy(t)) * Math.sqrt((dx(t) * dx(t) + dy(t) * dy(t))))
        return temp
    }

    override fun getDCurvature(t: Double): Double {
        val dx2dy2 = (dx(t) * dx(t) + dy(t) * dy(t))
        val num = (dx(t) * dddy(t) - dddx(t) * dy(t)) * dx2dy2 -
            3 * (dx(t) * ddy(t) - ddx(t) * dy(t)) * (dx(t) * ddx(t) + dy(t) * ddy(t))
        return num / (dx2dy2 * dx2dy2 * Math.sqrt(dx2dy2))
    }

    fun dCurvature2(t: Double): Double {
        val dx2dy2 = (dx(t) * dx(t) + dy(t) * dy(t))
        val num = (dx(t) * dddy(t) - dddx(t) * dy(t)) * dx2dy2 -
            3 * (dx(t) * ddy(t) - ddx(t) * dy(t)) * (dx(t) * ddx(t) + dy(t) * ddy(t))
        return num * num / (dx2dy2 * dx2dy2 * dx2dy2 * dx2dy2 * dx2dy2)
    }

    override fun getHeading(t: Double) = Rotation2d(dx(t), dy(t), true)

    fun sumDCurvature2(): Double {
        val dt = 1.0 / kSamples
        var sum = 0.0
        var t = 0.0
        while (t < 1.0) {
            sum += (dt * dCurvature2(t))
            t += dt
        }
        return sum
    }

    override fun toString(): String {
        return "$ax*t^5 + $bx*t^4 + $cx*t^3 + $dx*t^2 + $ex*t * $fx \n" +
                "$ay*t^5 + $by*t^4 + $cy*t^3 + $dy*t^2 + $ey*t * $fy"
    }

    companion object {
        private const val kEpsilon = 1E-5
        private const val kStepSize = 1.0
        private const val kMinDelta = 0.001
        private const val kSamples = 100
        private const val kMaxIterations = 100

        private data class ControlPoint(var ddx: Double = 0.0, var ddy: Double = 0.0)

        public fun sumDCurvature2(splines: MutableList<QuinticHermiteSpline>): Double {
            var sum: Double = 0.0
            for (s in splines) {
                sum += s.sumDCurvature2()
            }
            return sum
        }

        public fun optimizeSpline(splines: MutableList<QuinticHermiteSpline>): Double {
            var count = 0
            var prev = sumDCurvature2(splines)
            while (count < kMaxIterations) {
                runOptimizationIteration(splines)
                var current = sumDCurvature2(splines)
                if (prev - current < kMinDelta)
                    return current
                prev = current
                count++
            }
            return prev
        }

        @Suppress("ComplexMethod")
        private fun runOptimizationIteration(splines: MutableList<QuinticHermiteSpline>) {
            if (splines.size <= 1) return
            val controlPoints = arrayOfNulls<ControlPoint>(splines.size - 1)
            var magnitude = 0.0

            for (i in 0..splines.size - 2) {
                val temp = splines.get(i)
                val temp1 = splines.get(i + 1)

                val original = sumDCurvature2(splines)
                controlPoints[i] = ControlPoint()

                splines.set(i, QuinticHermiteSpline(
                    temp.x0, temp.x1, temp.dx0, temp.dx1, temp.ddx0, temp.ddx1 + kEpsilon,
                    temp.y0, temp.y1, temp.dy0, temp.dy1, temp.ddy0, temp.ddy1
                ))
                splines.set(i + 1, QuinticHermiteSpline(
                    temp1.x0, temp1.x1, temp1.dx0, temp1.dx1, temp1.ddx0 + kEpsilon, temp1.ddx1,
                    temp1.y0, temp1.y1, temp1.dy0, temp1.dy1, temp1.ddy0, temp1.ddy1
                ))
                controlPoints[i]?.ddx = (sumDCurvature2(splines) - original) / kEpsilon

                splines.set(i, QuinticHermiteSpline(
                    temp.x0, temp.x1, temp.dx0, temp.dx1, temp.ddx0, temp.ddx1,
                    temp.y0, temp.y1, temp.dy0, temp.dy1, temp.ddy0, temp.ddy1 + kEpsilon
                ))
                splines.set(i + 1, QuinticHermiteSpline(
                    temp1.x0, temp1.x1, temp1.dx0, temp1.dx1, temp1.ddx0, temp1.ddx1,
                    temp1.y0, temp1.y1, temp1.dy0, temp1.dy1, temp1.ddy0 + kEpsilon, temp1.ddy1
                ))
                controlPoints[i]?.ddy = (sumDCurvature2(splines) - original) / kEpsilon

                splines.set(i, temp)
                splines.set(i + 1, temp1)
                magnitude += (controlPoints[i]!!.ddx * controlPoints[i]!!.ddx +
                    controlPoints[i]!!.ddy * controlPoints[i]!!.ddy)
            }
            magnitude = Math.sqrt(magnitude)

            val p2 = Vector2d(0.0.meters, sumDCurvature2(splines).meters)

            for (i in 0..splines.size - 2) {
                if (splines.get(i).startPose.isCollinear(splines.get(i + 1).startPose) &&
                        splines.get(i).endPose.isCollinear(splines.get(i + 1).endPose)) {
                    continue
                }
                controlPoints[i]!!.ddx *= (kStepSize / magnitude)
                controlPoints[i]!!.ddy *= (kStepSize / magnitude)

                splines.get(i).ddx1 -= controlPoints[i]!!.ddx
                splines.get(i).ddy1 -= controlPoints[i]!!.ddy
                splines.get(i + 1).ddx0 -= controlPoints[i]!!.ddx
                splines.get(i + 1).ddy0 -= controlPoints[i]!!.ddy

                splines.get(i).calcCoeffs()
                splines.get(i + 1).calcCoeffs()
            }

            val p1 = Vector2d(-kStepSize.meters, sumDCurvature2(splines).meters)

            for (i in 0..splines.size - 2) {
                if (splines.get(i).startPose.isCollinear(splines.get(i + 1).startPose) &&
                        splines.get(i).endPose.isCollinear(splines.get(i + 1).endPose)) {
                    continue
                }
                splines.get(i).ddx1 += (2.0 * controlPoints[i]!!.ddx)
                splines.get(i).ddy1 += (2.0 * controlPoints[i]!!.ddy)

                splines.get(i + 1).ddx0 += (2.0 * controlPoints[i]!!.ddx)
                splines.get(i + 1).ddy0 += (2.0 * controlPoints[i]!!.ddy)

                splines.get(i).calcCoeffs()
                splines.get(i + 1).calcCoeffs()
            }

            val p3 = Vector2d(kStepSize.meters, sumDCurvature2(splines).meters)

            val stepSize = FunctionalQuadraticSpline(
                    p1, p2, p3
            ).vertexXCoordinate

            for (i in 0..splines.size - 2) {
                if (splines.get(i).startPose.isCollinear(splines.get(i + 1).startPose) &&
                        splines.get(i).endPose.isCollinear(splines.get(i + 1).endPose)) {
                    continue
                }
                controlPoints[i]!!.ddx *= (1.0 + stepSize.value / kStepSize)
                controlPoints[i]!!.ddy *= (1.0 + stepSize.value / kStepSize)

                splines.get(i).ddx1 += controlPoints[i]!!.ddx
                splines.get(i).ddy1 += controlPoints[i]!!.ddy

                splines.get(i + 1).ddx0 += controlPoints[i]!!.ddx
                splines.get(i + 1).ddy0 += controlPoints[i]!!.ddy

                splines.get(i).calcCoeffs()
                splines.get(i + 1).calcCoeffs()
            }
        }
    }
}
