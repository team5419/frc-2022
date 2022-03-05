package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.ctre.phoenix.motorcontrol.can.TalonFX
import com.ctre.phoenix.motorcontrol.can.VictorSPX
import com.ctre.phoenix.motorcontrol.can.TalonSRX
import edu.wpi.first.networktables.NetworkTableEntry
import edu.wpi.first.networktables.EntryListenerFlags
import edu.wpi.first.networktables.EntryNotification
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets
import com.ctre.phoenix.motorcontrol.*
import kotlin.math.*
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab

import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout
import edu.wpi.first.wpilibj.DigitalInput;
import com.ctre.phoenix.CANifier;
import frc.robot.LightsConstants


class Lights(tab: ShuffleboardTab) : SubsystemBase() {

    // declare motors and ports
    public val lights = CANifier(LightsConstants.Ports.lights);

    // conf
    init {
        lights.apply {
            configFactoryDefault(100)
        }
    }

    fun testLights() {
        lights.setLEDOutput(100.0, CANifier.LEDChannel.LEDChannelA);
    }

    fun stop() {
        lights.setLEDOutput(0.0, CANifier.LEDChannel.LEDChannelB);
    }

    override fun periodic() {
    }

    override fun simulationPeriodic() {
    }
}