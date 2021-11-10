package org.team5419.frc2022.controllers

import org.team5419.frc2022.subsystems.*

import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab
import org.team5419.frc2022.subsystems.Storage.StorageMode
import org.team5419.frc2022.input.DriverControls
import org.team5419.frc2022.input.CodriverControls
import org.team5419.frc2022.input.driverXbox
import org.team5419.frc2022.input.codriverXbox
import org.team5419.frc2022.InputConstants
import org.team5419.frc2022.HoodConstants
import org.team5419.frc2022.fault.math.units.derived.*
import org.team5419.frc2022.fault.math.units.*
//import org.team5419.frc2022.fault.input.DriveHelper
import org.team5419.frc2022.fault.input.SpaceDriveHelper
import org.team5419.frc2022.fault.input.DriveSignal
import org.team5419.frc2022.fault.Controller
import edu.wpi.first.wpilibj.GenericHID.Hand
import edu.wpi.first.wpilibj.GenericHID.RumbleType
import edu.wpi.first.wpilibj.XboxController

import org.team5419.frc2022.StorageConstants

import org.team5419.frc2022.fault.util.time.ITimer
import org.team5419.frc2022.fault.util.time.WPITimer
import org.team5419.frc2022.fault.math.units.seconds

val tab: ShuffleboardTab = Shuffleboard.getTab("Master")
var complete = false
var slowMultiplier = InputConstants.SlowMoveMultiplier

@SuppressWarnings("LargeClass")
class TeleopController(val driver: DriverControls, val codriver: CodriverControls) : Controller {

    private val timer: ITimer = WPITimer()

    var isAligning = false
    //var isDeployed = false

    var shotSetpoint: ShotSetpoint = Hood.HoodPosititions.RESET

    data class Setpoint ( override val angle: Double, override val velocity: Double  ) : ShotSetpoint

    init {
        /*tab.addNumber("FAR Adjustment", { HoodConstants.Far.adjustment })
        tab.addNumber("TRUSS Adjustment", { HoodConstants.Truss.adjustment })
        tab.addNumber("CLOSE Adjustment", { HoodConstants.Close.adjustment })*/
    }

    private val driveHelper = SpaceDriveHelper(
        { driver.getThrottle() },
        { driver.getTurn() },
        { driver.fastTurn() },
        { isAligning || driver.slowMove() },
        { driver.superSlowMove() },
        InputConstants.JoystickDeadband,
        InputConstants.SlowTurnMultiplier,
        InputConstants.SlowMoveMultiplier,
        InputConstants.SuperSlowMoveMultiplier
    )

    override fun start() {
        Vision.zoomOut()
        timer.reset()
        timer.start()
    }

    override fun update() {
        updateDrivetrain()
        updateIntake()
        updateHood()
        updateShooger()
        updateStorage()
        updateClimber()
    }

    private fun updateDrivetrain() {
        if( driver.togleAligning() ) {
            isAligning = !isAligning
            if(isAligning) {
                // turn limelight leds on
                Vision.on()

                // put the drive train in brake mode to make autoaligning easiers
                Drivetrain.brakeMode = true
            } else {
                // turn limelight leds off
                Vision.off()

                // put the drive train back in coast mode
                Drivetrain.brakeMode = false

                // turn of any rumbling from the controller
                driverXbox.setRumble(RumbleType.kLeftRumble, 0.0)
                driverXbox.setRumble(RumbleType.kRightRumble, 0.0)
            }
        }

        val output = driveHelper.output()

        if ( isAligning ) {
            // if ( driver.adjustOffsetRight() >= InputConstants.TriggerDeadband ) {
            //     Vision.offset += driver.adjustOffsetRight() / 10
            // }

            // if ( driver.adjustOffsetLeft() >= InputConstants.TriggerDeadband ) {
            //     Vision.offset -= driver.adjustOffsetLeft() / 10
            // }

            val alignOutput = Vision.autoAlign()

            if ( Vision.aligned() ) {
                codriverXbox.setRumble(RumbleType.kLeftRumble, 0.1)
                codriverXbox.setRumble(RumbleType.kRightRumble, 0.1)
            }

            Drivetrain.setPercent(
                output.left + alignOutput.left,
                output.right + alignOutput.right
            )
        } else {
            Drivetrain.setPercent(output)
        }

        if( driver.slowMove()){
            slowMultiplier = InputConstants.SlowMoveMultiplier
            //driveHelper.slowMultiplier = slowMultiplier
        }
        else if (driver.superSlowMove()){
            slowMultiplier = InputConstants.SuperSlowMoveMultiplier
            //  driveHelper.slowMultiplier = slowMultiplier
        }
    }

    private fun updateIntake() {
        if( codriver.storeIntake() ){
            //isDeployed = !isDeployed
            Intake.store()
            println("intake store")
        } else {
            Intake.deploy()

        }

             if ( codriver.outtake() ) Intake.outtake()
        else if ( codriver.intake() ) Intake.intake()
        else Intake.stopIntake()
    }

    private fun resetAdjustment()
    {
        /*HoodConstants.Far.adjustment = 0.0
        HoodConstants.Truss.adjustment = 0.0
        HoodConstants.Close.adjustment = 0.0*/
    }

    @SuppressWarnings("ComplexMethod")
    private fun updateHood() {
        //Hood.checkAndReset()

        if ( codriver.deployHoodAuto() )
        {
            shotSetpoint = Hood.HoodPosititions.AUTO
            //resetAdjustment()
        }
        else if ( codriver.deployHoodTruss())
        {
            shotSetpoint = Hood.HoodPosititions.TRUSS
            //resetAdjustment()
        }
        else if ( codriver.deployHoodClose() )
        {
            shotSetpoint = Hood.HoodPosititions.CLOSE
            //resetAdjustment()
        }
        else if ( codriver.retractHood() || driver.retractHood() )
        {
            shotSetpoint = Hood.HoodPosititions.RETRACT
            //resetAdjustment()
        }
        /*else if ( driver.increaseShoogVelocity() && !complete)
        {
            if(shotSetpoint == Hood.HoodPosititions.FAR && HoodConstants.Far.angle+HoodConstants.Far.adjustment < 15.0 )
            {
                HoodConstants.Far.adjustment = HoodConstants.Far.adjustment + 0.25
            }
            else if (shotSetpoint == Hood.HoodPosititions.TRUSS &&
                    HoodConstants.Truss.angle+HoodConstants.Truss.adjustment < 15.0)
            {
                HoodConstants.Truss.adjustment = HoodConstants.Truss.adjustment  + 0.25
            }
            else if (shotSetpoint == Hood.HoodPosititions.CLOSE &&
                    HoodConstants.Close.angle+HoodConstants.Close.adjustment < 15.0)
            {
                HoodConstants.Close.adjustment = HoodConstants.Close.adjustment + 0.25
            }
            complete = true
        }
        else if ( driver.decreaseShoogVelocity() && !complete)
        {
            if(shotSetpoint == Hood.HoodPosititions.FAR &&
                    HoodConstants.Far.angle+HoodConstants.Far.adjustment > 1.0)
            {
                HoodConstants.Far.adjustment = HoodConstants.Far.adjustment - 0.25
            }
            else if (shotSetpoint == Hood.HoodPosititions.TRUSS &&
                    HoodConstants.Truss.angle+HoodConstants.Truss.adjustment > 1.0)
            {
                HoodConstants.Truss.adjustment = HoodConstants.Truss.adjustment - 0.25
            }
            else if (shotSetpoint == Hood.HoodPosititions.CLOSE &&
                    HoodConstants.Close.angle+HoodConstants.Close.adjustment > 1.0)
            {
                HoodConstants.Close.adjustment = HoodConstants.Close.adjustment - 0.25

            }
            complete = true
        }
        else if (!driver.decreaseShoogVelocity()) complete = false */


        if ( driver.adjustHoodUp() )
            shotSetpoint = Setpoint( shotSetpoint.angle + 0.2, shotSetpoint.velocity )
        else if ( driver.adjustHoodDown() )
            shotSetpoint = Setpoint( shotSetpoint.angle - 0.2, shotSetpoint.velocity )

        if ( false ) { // disabled until hood lookup table is complete
            println("Its true")
             // if the shot setpoint is not null, then set the shotSetpoint to it.
            Vision.getShotSetpoint()?.let { shotSetpoint = it }
        }

        Hood.goto( shotSetpoint )
    }

    private fun updateShooger() {
             if ( codriver.shoog() ) Shooger.shoog( shotSetpoint )
        else if ( codriver.spinUp() ) Shooger.spinUp( shotSetpoint )
        else Shooger.stop()

        if ( Shooger.isSpedUp() ) {
            codriverXbox.setRumble(RumbleType.kLeftRumble, 0.3)
            codriverXbox.setRumble(RumbleType.kRightRumble, 0.3)
        } else {
            codriverXbox.setRumble(RumbleType.kLeftRumble, 0.0)
            codriverXbox.setRumble(RumbleType.kRightRumble, 0.0)
        }
    }

    private fun updateStorage() {
        if ( codriver.reverseStorage() ) {
            Storage.reverse()
        } else {


            if( Shooger.isHungry() && Storage.isLoadedBall ){
                Storage.mode = StorageMode.LOAD
            } else if( Intake.isActive() || Shooger.isActive() ) {
                if(timer.get().value.seconds.rem(StorageConstants.LoopTime) < StorageConstants.OffTime) {
                    Storage.mode = StorageMode.REVERSE
                } else {
                    Storage.mode = StorageMode.PASSIVE
                }
            } else {
                Storage.mode = StorageMode.OFF
            }



            // if( Shooger.isHungry() ) {
            //     Storage.mode = StorageMode.LOAD
            // } else if ( Storage.mode == StorageMode.LOAD && !Storage.isLoadedBall ) {
            //     Storage.mode = StorageMode.PASSIVE
            // } else if ( Intake.isActive() || Shooger.isActive() ) {
            //     Storage.mode = StorageMode.PASSIVE
            // } else {
            //     Storage.mode = StorageMode.OFF
            // }
        }
    }

    private fun updateClimber() {
        if ( driver.climb() ) {
            Climber.deploy()
        } else if ( driver.unclimb() ) {
            Climber.retract()
        } else {
            Climber.stop()
        }

        if ( driver.winch() ) {
            Climber.winch()
        } else if (driver.unwinch() ){
            Climber.retractWinch()
        } else {
            Climber.stopWinch()
        }
    }

    override fun reset() {
        Vision.zoomOut()
        shotSetpoint = Hood.HoodPosititions.RESET
    }
}
