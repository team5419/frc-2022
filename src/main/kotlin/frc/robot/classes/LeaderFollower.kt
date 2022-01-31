package frc.robot.classes
import com.ctre.phoenix.motorcontrol.can.TalonFX

open class LeaderFollower {
    val leader: TalonFX
    val follower: TalonFX
    constructor(_leader: TalonFX, _follower: TalonFX) {
        this.leader = _leader;
        this.follower = _follower;
    }
}