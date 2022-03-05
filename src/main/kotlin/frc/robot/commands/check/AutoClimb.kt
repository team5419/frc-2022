package frc.robot.commands.check; 
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab
import frc.robot.ClimberConstants
import frc.robot.commands.check.CheckCommand
import frc.robot.subsystems.Climber


class AutoClimb(_climber: Climber, _time: Double, _tab: ShuffleboardTab) : CheckCommand("Climber", _tab, _time, 4) {
    private val climber : Climber = _climber

    init {
        addRequirements(_climber);
    }

    override fun check(i: Int): Boolean
    {
      return currentVelocities[i] >= ClimberConstants.autoCheckVelocities[i]
    }

    override fun runMotors() {
        for(i in 0..2) {
            climber.setPair(i, 0.1, 0.0);
        }
    }

    override fun getVels(): List<Double> {
      return climber.getAllVelocities();
    }
}
