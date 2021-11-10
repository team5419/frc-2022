package org.team5419.frc2022.auto.actions

import org.team5419.frc2022.fault.auto.Action
import org.team5419.frc2022.fault.math.units.SIUnit
import org.team5419.frc2022.fault.math.units.Second
import org.team5419.frc2022.fault.math.units.derived.Radian
import edu.wpi.first.wpilibj.controller.PIDController
import org.team5419.frc2022.subsystems.Drivetrain
import org.team5419.frc2022.DriveConstants.TurnPID

public enum class TurnDiection(val value: Int){ ClockWise(1), CounterClockwise(-1) }

public class TurnAction(theta: Double, val dir: Int): Action() {

    constructor(theta: Double, direction: TurnDiection) : this(theta, direction.value)

    private val pid: PIDController
    private var output: Double

    init{
        pid = PIDController(TurnPID.P, TurnPID.I, TurnPID.D).apply{
            enableContinuousInput(0.0, 360.0)
            setSetpoint(theta)
            setTolerance(1.0)
        }
        output = 0.0

        finishCondition.set({ pid.atSetpoint() })
    }

    override fun update(dt: SIUnit<Second>) {
        output = pid.calculate(Drivetrain.angle)
        Drivetrain.setPercent(dir * output, -dir * output)
    }

    override fun finish() {
        Drivetrain.setPercent(0.0, 0.0)
    }
}
