package org.team5419.frc2022.fault.math.splines

import org.team5419.frc2022.fault.math.geometry.Pose2d
import org.team5419.frc2022.fault.math.geometry.Pose2dWithCurvature
import org.team5419.frc2022.fault.math.units.Meter
import org.team5419.frc2022.fault.math.units.SIUnit
import org.team5419.frc2022.fault.math.units.derived.Radian

@SuppressWarnings("LongParameterList")
object SplineGenerator {

    private const val kMinSampleSize = 1

    fun parameterizeSpline(
        s: Spline,
        maxDx: SIUnit<Meter>,
        maxDy: SIUnit<Meter>,
        maxDTheta: SIUnit<Radian>,
        t0: Double = 0.0,
        t1: Double = 1.0
    ): MutableList<Pose2dWithCurvature> {
        val rv: MutableList<Pose2dWithCurvature> = mutableListOf()
        rv.add(s.getPoseWithCurvature(0.0))
        val dt = t1 - t0
        var t = 0.0
        while (t < t1) {
            getSegmentArc(s, rv, t0, t1, maxDx, maxDy, maxDTheta)
            t += dt / kMinSampleSize
        }
        return rv
    }

    fun parameterizeSpline(
        s: Spline,
        maxDx: SIUnit<Meter>,
        maxDy: SIUnit<Meter>,
        maxDTheta: SIUnit<Radian>
    ): MutableList<Pose2dWithCurvature> {
        return parameterizeSpline(s, maxDx, maxDy, maxDTheta, 0.0, 1.0)
    }

    fun parameterizeSplines(
        splines: List<Spline>,
        maxDx: SIUnit<Meter>,
        maxDy: SIUnit<Meter>,
        maxDTheta: SIUnit<Radian>
    ): MutableList<Pose2dWithCurvature> {
        val rv: MutableList<Pose2dWithCurvature> = mutableListOf()
        if (splines.isEmpty()) return rv
        rv.add(splines.get(0).getPoseWithCurvature(0.0))
        for (s in splines) {
            val samples: MutableList<Pose2dWithCurvature> = parameterizeSpline(s, maxDx, maxDy, maxDTheta)
            samples.removeAt(0)
            rv.addAll(samples)
        }
        return rv
    }

    private fun getSegmentArc(
        s: Spline,
        rv: MutableList<Pose2dWithCurvature>,
        t0: Double,
        t1: Double,
        maxDx: SIUnit<Meter>,
        maxDy: SIUnit<Meter>,
        maxDTheta: SIUnit<Radian>
    ) {
        val p0 = s.getPoint(t0)
        val p1 = s.getPoint(t1)
        val r0 = s.getHeading(t0)
        val r1 = s.getHeading(t1)
        val transformation = Pose2d((p1 - p0) * -r0, r1 + -r0)
//        val transformation = Pose2d(Vector2(p0, p1).rotateBy(r0.inverse()), r1.rotateBy(r0.inverse()))
        val twist = transformation.twist
        if (twist.dy > maxDy || twist.dx > maxDx || twist.dTheta > maxDTheta) {
            getSegmentArc(s, rv, t0, (t0 + t1) / 2, maxDx, maxDy, maxDTheta)
            getSegmentArc(s, rv, (t0 + t1) / 2, t1, maxDx, maxDy, maxDTheta)
        } else {
            rv.add(s.getPoseWithCurvature(t1))
        }
    }
}
