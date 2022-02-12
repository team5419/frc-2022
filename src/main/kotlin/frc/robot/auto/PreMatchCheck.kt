package frc.robot.auto
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

import frc.robot.subsystems.Drivetrain
import frc.robot.subsystems.Shooter
import frc.robot.subsystems.Vision
import frc.robot.subsystems.Feeder

import frc.robot.commands.AutoDrive
import frc.robot.commands.Feed
import frc.robot.commands.Shoot

class PreMatchCheck(m_drivetrain: Drivetrain, m_shooter: Shooter, m_vision: Vision, m_feeder: Feeder) : SequentialCommandGroup(){

    val drivetrain: Drivetrain = m_drivetrain
    val shooter: Shooter = m_shooter
    val vision: Vision = m_vision
    val feeder: Feeder = m_feeder

    init {
        addCommands(
            AutoDrive(drivetrain, 5.0),
            Shoot(shooter, 1000.0, 1000.0, 5.0),
            Feed(feeder, 5.0)
        )
    }
}
    

