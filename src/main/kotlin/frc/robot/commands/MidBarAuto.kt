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

    //val highestPosMidRight: Double;
    val finalPosMidRight: Double;
    val highestPosMidLeft: Double;
    val finalPosMidLeft: Double;
    
    init {
        //highestPosMidRight = 200555.0;
        finalPosMidRight = 6104.0;
        highestPosMidLeft = 0.0;
        finalPosMidLeft = 0.0;
        println("its working!!!")
        addCommands(
            SequentialCommandGroup(
                //MoveArm(climber, codriver, "right", false, highestPosMidRight),
                MoveArm(climber, codriver, "right", true, finalPosMidRight),
                MoveArm(climber, codriver, "left", false, highestPosMidLeft),
                MoveArm(climber, codriver, "left", true, finalPosMidLeft)
            )
        )
    }
}