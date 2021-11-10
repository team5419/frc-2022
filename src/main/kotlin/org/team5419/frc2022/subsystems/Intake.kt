package org.team5419.frc2022.subsystems

import org.team5419.frc2022.tab
import org.team5419.frc2022.IntakeConstants
import org.team5419.frc2022.fault.subsystems.Subsystem
import org.team5419.frc2022.fault.math.units.native.NativeUnitRotationModel
import com.ctre.phoenix.motorcontrol.can.TalonSRX
import com.ctre.phoenix.motorcontrol.FeedbackDevice
import com.ctre.phoenix.motorcontrol.NeutralMode
import com.ctre.phoenix.motorcontrol.ControlMode

@Suppress("TooManyFunctions")
object Intake : Subsystem("Intake") {
    // motors

    val intakeMotor = TalonSRX(IntakeConstants.IntakePort).apply {
        configFactoryDefault(100)

        setNeutralMode(NeutralMode.Coast)

        setInverted(true)

        //configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute) // temporary
    }

    val deployMotor = TalonSRX(IntakeConstants.DeployPort).apply {
        //println("success")
        configFactoryDefault(100)

        configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute)


        //
        setNeutralMode(NeutralMode.Coast)

        setSensorPhase(false)
        setInverted(true)

        config_kP(0, IntakeConstants.DeployPID.P, 100)
        config_kI(0, IntakeConstants.DeployPID.I, 100)
        config_kD(0, IntakeConstants.DeployPID.D, 100)

        // configForwardSoftLimitThreshold(
        //     radiansToNativeUnits(IntakeConstants.DeployPosition.value), 100
        // )
        // configForwardSoftLimitEnable(true)

        configClosedLoopPeakOutput(0, 0.6, 100)
        setSelectedSensorPosition(0,0,100)
    }

    // intake modes

    init {
        tab.addNumber("depoy pos", { deployMotor.getSelectedSensorPosition(0).toDouble() })
        tab.addNumber("depoy err", { deployMotor.getClosedLoopError(0).toDouble() })
    }

    public enum class IntakeMode {
        INTAKE,
        OUTTAKE,
        OFF
    }

    public var intakeMode = IntakeMode.OFF
        set(mode: IntakeMode) {
            if ( mode == field ) return

            when (mode) {
                IntakeMode.INTAKE  -> { intakeMotor.set(ControlMode.PercentOutput, 0.8 ) }
                IntakeMode.OUTTAKE -> { intakeMotor.set(ControlMode.PercentOutput, -1.0 ) }
                IntakeMode.OFF     -> { intakeMotor.set(ControlMode.PercentOutput, 0.0 ) }
            }

            field = mode
        }

    // deploy modes

    /*public enum class DeployMode {
        DEPLOY,
        STORE,
        OFF
    }

    public var deployMode = DeployMode.OFF
        set(mode: DeployMode) {
            if ( mode == field ) return

            when (mode) {
                DeployMode.DEPLOY -> {
                    deployMotor.set(ControlMode.PercentOutput, 0.0)
                }
                DeployMode.STORE -> {
                    deployMotor.set(ControlMode.Position, IntakeConstants.StorePosition.toDouble())
                }
                DeployMode.OFF -> {
                    deployMotor.set(ControlMode.PercentOutput, 0.0 )
                }
            }

            field = mode
        }*/

    // deploy functions

    fun radiansToNativeUnits(radians: Double): Int = (radians / Math.PI / 2 * 4096).toInt()

    fun nativeUnitsToRadians(ticks: Int): Double = ticks / 4096 * 2 * Math.PI

    /*init {
        tab.addNumber("intake pos", {deployMotor.getSelectedSensorPosition(0).toDouble()})
    }*/

    public fun store() {
        deployMotor.set(ControlMode.PercentOutput, 0.5)
        //deployMode = DeployMode.STORE

        // turn off the intake, were storing it
        intakeMode = IntakeMode.OFF
    }

    public fun deploy() {
        deployMotor.set(ControlMode.PercentOutput, 0.0)
        //deployMode = DeployMode.DEPLOY
    }

    /*public fun stopDeploy() {
        if( deployMode != DeployMode.STORE ) {
            deployMode = DeployMode.OFF
        }
    }*/

    // intake functions

    public fun intake() {
        intakeMode = IntakeMode.INTAKE
    }

    public fun outtake() {
        intakeMode = IntakeMode.OUTTAKE
    }

    public fun stopIntake() {
        intakeMode = IntakeMode.OFF
    }

    public fun isActive() = intakeMode == IntakeMode.INTAKE

    //public fun isAtSetPoint() =
    //    deployMotor.getClosedLoopError() < 30

    // combined functions

    public fun stop() {
        //stopDeploy()
        stopIntake()
    }

    // subsystem functions
    fun reset() {
        //deployMode = DeployMode.OFF
        deployMotor.set(ControlMode.PercentOutput, 0.0)
        intakeMode = IntakeMode.OFF
    }

    override fun autoReset() = reset()
    override fun teleopReset() = reset()

    override fun periodic() {
    }
}
