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
import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import frc.robot.LightsConstants
import frc.robot.classes.RGB

class Lights(tab: ShuffleboardTab) : SubsystemBase() {

    // declare motors and ports
    private val lights1 = AddressableLED(LightsConstants.Ports.lights1);
    // private val lights2 = AddressableLED(LightsConstants.Ports.lights2)
    private val buffer1 = AddressableLEDBuffer(LightsConstants.len1);
    // private val buffer2 = AddressableLEDBuffer(LightsConstants.len2);
    public var currentRGB: RGB = RGB(0, 0, 0);
    public var blinking: Boolean = false;

    // conf
    init {
        lights1.setLength(buffer1.getLength());
        // lights2.setLength(buffer2.getLength());
        lights1.start();
        // lights2.start();
    }

    public fun setColor() {
        //println("color ${currentRGB.r} ${currentRGB.g} ${currentRGB.b}");
        for(i in 0..buffer1.getLength() - 1) {
            buffer1.setRGB(i, currentRGB.r, currentRGB.g, currentRGB.b);
        }
        // for(i in 0..buffer2.getLength() - 1) {
        //     buffer2.setRGB(i, currentRGB.r, currentRGB.g, currentRGB.b);
        // }
        lights1.setData(buffer1);
        //lights2.setData(buffer2);
    }

    public fun off() {
        for(i in 0..buffer1.getLength() - 1) {
            buffer1.setRGB(i, 0, 0, 0);
        }
        // for(i in 0..buffer2.getLength() - 1) {
        //     buffer2.setRGB(i, 0, 0, 0);
        // }
        lights1.setData(buffer1);
        //lights2.setData(buffer2);
    }

    public fun isEqualTo(r: Int, g: Int, b: Int): Boolean {
        return currentRGB.r == r && currentRGB.b == b && currentRGB.g == g;
    }

    override fun periodic() {
    }

    override fun simulationPeriodic() {
    }
    
    public fun stop() {
        currentRGB = RGB(0, 0, 0);
        blinking = false;
        setColor();
    }
}