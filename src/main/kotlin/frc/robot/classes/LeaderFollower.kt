package frc.robot.classes
import com.ctre.phoenix.motorcontrol.can.TalonFX
import com.ctre.phoenix.motorcontrol.can.VictorSPX


open class LeaderFollower {
    val leader: TalonFX
    val follower: VictorSPX
    val invertedLeader : Boolean
    val invertedFollower : Boolean
    constructor(_leader: TalonFX, _invertedLeader : Boolean = false, _follower: VictorSPX, _invertedFollower : Boolean = false) {
        this.leader = _leader;
        this.follower = _follower;
        this.invertedLeader = _invertedLeader
        this.invertedFollower = _invertedFollower

    }
}