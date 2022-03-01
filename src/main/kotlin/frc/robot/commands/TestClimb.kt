package frc.robot.commands
import frc.robot.commands.AutoClimb;
import frc.robot.subsystems.Climber;


import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab

class TestClimb(m_climber: Climber) : SequentialCommandGroup(){
    val climber: Climber = m_climber;

    init {
        addCommands(
            AutoClimb(climber, 0, 100.0, 0.2), // run 100 ticks on pair 1 at 20% power
            AutoClimb(climber, 1, 100.0, 0.2) // run 100 ticks on pair 2 at 20% power
        )
    }
}
    

