package frc.robot.commands.check; 
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab
import frc.robot.ShooterConstants
import frc.robot.commands.check.CheckCommand
import frc.robot.subsystems.Shooter


class AutoShoot(_shooter: Shooter, _time: Double, _tab: ShuffleboardTab) : CheckCommand("Shooter", _tab, _time, 2) {
    private val shooter : Shooter = _shooter

    init {
        addRequirements(_shooter);
    }

    override fun check(i: Int): Boolean
    {
      return currentVelocities[i] >= if (i == 0) ShooterConstants.mainVelocity else ShooterConstants.kickerVelocity
    }

    override fun runMotors() {
      shooter.shoot(ShooterConstants.mainVelocity, ShooterConstants.kickerVelocity);
    }

    override fun getVels(): List<Double> {
      return listOf(shooter.flyWheelVelocity(shooter.mainMotor), shooter.flyWheelVelocity(shooter.kickerMotor));
    }
}
