// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.ctre.phoenix.motorcontrol.can.TalonFX
import com.ctre.phoenix.motorcontrol.can.TalonSRX
import edu.wpi.first.networktables.NetworkTableEntry
import edu.wpi.first.networktables.EntryListenerFlags
import edu.wpi.first.networktables.EntryNotification
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets
import com.ctre.phoenix.motorcontrol.*
import kotlin.math.*

import frc.robot.ShooterConstants
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab

class Shooter(tab: ShuffleboardTab) : SubsystemBase() {

    val leaderMotor = TalonFX(ShooterConstants.Ports.leader)
    val followerMotor = TalonFX(ShooterConstants.Ports.follower)
    var defaultVelocity: Double = 0.0

    init {
        leaderMotor.apply {
            configFactoryDefault(100)

            setNeutralMode(NeutralMode.Coast)
            setSensorPhase(false)
            setInverted(true)

            configSupplyCurrentLimit(SupplyCurrentLimitConfiguration(true, 40.0, 0.0, 0.0), 100)

            // bang bang PID
            config_kP(0, 10000.0, 100)
            config_kI(0, 0.0, 100)
            config_kD(0, 0.0, 100)
            config_kF(0, 0.0, 100)

            // velocity controlle PID
            config_kP(1, 0.5, 100)
            config_kI(1, 0.0, 100)
            config_kD(1, 0.0, 100)
            config_kF(1, 0.06, 100)

            // we want to use velocity controlle
            selectProfileSlot(1, 0)

            setSelectedSensorPosition(0.0, 0, 100)
            configClosedloopRamp(1.0, 100)

            configClosedLoopPeriod(0, 1, 100)

            configPeakOutputForward(1.0, 100)
            configPeakOutputReverse(0.0, 100)
        }

        followerMotor.apply {
            follow(leaderMotor)

            setInverted(false)
            setNeutralMode(NeutralMode.Coast)

            configSupplyCurrentLimit(SupplyCurrentLimitConfiguration(true, 40.0, 0.0, 0.0), 100)

            configPeakOutputForward(1.0, 100)
            configPeakOutputReverse(0.0, 100)
        }

        tab.addNumber("Attempted Velocity", { setpoint })
        tab.addNumber("Real Velocity", { flyWheelVelocity() })
        tab.add("B button shooter velocity", 0.0)
            .withWidget(BuiltInWidgets.kNumberSlider)
            .withProperties(mapOf("min" to 0, "max" to 5000))
            .getEntry()
            .addListener({ value: EntryNotification -> this.defaultVelocity = value.value.getDouble() }, EntryListenerFlags.kUpdate)
    }

    public var setpoint = 0.0

    public fun flyWheelVelocity(): Double {
        return leaderMotor.getSelectedSensorVelocity(0)
    }

    public fun isSpedUp(): Boolean {
        return setpoint != 0.0 && flyWheelVelocity() >= setpoint
    }

    public fun stop() {
        setpoint = 0.0
        leaderMotor.set(ControlMode.PercentOutput, 0.0)
    }

    public fun shoot(velocity: Double) {
        if(velocity == setpoint) {
            return
        }
        if(velocity == -1.0) {
            setpoint = this.defaultVelocity
        } else if(velocity < 0) {
            return
        } else {
            setpoint = velocity
        }
        println("Setting Velocity: ${setpoint}")
        leaderMotor.set(ControlMode.Velocity, setpoint)
    }

    override fun periodic() {
    // This method will be called once per scheduler run
    }

    override fun simulationPeriodic() {
    // This method will be called once per scheduler run during simulation
    }
}