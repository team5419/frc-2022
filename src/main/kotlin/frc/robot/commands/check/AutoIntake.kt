package frc.robot.commands.check; 
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab
import frc.robot.IntakeConstants
import frc.robot.commands.check.CheckCommand
import frc.robot.subsystems.Intake


class AutoIntake(_intake: Intake, _time: Double, _tab: ShuffleboardTab) : CheckCommand("Intake", _tab, _time, 1) {
    private val intake : Intake = _intake

    init {
        addRequirements(_intake);
    }

    override fun check(i: Int): Boolean
    {
      return currentVelocities[i] >= IntakeConstants.autoSpeed;
    }

    override fun runMotors() {
      intake.intake()
    }

    override fun getVels(): List<Double> {
      return listOf(intake.encoder.getVelocity());
    }
}
