package frc.robot.auto
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

import frc.robot.subsystems.Drivetrain
import frc.robot.subsystems.Shooter
import frc.robot.subsystems.Vision
import frc.robot.subsystems.Indexer

import frc.robot.commands.check.AutoDrive


import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab

class PreMatchCheck(m_drivetrain: Drivetrain, m_shooter: Shooter, m_vision: Vision, m_indexer: Indexer) : SequentialCommandGroup(){

    val drivetrain: Drivetrain = m_drivetrain
    val shooter: Shooter = m_shooter
    val vision: Vision = m_vision
    val indexer: Indexer = m_indexer

    val checkTab: ShuffleboardTab = Shuffleboard.getTab("Pre Match Check")

    init {
        addCommands(
            AutoDrive(drivetrain, 5.0, checkTab)
            // Shoot(shooter, 1000.0, 1000.0, 5.0)
        )
    }
}
    

