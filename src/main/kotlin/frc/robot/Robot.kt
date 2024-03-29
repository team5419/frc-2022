package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;

import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab
import edu.wpi.first.networktables.NetworkTableInstance

val tab: ShuffleboardTab = Shuffleboard.getTab("Master")

class Robot : TimedRobot() {
  private val m_robotContainer = RobotContainer(tab);
  private var m_autonomousCommand = m_robotContainer.getAutonomousCommand();

  override fun robotInit() {
    NetworkTableInstance.getDefault().setUpdateRate(0.01)
  }

  override fun robotPeriodic() {
    // schedule/manage commands and run subsystem periodic methods
    CommandScheduler.getInstance().run();
  }

  override fun disabledInit() {
    m_robotContainer.onTeleop()
  }

  override fun disabledPeriodic() {}

  // runs autonomous command selected in RobotContainer.kt
  override fun autonomousInit() {
    m_robotContainer.onAuto()
    m_autonomousCommand = m_robotContainer.getAutonomousCommand();
    if(m_autonomousCommand != null) {
      m_autonomousCommand.schedule();
    }
  }

  override fun autonomousPeriodic() {}

  override fun teleopInit() {
    m_robotContainer.onTeleop()
    // cancel all autonomous commands when teleop starts
    if(m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }
  }

  override fun teleopPeriodic() {}

  override fun testInit() {
    CommandScheduler.getInstance().cancelAll();
  }

  override fun testPeriodic() {}
}
