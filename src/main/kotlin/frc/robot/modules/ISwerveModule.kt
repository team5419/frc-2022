package frc.robot.modules;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import frc.robot.modules.SwerveModule;
import frc.robot.modules.SimulatedSwerveModule;
import frc.robot.classes.ModuleInfo;
interface ISwerveModule {
    fun getDrive(): Double;
    fun getTurn(): Rotation2d;
    fun getState(): SwerveModuleState;
    fun setDesiredState(desiredState: SwerveModuleState, preventTurn: Boolean, slow: Boolean, pid: Boolean);
    fun simulationPeriodic(dt: Double);
    fun resetEncoders();
    fun setBrakeMode(on: Boolean);
    fun test();
}
object Module {
    public fun create(isSim: Boolean = false, info: ModuleInfo): ISwerveModule {
        if(isSim) {
            return SimulatedSwerveModule(info.driverPort, info.cancoderPort, info.turnerPort, info.cancoderPort + 4);
        } else {
            return SwerveModule(info.driverPort, info.turnerPort, info.cancoderPort, info.offset, info.driveInverted, info.turnInverted);
        }
    }
}