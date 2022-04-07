package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.commands.*;


import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab
import edu.wpi.first.networktables.NetworkTableInstance

val tab: ShuffleboardTab = Shuffleboard.getTab("Master")

class Robot : TimedRobot() {
  private val m_robotContainer = RobotContainer(tab);
  private var m_autonomousCommand = m_robotContainer.getAutonomousCommand();

  override fun robotInit() {
    NetworkTableInstance.getDefault().setUpdateRate(0.05)
  }

  override fun robotPeriodic() {
    // schedule/manage commands and run subsystem periodic methods
    CommandScheduler.getInstance().run();
  }

  override fun disabledInit() {
    m_robotContainer.lightsOff();
  }

  override fun disabledPeriodic() {}

  // runs autonomous command selected in RobotContainer.kt
  override fun autonomousInit() {
    m_autonomousCommand = m_robotContainer.getAutonomousCommand();
    m_robotContainer.setDefaults();
    if(m_autonomousCommand != null) {
      m_autonomousCommand.schedule();
    }
    m_robotContainer.m_drivetrain.resetOdometry()
  }

  override fun autonomousPeriodic() {}

  override fun teleopInit() {
    // cancel all autonomous commands when teleop starts
    if(m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }
    LimitCurrent(m_robotContainer.m_drivetrain)

  }

  override fun teleopPeriodic() {}

  override fun testInit() {
    CommandScheduler.getInstance().cancelAll();
  }

  override fun testPeriodic() {}
}
