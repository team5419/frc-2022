package frc.robot.modules;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import frc.robot.modules.SwerveModule;
import frc.robot.modules.SimulatedSwerveModule;
interface ISwerveModule {
    fun getTurn(): Rotation2d;
    fun getState(): SwerveModuleState;
    fun setDesiredState(desiredState: SwerveModuleState);
    fun simulationPeriodic(dt: Double);
    fun resetEncoders();
    fun setBrakeMode(on: Boolean);
}
object Module {
    public fun create(drivePort: Int, turnPort: Int, encoderPort: Int, isSim: Boolean = false, driveInverted: Boolean = false, turnInverted: Boolean = false): ISwerveModule {
        if(isSim) {
            return SimulatedSwerveModule(drivePort, encoderPort, turnPort, encoderPort + 4);
        } else {
            return SwerveModule(drivePort, turnPort, encoderPort, driveInverted, turnInverted);
        }
    }
}