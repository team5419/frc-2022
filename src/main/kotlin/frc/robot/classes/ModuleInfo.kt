package frc.robot.classes;

class ModuleInfo {
    public val driverPort: Int;
    public val turnerPort: Int;
    public val driveInverted: Boolean;
    public val turnInverted: Boolean;
    public val cancoderPort: Int;
    public val offset: Double;
    constructor(_driverPort: Int, _turnerPort: Int, _driveInverted: Boolean, _turnInverted: Boolean, _cancoderPort: Int, _offset: Double) {
        this.driverPort = _driverPort;
        this.turnerPort = _turnerPort;
        this.driveInverted = _driveInverted;
        this.turnInverted = _turnInverted;
        this.cancoderPort = _cancoderPort;
        this.offset = _offset;
    }
}