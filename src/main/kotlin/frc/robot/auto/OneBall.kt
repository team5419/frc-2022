package frc.robot.auto

import edu.wpi.first.math.geometry.Pose2d
import edu.wpi.first.math.geometry.Rotation2d

import edu.wpi.first.wpilibj.XboxController;

import frc.robot.subsystems.*
import frc.robot.commands.*

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;

class OneBall(_drivetrain: Drivetrain, _catapult: Catapult, _intake: IntakeSub) : SequentialCommandGroup() {
    private val drivetrain: Drivetrain = _drivetrain
    private val catapult: Catapult =  _catapult
    private val intake: IntakeSub = _intake
    
    init {

        addCommands(
            SequentialCommandGroup (
                Wait(10.0),
                Shoot(catapult),
                Shoot(catapult)
            )
        )
    }
}