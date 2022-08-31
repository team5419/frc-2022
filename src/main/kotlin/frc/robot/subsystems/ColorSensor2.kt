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
import com.ctre.phoenix.led.RainbowAnimation
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab

import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import frc.robot.LightsConstants
import frc.robot.classes.RGB

class ColorSensor2(tab: ShuffleboardTab) : SubsystemBase() {
    // private val buffer2 = AddressableLEDBuffer(LightsConstants.len2);
    private var currentRawColor: RGB = RGB(0, 0, 0);
        // Example usage of a CANdle
    private val candle: CANdle = CANdle(LightsConstants.Ports.lights1); // creates a new CANdle with ID 0
    private val config: CANdleConfiguration = CANdleConfiguration();
    public var isRainbow: Boolean = false;
    //private val cached: MutableList<RGB> = mutableListOf<RGB>();

    // candle.setLEDs(255, 255, 255); // set the CANdle LEDs to white
    // conf
    init {
        config.stripType = LEDStripType.RGB; // set the strip type to RGB
        config.brightnessScalar = 1.0; // dim the LEDs to half brightness
        candle.configAllSettings(config);
    }
    
    // check if we see a ball
    public var seesBall(): boolean {
        return !Util.epsilonEquals(mPeriodicIO.color_ratio, 1.0, Constants.ColorSensorConstants.kColorSensorRatioThreshold);
    }

    // check if we have a new ball
    public var seesNewBall(): boolean {
        boolean newBall = false;
        if ((seesBall() && !mSawBall)) {
            newBall = true;
        }
        mSawBall = seesBall();
        return newBall;
    }

    // check if we have the right color
    public var hasCorrectColor(): boolean {
        return mMatchedColor == mAllianceColor;
    }

    // check if we have the opposite color
    public val hasOppositeColor(): boolean {
        return !hasCorrectColor()
                    && (mMatchedColor != ColorChoices.OTHER)
                    && (mMatchedColor != ColorChoices.NONE);
    }

    // update baseline color values
    public var updateColorOffset() {
        if (mPeriodicIO.raw_color != null) {
            mPeriodicIO.color_offset = mPeriodicIO.raw_color.blue - mPeriodicIO.raw_color.red;
        }
    }

    // update our alliance color
    // only should be updated in disabled periodic
    public var updateAllianceColor() {
        if (DriverStation.isDSAttached()) {
            if (edu.wpi.first.wpilibj.DriverStation.getAlliance() == Alliance.Red) {
                mAllianceColor = ColorChoices.RED;
            } else if (edu.wpi.first.wpilibj.DriverStation.getAlliance() == Alliance.Blue){
                mAllianceColor = ColorChoices.BLUE;
            }
        } else {
            mAllianceColor = ColorChoices.NONE;
            DriverStation.reportError("No Alliance Color Detected", true);
        }
    }

    // update the color of the cargo we see
    public var updateMatchedColor() {
        if (Util.epsilonEquals(mPeriodicIO.color_ratio,
                               1.0,
                               Constants.ColorSensorConstants.kColorSensorRatioThreshold)) { 
            mMatchedColor = ColorChoices.NONE;
        } else {
            if (mPeriodicIO.color_ratio > 1.0) {
                mMatchedColor = ColorChoices.RED;
            } else if (mPeriodicIO.color_ratio < 1.0) {
                mMatchedColor = ColorChoices.BLUE;
            } else {
                mMatchedColor = ColorChoices.OTHER;
            }
        }
    }

    public var outputTelemetry() {
        SmartDashboard.putNumber("color_ratio", mPeriodicIO.color_ratio);
        SmartDashboard.putNumber("color_offset", mPeriodicIO.color_offset);
        SmartDashboard.putNumber("adjusted_red", mPeriodicIO.adjusted_red);
        SmartDashboard.putNumber("adjusted_blue", mPeriodicIO.adjusted_blue);
    }

    //subsystem getters
    public double getDetectedRValue() {
        if (mPeriodicIO.raw_color == null) {
            return 0;
        }
        return mPeriodicIO.raw_color.red;
    }
    public fun getDetectedGValue(): double {
        if (mPeriodicIO.raw_color == null) {
            return 0;
        }
        return mPeriodicIO.raw_color.green;
    }
    public fun getDetectedBValue(): double {
        if (mPeriodicIO.raw_color == null) {
            return 0;
        }
        return mPeriodicIO.raw_color.blue;
    }
    public getAllianceColor(): ColorChoices {
        return mAllianceColor;
    }
    public getMatchedColor(): String {
        return mMatchedColor.toString();
    }
    public getForwardBeamBreak(): boolean {
        return !mForwardBreak.get();
    }
    public getDistance(): double {
        return mPeriodicIO.proximity;
    }
    public getColorRatio(): double {
        return mPeriodicIO.color_ratio;
    }
    public getSensor0(): boolean {
        return mPeriodicIO.sensor0Connected;
    }
    public getTimestamp(): double {
        return mPeriodicIO.timestamp;
    }
    override fun periodic() {
    }

    override fun simulationPeriodic() {
    }
}