package org.team5419.frc2022.auto

import org.team5419.frc2022.fault.auto.*
import org.team5419.frc2022.fault.math.geometry.Pose2d
import org.team5419.frc2022.fault.math.geometry.Vector2
import org.team5419.frc2022.fault.math.geometry.Pose2dWithCurvature
import org.team5419.frc2022.fault.math.units.*
import org.team5419.frc2022.fault.math.units.derived.*
import org.team5419.frc2022.fault.trajectory.DefaultTrajectoryGenerator
import org.team5419.frc2022.fault.trajectory.constraints.TimingConstraint
import org.team5419.frc2022.subsystems.Drivetrain
import org.team5419.frc2022.auto.actions.*
import org.team5419.frc2022.auto.actions.RamseteAction
import org.team5419.frc2022.subsystems.Hood.HoodPosititions
import org.team5419.frc2022.subsystems.Setpoint
import org.team5419.frc2022.subsystems.Storage.StorageMode
import org.team5419.frc2022.DriveConstants
import edu.wpi.first.wpilibj.trajectory.TrajectoryUtil
import edu.wpi.first.wpilibj.trajectory.Trajectory


val routines = arrayOf<Routine>(
    Routine("In front of target", Pose2d(),
        // shoog from starting position
        IntakeAction(),
        AutoHoodAction(),
        SpinUpAction(),
        AutoAlignAction(),
        TimedShoogAction(3.seconds),

        // bring hood back down and turn intake on

        RetractHoodAction(),
        DeployIntakeAction(),

        // // navigate to behind trench

        //DisableStorageAction(),
        RamseteAction( arrayOf<Pose2d>(
            Pose2d(0.0.meters, 0.0.meters, 0.0.degrees),
            Pose2d(4.5.meters, 0.0.meters, 0.0.degrees)
        ) ),
        AutoHoodAction(),
        SpinUpAction(),
        RamseteAction( arrayOf<Pose2d>(
            Pose2d(4.5.meters, 0.0.meters, 0.0.degrees),
            Pose2d(0.0.meters, 0.5.meters, -17.0.degrees)
        ), reversed = true ),

        // align and shoog
        //SpinUpAction(),
        AutoAlignAction(),
        //
        //AutoAlignAction(),
        TimedShoogAction(6.seconds),
        DisableStorageAction(),
        DisableIntakeAction()

    ),

    Routine("Anything else", Pose2d(),
        IntakeAction(),
        AutoHoodAction(),
        SpinUpAction(),
        AutoAlignAction(),
        TimedShoogAction(7.seconds),

        RamseteAction( arrayOf<Pose2d>(
            Pose2d(0.0.meters, 0.0.meters, 0.0.degrees),
            Pose2d(1.5.meters, 0.0.meters, 0.0.degrees)
        ) )
    ),

    Routine("Enemy steal", Pose2d(),
        IntakeAction(),
        DeployIntakeAction(),
        RamseteAction( arrayOf<Pose2d>(
            Pose2d(0.0.meters, 0.0.meters, 0.0.degrees),
            Pose2d(2.75.meters, 0.0.meters, -7.0.degrees)
        )),
        RamseteAction( arrayOf<Pose2d>(
            Pose2d(2.75.meters, 0.0.meters, -7.0.degrees),
            Pose2d(-0.5.meters, -3.5.meters, 17.0.degrees)
        ), reversed = true),
        AutoHoodAction(),
        SpinUpAction(),
        AutoAlignAction(),
        TimedShoogAction(10.seconds),

        DisableIntakeAction(),
        DisableStorageAction(),
        RetractHoodAction()
    ),

    Routine("Enemy steal with extra ball", Pose2d(),
        IntakeAction(),
        DeployIntakeAction(),
        RamseteAction( arrayOf<Pose2d>(
            Pose2d(0.0.meters, 0.0.meters, 0.0.degrees),
            Pose2d(2.75.meters, 0.0.meters, 15.0.degrees) // pick up trench balls
        )),
        AutoHoodAction(),
        SpinUpAction(),
        RamseteAction( arrayOf<Pose2d>(
            Pose2d(2.75.meters, 0.0.meters, 15.0.degrees),
            Pose2d(-0.5.meters, -3.5.meters, 17.0.degrees) // go to auto line and shoot
        ), reversed = true),
        AutoAlignAction(),
        TimedShoogAction(5.seconds),
        RamseteAction( arrayOf<Pose2d>(
            Pose2d(-0.5.meters, -3.5.meters, 17.0.degrees),
            Pose2d(3.2.meters, -3.5.meters, 0.0.degrees) // drive back to pick up more balls (only if we have time)
        )),
        DisableIntakeAction(),
        DisableStorageAction(),
        RetractHoodAction()
    )
)
