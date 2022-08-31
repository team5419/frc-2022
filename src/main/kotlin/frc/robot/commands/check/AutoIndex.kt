package frc.robot.commands.check; 
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab
import frc.robot.IndexerConstants
import frc.robot.commands.check.CheckCommand
import frc.robot.subsystems.Indexer


class AutoIndex(_indexer: Indexer, _time: Double, _tab: ShuffleboardTab) : CheckCommand("Indexer", _tab, _time, 1) {
    private val indexer : Indexer = _indexer

    init {
        addRequirements(_indexer);
    }

    override fun check(i: Int): Boolean
    {
      return currentVelocities[i] >= IndexerConstants.autoSpeed;
    }

    override fun runMotors() {
      indexer.index(1.0)
    }

    override fun getVels(): List<Double> {
      return listOf(indexer.encoder.getVelocity());
    }
}
