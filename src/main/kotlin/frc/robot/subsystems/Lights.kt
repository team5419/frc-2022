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
import kotlin.collections.mutableListOf
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
    private val cached: MutableList<RGB> = mutableListOf<RGB>();

    // candle.setLEDs(255, 255, 255); // set the CANdle LEDs to white
    // conf
    init {
        config.stripType = LEDStripType.RGB; // set the strip type to RGB
        config.brightnessScalar = 1.0; // dim the LEDs to half brightness
        candle.configAllSettings(config);
        for(i in 0..359) {
            cached.add(hsvToRgb(i + 0.0, 1.0, 1.0))
        }
    }

    public fun setColor(rgb: RGB) {
        currentRGB = rgb;
        candle.setLEDs(rgb.g, rgb.r, rgb.b)
    }

    public fun setRainbow(index: Int) {
        for(i in 0..100) {
            val cach: RGB = cached[(index + i) % 360];
            println("${cach.r}, ${cach.g}, ${cach.b}");
            candle.setLEDs(cach.r, cach.g, cach.b, 255, i, 1)
        }
    }

    fun hsvToRgb(h: Double, s: Double, v: Double): RGB {
        var hh: Double = h / 60;
        val i: Int = Math.floor(hh).toInt();
        val ff: Double = hh - i;
        val p: Double = v * (1.0 - s);
        val q: Double = v * (1.0 - (s * ff));
        val t: Double = v * (1.0 - (s * (1.0 - ff)));
        var r: Double = 0.0;
        var g: Double = 0.0;
        var b: Double = 0.0;
        when(i) {
          0 -> {
              r = v;
              g = t;
              b = p;
          }
          1 -> {
              r = q;
              g = v;
              b = p;
          }
          2 -> {
              r = p;
              g = v;
              b = t;
          }
          3 -> {
              r = p;
              g = q;
              b = v;
          }
          4 -> {
              r = t;
              g = p;
              b = v;
          }
          5 -> {
              r = v;
              g = p;
              b = q;
          }
        }
        return RGB((r * 255).toInt(), (g * 255).toInt(), (b * 255).toInt());     
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