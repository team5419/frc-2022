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

class ColorSensor(tab: Shuffleboard) extends SubsystemBase() {

    init {
        motor.apply {
            restoreFactoryDefaults()
            setIdleMode(IdleMode.kBrake)
            setInverted(false)
            //setSensorPhase(false)
            setSmartCurrentLimit(40)
            setClosedLoopRampRate(1.0)
            setControlFramePeriodMs(50)
            setPeriodicFramePeriod(PeriodicFrame.kStatus2, 50)
        }

        
        controller.apply {
            setP(1.0, 1)
            setI(0.0, 1)
            setD(0.0, 1)
        }

        encoder.apply {
            setPosition(0.0)
        }
        layout.addNumber("Position", { encoder.getPosition() })
        // layout.addNumber("Velocity", { encoder.getVelocity() })
        layout.addNumber("Sensor 1", { sensor1.getValue().toDouble() })
        layout.addNumber("Sensor 2", { sensor2.getValue().toDouble() })
        layout.addNumber("Sensor 3", { sensor3.getValue().toDouble() })
    }


    private static ColorSensor mInstance;
    public static synchronized ColorSensor getInstance() {
        if (mInstance == null) {
            mInstance = new ColorSensor();
        }
        return mInstance;
    }
    
    // logger
    LogStorage<PeriodicIO> mStorage = null;

    public PeriodicIO mPeriodicIO = new PeriodicIO();
    private PicoColorSensor mPico;

    private val mForwardBreak: DigitalInput;
    private var mSawBall: boolean = false;

    public var mAllianceColor: ColorChoices = ColorChoices.NONE;
    public var mMatchedColor: ColorChoices;

    public enum ColorChoices {
        RED, BLUE, OTHER, NONE  
    }

    private var ColorSensor() {
        mMatchedColor = ColorChoices.NONE;
        mPico = new PicoColorSensor();

        mForwardBreak = new DigitalInput(Ports.getForwardBeamBreakPort());
    }

    @Override
    public var registerEnabledLoops(ILooper enabledLooper) {
        enabledLooper.register(new Loop() {
            @Override
            public var onStart(double timestamp) {
                mPico.start();
            }
 
            @Override
            public var onLoop(double timestamp) {
                outputTelemetry();

                // send log data
                SendLog();
            }
 
            @Override
            public var onStop(double timestamp) {
            }
        });
    }

    @Override
    public var checkSystem(): boolean {
        // TODO Auto-generated method stub
        return false;
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

    public static class PeriodicIO {
        // INPUTS
        public double timestamp;

        public RawColor raw_color;
        public double color_offset; // offset of blue - red
        public double adjusted_red;
        public double adjusted_blue;
        public double color_ratio; // ratio of red to blue color
        public int proximity;
        public boolean sensor0Connected;
    }

    @Override
    public var registerLogger(LoggingSystem LS) {
        SetupLog();
        LS.register(mStorage, "COLOR_SENSOR_LOGS.csv");
    }

    public var SetupLog() {
        mStorage = new LogStorage<PeriodicIO>();

        ArrayList<String> headers = new ArrayList<String>();
        headers.add("timestamp");

        headers.add("sensor_connected");

        headers.add("raw_red");
        headers.add("raw_blue");

        headers.add("adjusted_red");
        headers.add("adjusted_blue");
        headers.add("color_offset");

        headers.add("color_ratio");
        headers.add("proximity");

        headers.add("has_ball");
        headers.add("eject");

        mStorage.setHeaders(headers);
    }

    public var SendLog() {
        ArrayList<Number> items = new ArrayList<Number>();
        items.add(Timer.getFPGATimestamp());

        items.add(mPeriodicIO.sensor0Connected ? 1.0 : 0.0);

        items.add(mPeriodicIO.raw_color.red);
        items.add(mPeriodicIO.raw_color.blue);

        items.add(mPeriodicIO.adjusted_red);
        items.add(mPeriodicIO.adjusted_blue);
        items.add(mPeriodicIO.color_offset);

        items.add(mPeriodicIO.color_ratio);
        items.add(mPeriodicIO.proximity);

        // send data to logging storage
        mStorage.addData(items);
    }

    init {

    }

    override fun periodic() {
        @Override
            public synchronized var readPeriodicInputs() {
                mPeriodicIO.sensor0Connected = mPico.isSensor0Connected();
                mPeriodicIO.raw_color = mPico.getRawColor0();
                mPeriodicIO.adjusted_blue = mPeriodicIO.raw_color.blue; // keep blue the same
                mPeriodicIO.adjusted_red = mPeriodicIO.raw_color.red + mPeriodicIO.color_offset;
                mPeriodicIO.color_ratio = (double) mPeriodicIO.adjusted_red / (double) mPeriodicIO.adjusted_blue;
                mPeriodicIO.proximity = mPico.getProximity0();

                mPeriodicIO.timestamp = mPico.getLastReadTimestampSeconds();

                updateMatchedColor();
            }

    @Override 
    public synchronized var writePeriodicOutputs() {
        
    }
    }

    override fun simulationPeriodic() {
    }

    
    
}
