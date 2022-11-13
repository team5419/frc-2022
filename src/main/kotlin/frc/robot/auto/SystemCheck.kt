package frc.robot.auto

import edu.wpi.first.math.geometry.Pose2d
import edu.wpi.first.math.geometry.Rotation2d

import edu.wpi.first.wpilibj.XboxController;

import frc.robot.subsystems.*
import frc.robot.commands.*

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;

class SystemCheck(_drivetrain: Drivetrain,  _catapult: Catapult, _intake: IntakeSub, _climber: Climber, _codriver: XboxController) : SequentialCommandGroup() {
    private val drivetrain: Drivetrain = _drivetrain
    private val catapult: Catapult =  _catapult
    private val intake: IntakeSub = _intake
    private val climber: Climber = _climber
    private val codriver: XboxController = _codriver
    
    init {

        addCommands(
            SequentialCommandGroup (
                Shoot(catapult),
                Shoot(catapult),
                ParallelCommandGroup (
                    MoveArm(climber, codriver, "left", false, 100.0),
                    MoveArm(climber, codriver, "right", false, 100.0)
                ),
                ParallelCommandGroup (
                    MoveArm(climber, codriver, "left", true, 100.0),
                    MoveArm(climber, codriver, "right", true, 100.0)
                ),
                Deploy(intake),
                Intake(intake),
                Deploy(intake),
                ValueDrive(drivetrain, 10.0, 0.0, 5.0),
                ValueDrive(drivetrain, -10.0, 0.0, 5.0),
                ValueDrive(drivetrain, 0.0, 10.0, 5.0),
                ValueDrive(drivetrain, 0.0, -10.0, 5.0)
            )
        )
    }
}