package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.ctre.phoenix.motorcontrol.can.TalonFX
import com.ctre.phoenix.motorcontrol.can.TalonSRX
import edu.wpi.first.networktables.NetworkTableEntry
import edu.wpi.first.networktables.EntryListenerFlags
import edu.wpi.first.networktables.EntryNotification
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets
import com.ctre.phoenix.motorcontrol.*
import kotlin.math.*
import frc.robot.classes.LeaderFollower;

import frc.robot.ClimberConstants
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab

class Climber(tab: ShuffleboardTab) : SubsystemBase() {

    // declare motors and ports
    val pairs: Array<LeaderFollower> = arrayOf(
        LeaderFollower(TalonFX(ClimberConstants.Ports.left1), TalonFX(ClimberConstants.Ports.right1)),
        LeaderFollower(TalonFX(ClimberConstants.Ports.left2), TalonFX(ClimberConstants.Ports.right2)),
        LeaderFollower(TalonFX(ClimberConstants.Ports.left3), TalonFX(ClimberConstants.Ports.right3))
    )

    // configure the motors and add to shuffleboard
    init {
        for(pair in pairs) {
            pair.leader.apply {
                configFactoryDefault(100)
                setInverted(true)
                configPeakOutputForward(1.0)
                configPeakOutputReverse(-1.0)
            }

            pair.follower.apply {
                configFactoryDefault(100)
                follow(pair.leader)
                setInverted(InvertType.FollowMaster)
                configPeakOutputForward(1.0)
                configPeakOutputReverse(-1.0)
            }
        }
    }

    public fun stop() {
        for(pair in pairs) {
            pair.leader.set(ControlMode.PercentOutput, 0.0)
        }
    }

    public fun setPair(pair: Int, velocity: Double) {
        pairs[pair].leader.set(ControlMode.PercentOutput, velocity)
    }

    override fun periodic() {
    }

    override fun simulationPeriodic() {
    }
}