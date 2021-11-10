package org.team5419.frc2022.auto.actions

//import org.team5419.frc2022.subsystems.Intake.DeployMode
import org.team5419.frc2022.subsystems.Intake
import org.team5419.frc2022.fault.auto.Action
import com.ctre.phoenix.motorcontrol.ControlMode

/*public class DeployIntakeAction(): ConfigDeployAction(DeployMode.DEPLOY)
public class RetractIntakeAction(): ConfigDeployAction(DeployMode.STORE)
public class DisableIntakeDeployAction(): ConfigDeployAction(DeployMode.OFF)*/

open class DeployIntakeAction(): Action() {
    override fun start() {
        Intake.deployMotor.set(ControlMode.PercentOutput, 0.0)
    }

    override fun next() = true

}
