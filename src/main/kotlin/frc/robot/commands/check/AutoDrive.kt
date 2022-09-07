package frc.robot.commands.check; 
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab
import frc.robot.DriveConstants
import frc.robot.commands.check.CheckCommand
import frc.robot.subsystems.Drivetrain


class AutoDrive(_drivetrain: Drivetrain, _time: Double, _tab: ShuffleboardTab) : CheckCommand("Drivetrain", _tab, _time, 4) {
    private val drivetrain : Drivetrain = _drivetrain

    init {
        addRequirements(_drivetrain);
    }

    override fun check(i: Int): Boolean
    {
      return currentVelocities[i] >= DriveConstants.autoCheckVelocities[i]
    }

    override fun runMotors() {
      drivetrain.drive(0.5, 0.0, 0.0)
    }

    override fun getVels(): List<Double> {
      return drivetrain.getAllVelocities()
    }
}
