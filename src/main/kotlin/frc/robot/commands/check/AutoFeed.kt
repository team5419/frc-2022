package frc.robot.commands.check; 
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab
import frc.robot.FeederConstants
import frc.robot.commands.check.CheckCommand
import frc.robot.subsystems.Feeder


class AutoFeed(_feeder: Feeder, _time: Double, _tab: ShuffleboardTab) : CheckCommand("Feeder", _tab, _time, 1) {
    private val feeder : Feeder = _feeder

    init {
        addRequirements(_feeder);
    }

    override fun check(i: Int): Boolean
    {
      return currentVelocities[i] >= FeederConstants.autoSpeed;
    }

    override fun runMotors() {
      feeder.currentVel = FeederConstants.activePercent;
      //feeder.feed();
    }

    override fun getVels(): List<Double> {
      return listOf(feeder.encoder.getVelocity());
    }
}
