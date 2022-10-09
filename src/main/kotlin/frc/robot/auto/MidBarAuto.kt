package frc.robot.auto

import edu.wpi.first.math.geometry.Pose2d
import edu.wpi.first.math.geometry.Rotation2d

import edu.wpi.first.wpilibj.XboxController;

import frc.robot.subsystems.*
import frc.robot.commands.*

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;

import com.ctre.phoenix.motorcontrol.can.TalonFX

class MidBarAuto(_climber: Climber, m_codriver: XboxController) : SequentialCommandGroup() {
    val climber: Climber = _climber
    val codriver: XboxController = m_codriver

    val wantedHighestPosMid: Double = 0.0;
    val finalPosMid: Double = 0.0;
    
    init {
        //get encoder values at all the way down and high enough to reach mid
        //and automate
        //MotionMagic for talons / velocity position control

        addCommands(
            SequentialCommandGroup(
                MoveArm(climber, codriver, "left", wantedHighestPosMid),
                MoveArm(climber, codriver, "right", finalPosMid)
            )
        )
    }
}