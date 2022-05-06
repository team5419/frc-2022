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
import com.ctre.phoenix.led.CANdle
import com.ctre.phoenix.led.CANdleConfiguration
import com.ctre.phoenix.led.CANdle.LEDStripType
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
    // private val buffer2 = AddressableLEDBuffer(LightsConstants.len2);
    private var currentRGB: RGB = RGB(0, 0, 0);
        // Example usage of a CANdle
    private val candle: CANdle = CANdle(LightsConstants.Ports.lights1); // creates a new CANdle with ID 0
    private val config: CANdleConfiguration = CANdleConfiguration();
    

    // candle.setLEDs(255, 255, 255); // set the CANdle LEDs to white
    // conf
    init {
        config.stripType = LEDStripType.RGB; // set the strip type to RGB
        config.brightnessScalar = 0.5; // dim the LEDs to half brightness
        candle.configAllSettings(config);
    }

    public fun setColor(rgb: RGB) {
        println("setting color (red = ${rgb.r})")
        currentRGB = rgb;
        candle.setLEDs(rgb.g, rgb.r, rgb.b)
    }

    public fun off() {
        candle.setLEDs(0, 0, 0)
    }

    public fun isEqualTo(r: Int, g: Int, b: Int): Boolean {
        return currentRGB.r == r && currentRGB.b == b && currentRGB.g == g;
    }

    override fun periodic() {
    }

    override fun simulationPeriodic() {
    }
    
    public fun stop() {
        setColor(RGB(0, 0, 0));
    }
}