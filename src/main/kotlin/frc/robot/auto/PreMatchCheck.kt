package frc.robot.auto
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

import frc.robot.subsystems.*;
import frc.robot.commands.check.*;

import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab

class PreMatchCheck(
    m_climber: Climber,
    m_drivetrain: Drivetrain,
    m_feeder: Feeder,
    m_indexer: Indexer,
    m_intake: Intake,
    m_shooter: Shooter
) : SequentialCommandGroup(){
    val climber: Climber = m_climber;
    val drivetrain: Drivetrain = m_drivetrain;
    val feeder: Feeder = m_feeder;
    val indexer: Indexer = m_indexer;
    val intake: Intake = m_intake;
    val shooter: Shooter = m_shooter;
    val checkTab: ShuffleboardTab = Shuffleboard.getTab("Pre Match Check")

    init {
        // drivetrain.setDefaultCommand(null);
        // climber.setDefaultCommand(null);
        // feeder.setDefaultCommand(null);
        // indexer.setDefaultCommand(null);
        addCommands(
            AutoClimb(climber, 5.0, checkTab),
            AutoDrive(drivetrain, 5.0, checkTab),
            AutoFeed(feeder, 5.0, checkTab),
            AutoIndex(indexer, 5.0, checkTab),
            AutoIntake(intake, 5.0, checkTab),
            AutoShoot(shooter, 5.0, checkTab)
        )
    }
}
    

